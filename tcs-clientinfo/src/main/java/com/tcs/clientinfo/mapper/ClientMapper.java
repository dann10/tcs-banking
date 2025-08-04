package com.tcs.clientinfo.mapper;

import com.tcs.clientinfo.dto.ClientRequest;
import com.tcs.clientinfo.dto.ClientResponse;
import com.tcs.clientinfo.model.Client;
import com.tcs.clientinfo.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "id", source = "clientId")
    @Mapping(target = "name", source = "person.name")
    @Mapping(target = "gender", source = "person.gender")
    @Mapping(target = "age", source = "person.age")
    @Mapping(target = "identification", source = "person.identification")
    @Mapping(target = "address", source = "person.address")
    @Mapping(target = "phone", source = "person.phone")
    @Mapping(target = "status", source = "status")
    ClientResponse toResponse(Client client);

    @Mapping(target = "person", expression = "java(mapRequestToPerson(request))")
    @Mapping(target = "clientId", ignore = true)
    @Mapping(target = "clientCode", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    Client toEntity(ClientRequest request);

    @Named("mapRequestToPerson")
    default Person mapRequestToPerson(ClientRequest request) {
        if (request == null) {
            return null;
        }

        return Person.builder()
                .name(request.getName())
                .gender(request.getGender())
                .age(request.getAge())
                .identification(request.getIdentification())
                .address(request.getAddress())
                .phone(request.getPhone())
                .build();
    }
}