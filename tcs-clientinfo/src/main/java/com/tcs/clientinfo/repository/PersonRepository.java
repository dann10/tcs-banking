package com.tcs.clientinfo.repository;

import com.tcs.clientinfo.model.Person;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
  boolean existsByIdentification(String identification);
  Optional<Person> findByIdentification(String identification);
}
