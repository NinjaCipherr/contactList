package io.greenCode.contactList.entity;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Contact
 */
@Data
@Entity
@Table(name = "contacts")
public class Contact {
  @Id
  @UuidGenerator
  @Column(name = "id", unique = true, updatable = false)
  private String id;
  private String email;
  private String name;
  private String title;
  private String phoneNumber;
  private String adress;
  private String status;
  private String photoUrl;

}
