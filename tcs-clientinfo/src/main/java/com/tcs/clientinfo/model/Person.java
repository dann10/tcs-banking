package com.tcs.clientinfo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PERSON", schema = "BANKING_TCS")
@Builder
public class Person {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "PERSON_ID")
  private Long personId;

  @Column(name = "NAME", nullable = false, length = 100)
  private String name;

  @Column(name = "GENDER", length = 20)
  private String gender;

  @Column(name = "AGE")
  private Integer age;

  @Column(name = "IDENTIFICATION", unique = true, nullable = false, length = 20)
  private String identification;

  @Column(name = "ADDRESS", length = 200)
  private String address;

  @Column(name = "PHONE", length = 20)
  private String phone;

  @Column(name = "CREATED_AT")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "UPDATED_AT")
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public void updateFrom(Person personIn) {
    this.name = personIn.getName();
    this.gender = personIn.getGender();
    this.age = personIn.getAge();
    this.address = personIn.getAddress();
    this.phone = personIn.getPhone();
  }
}
