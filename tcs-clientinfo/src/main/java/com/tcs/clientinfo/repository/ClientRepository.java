package com.tcs.clientinfo.repository;

import com.tcs.clientinfo.model.Client;
import com.tcs.clientinfo.model.Person;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findAllByStatus(boolean status);
    boolean existsByPersonAndStatusTrue(Person person);
}