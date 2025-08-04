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
@Builder
@Table(name = "CLIENT", schema = "BANKING_TCS")
public class Client {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "CLIENT_ID")
  private Long clientId;

  @OneToOne
  @JoinColumn(name = "PERSON_ID", referencedColumnName = "PERSON_ID", nullable = false)
  private Person person;

  @Column(name = "CLIENT_CODE", unique = true, nullable = false, length = 50)
  private String clientCode;

  @Column(name = "PASSWORD", nullable = false, length = 100)
  private String password;

  @Column(name = "STATUS", columnDefinition = "BOOLEAN DEFAULT TRUE")
  private Boolean status;

  @Column(name = "CREATED_AT")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "UPDATED_AT")
  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
