package com.tcs.transactionmovement.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.tcs.transactionmovement.client.ClientFeignClient;
import com.tcs.transactionmovement.dto.ClientResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@EnableFeignClients(clients = ClientFeignClient.class)
@TestPropertySource(
    properties = {
      "client.url=http://localhost:8089",
      "spring.main.allow-bean-definition-overriding=true",
      "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration"
    })
class ClientFeignClientIntegrationTest {

  private static final Long TEST_CLIENT_ID = 1L;
  private static final String TEST_CLIENT_CODE = "CLI-123456";
  private static final String TEST_IDENTIFICATION = "1234567890";

  @Autowired private ClientFeignClient clientFeignClient;

  @Autowired private ObjectMapper objectMapper;

  private WireMockServer wireMockServer;

  @BeforeEach
  void setup() {
    wireMockServer = new WireMockServer(8089);
    wireMockServer.start();
    WireMock.configureFor("localhost", 8089);
  }

  @AfterEach
  void tearDown() {
    if (wireMockServer != null) {
      wireMockServer.stop();
    }
  }

  private String createClientResponseJson() {
    try {
      ClientResponse response =
          ClientResponse.builder()
              .id(TEST_CLIENT_ID)
              .clientCode(TEST_CLIENT_CODE)
              .name("Daniel Arias")
              .gender("MASCULINO")
              .age(25)
              .identification(TEST_IDENTIFICATION)
              .address("Calle 123")
              .phone("777-1234")
              .status(true)
              .build();
      return objectMapper.writeValueAsString(response);
    } catch (Exception e) {
      throw new RuntimeException("Error creating test JSON", e);
    }
  }

  @Test
  void getClient_shouldReturnClientDetails() {
    WireMock.stubFor(
        WireMock.get(WireMock.urlEqualTo("/?id=" + TEST_CLIENT_ID))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .withBody(createClientResponseJson())));

    ResponseEntity<ClientResponse> response = clientFeignClient.getClient(TEST_CLIENT_ID);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    ClientResponse client = response.getBody();
    assertNotNull(client);
    assertEquals(TEST_CLIENT_ID, client.getId());
    assertEquals(TEST_CLIENT_CODE, client.getClientCode());
    assertEquals("Daniel Arias", client.getName());
    assertEquals(TEST_IDENTIFICATION, client.getIdentification());
    assertTrue(client.getStatus());

    WireMock.verify(WireMock.getRequestedFor(WireMock.urlEqualTo("/?id=" + TEST_CLIENT_ID)));
  }

  @Test
  void getClient_shouldHandleNotFound() {
    Long nonExistentId = 999L;

    WireMock.stubFor(
        WireMock.get(WireMock.urlEqualTo("/?id=" + nonExistentId))
            .willReturn(WireMock.aResponse().withStatus(404)));

    assertThrows(Exception.class, () -> clientFeignClient.getClient(nonExistentId));

    WireMock.verify(WireMock.getRequestedFor(WireMock.urlEqualTo("/?id=" + nonExistentId)));
  }
}
