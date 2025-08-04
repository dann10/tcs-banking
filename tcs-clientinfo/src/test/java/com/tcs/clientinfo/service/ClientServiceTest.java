package com.tcs.clientinfo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.tcs.clientinfo.dto.ClientRequest;
import com.tcs.clientinfo.dto.ClientResponse;
import com.tcs.clientinfo.exception.BankingException;
import com.tcs.clientinfo.mapper.ClientMapper;
import com.tcs.clientinfo.model.Client;
import com.tcs.clientinfo.model.Person;
import com.tcs.clientinfo.repository.ClientRepository;
import com.tcs.clientinfo.repository.PersonRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

  @Mock private PersonRepository personRepository;
  @Mock private ClientRepository clientRepository;
  @Mock private ClientMapper clientMapper;

  @InjectMocks private ClientService clientService;

  private ClientRequest clientRequest;
  private Person existingPerson;
  private ClientResponse clientResponse;

  @BeforeEach
  void setUp() {
    clientRequest =
        ClientRequest.builder()
            .name("Daniel Arias")
            .gender("MASCULINO")
            .age(25)
            .identification("1234567890")
            .address("Calle 123")
            .phone("777-1234")
            .password("Pass123*")
            .build();

    existingPerson =
        Person.builder()
            .name("Daniel Arias")
            .gender("MASCULINO")
            .age(30)
            .identification("1234567890")
            .address("Calle 123")
            .phone("777-1234")
            .build();

    clientResponse =
        ClientResponse.builder().id(1L).name("Daniel Arias").identification("1234567890").build();
  }

  @Test
  void createClient_NewPerson_Success() {
    // Arrange
    when(personRepository.findByIdentification(anyString())).thenReturn(Optional.empty());
    when(personRepository.save(any(Person.class))).thenReturn(existingPerson);
    when(clientRepository.save(any(Client.class)))
        .thenAnswer(
            invocation -> {
              Client c = invocation.getArgument(0);
              c.setClientId(1L);
              c.setClientCode("CLI-ABC123");
              return c;
            });
    when(clientMapper.toResponse(any(Client.class))).thenReturn(clientResponse);

    // Act
    ClientResponse result = clientService.createClient(clientRequest);

    // Assert
    assertNotNull(result);
    assertEquals("1234567890", result.getIdentification());
    verify(personRepository).save(any(Person.class));
    verify(clientRepository).save(any(Client.class));
    verify(clientMapper).toResponse(any(Client.class));
  }

  @Test
  void createClient_ExistingPersonNoClient_Success() {
    // Arrange
    when(personRepository.findByIdentification(anyString()))
        .thenReturn(Optional.of(existingPerson));
    when(clientRepository.existsByPersonAndStatusTrue(any(Person.class))).thenReturn(false);
    when(clientRepository.save(any(Client.class)))
        .thenAnswer(
            invocation -> {
              Client c = invocation.getArgument(0);
              c.setClientId(1L);
              c.setClientCode("CLI-ABC123");
              return c;
            });
    when(clientMapper.toResponse(any(Client.class))).thenReturn(clientResponse);

    // Act
    ClientResponse result = clientService.createClient(clientRequest);

    // Assert
    assertNotNull(result);
    assertEquals("Daniel Arias", result.getName());
    verify(personRepository, never()).save(any());
    verify(clientRepository).save(any(Client.class));
  }

  @Test
  void createClient_PersonExistsWithActiveClient_ThrowsException() {
    // Arrange
    when(personRepository.findByIdentification(anyString()))
        .thenReturn(Optional.of(existingPerson));
    when(clientRepository.existsByPersonAndStatusTrue(any(Person.class))).thenReturn(true);

    // Act & Assert
    BankingException exception =
        assertThrows(BankingException.class, () -> clientService.createClient(clientRequest));

    assertEquals("Esta persona ya tiene un cliente activo", exception.getMessage());
    verify(clientRepository, never()).save(any());
  }

  @Test
  void createClient_StatusIsAlwaysTrueForNewClient() {
    // Arrange
    when(personRepository.findByIdentification(anyString())).thenReturn(Optional.empty());
    when(personRepository.save(any(Person.class))).thenReturn(existingPerson);
    when(clientRepository.save(any(Client.class)))
        .thenAnswer(
            invocation -> {
              Client c = invocation.getArgument(0);
              c.setClientId(1L);
              return c;
            });

    // Act
    clientService.createClient(clientRequest);

    // Assert
    verify(clientRepository).save(argThat(Client::getStatus));
  }

  @Test
  void createClient_AllPersonDataIsCorrectlyMapped() {
    // Arrange
    when(personRepository.findByIdentification(anyString())).thenReturn(Optional.empty());
    when(personRepository.save(any(Person.class))).thenReturn(existingPerson);
    when(clientRepository.save(any(Client.class)))
        .thenAnswer(
            invocation -> {
              Client c = invocation.getArgument(0);
              c.setClientId(1L);
              return c;
            });
    when(clientMapper.toResponse(any(Client.class))).thenReturn(clientResponse);

    // Act
    clientService.createClient(clientRequest);

    // Assert
    verify(personRepository)
        .save(
            argThat(
                person ->
                    person.getName().equals("Daniel Arias")
                        && person.getIdentification().equals("1234567890")
                        && person.getGender().equals("MASCULINO")
                        && person.getAge() == 25
                        && person.getAddress().equals("Calle 123")
                        && person.getPhone().equals("777-1234")));
  }
}
