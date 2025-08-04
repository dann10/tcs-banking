package com.tcs.clientinfo.service;

import static com.tcs.clientinfo.util.ErrorType.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcs.clientinfo.config.SecurityConfig;
import com.tcs.clientinfo.dto.ClientRequest;
import com.tcs.clientinfo.dto.ClientResponse;
import com.tcs.clientinfo.dto.DisableAccount;
import com.tcs.clientinfo.exception.BankingException;
import com.tcs.clientinfo.mapper.ClientMapper;
import com.tcs.clientinfo.model.Client;
import com.tcs.clientinfo.model.Person;
import com.tcs.clientinfo.repository.ClientRepository;
import com.tcs.clientinfo.repository.PersonRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ClientService {

  private final PersonRepository personRepository;
  private final ClientRepository clientRepository;
  private final ClientMapper clientMapper;
  private final ProducerTemplate producerTemplate;
  private final ObjectMapper objectMapper;

  public ClientResponse createClient(ClientRequest request) {
    Optional<Person> existingPerson =
        personRepository.findByIdentification(request.getIdentification());
    Person person;

    if (existingPerson.isPresent()) {
      person = existingPerson.get();
      if (clientRepository.existsByPersonAndStatusTrue(person)) {
        log.error("Esta persona {} ya tiene un cliente activo", person.getIdentification());
        throw new BankingException("Esta persona ya tiene un cliente activo", DUPLICATE_ENTRY);
      }
    } else {
      person =
          Person.builder()
              .name(request.getName())
              .gender(request.getGender())
              .age(request.getAge())
              .identification(request.getIdentification())
              .address(request.getAddress())
              .phone(request.getPhone())
              .build();

      personRepository.save(person);
    }

    Client client =
        Client.builder()
            .person(person)
            .clientCode(generateClientCode())
            .password(SecurityConfig.hashPassword(request.getPassword()))
            .status(true)
            .build();

    log.info("Cliente {} creado exitosamente", client.getClientCode());
    return clientMapper.toResponse(clientRepository.save(client));
  }

  @Transactional(readOnly = true)
  public ClientResponse getClient(Long id) {
    Client client =
        clientRepository
            .findById(id)
            .orElseThrow(() -> new BankingException("Cliente no encontrado", NOT_FOUND));

    if (Boolean.FALSE.equals(client.getStatus())) {
      throw new BankingException("Cliente eliminado, contactar con administracion", CONFLICT);
    }

    return clientMapper.toResponse(client);
  }

  public ClientResponse updateClient(Long id, ClientRequest request) {
    Client existingClient =
        clientRepository
            .findById(id)
            .orElseThrow(() -> new BankingException("Cliente no encontrado", NOT_FOUND));

    Person updatedPerson = clientMapper.mapRequestToPerson(request);
    existingClient.getPerson().updateFrom(updatedPerson);

    if (request.getPassword() != null && !request.getPassword().isEmpty()) {
      existingClient.setPassword(SecurityConfig.hashPassword(request.getPassword()));
    }
    log.info("Cliente {} editado exitosamente", existingClient.getClientCode());
    return clientMapper.toResponse(clientRepository.save(existingClient));
  }

  public void deleteClient(Long id) {
    Client client =
        clientRepository
            .findById(id)
            .orElseThrow(() -> new BankingException("Cliente no encontrado", NOT_FOUND));

    if (Boolean.FALSE.equals(client.getStatus())) {
      throw new BankingException("El cliente fue previamente eliminado", CONFLICT);
    }

    client.setStatus(false);
    try {
      producerTemplate.sendBody(
          "direct:disable_accounts",
          objectMapper.writeValueAsString(
              DisableAccount.builder().clientId(client.getClientId()).build()));
    } catch (JsonProcessingException e) {
      log.error("Error al crear json de desactivacion: {}", e.getMessage());
      throw new BankingException(
          "Error al crear json de desactivacion: " + e.getMessage(), CONFLICT);
    }
    log.info("Cliente {} eliminado exitosamente", client.getClientCode());
    clientRepository.save(client);
  }

  @Transactional(readOnly = true)
  public List<ClientResponse> getClientList() {
    return clientRepository.findAllByStatus(true).stream().map(clientMapper::toResponse).toList();
  }

  @Transactional(readOnly = true)
  public Boolean isClientActive(Long id) {
    return clientRepository
        .findById(id)
        .map(Client::getStatus)
        .orElseThrow(() -> new BankingException("Cliente no encontrado", NOT_FOUND));
  }

  private String generateClientCode() {
    return "CLI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }
}
