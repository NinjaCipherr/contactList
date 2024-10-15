package io.greenCode.contactList.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.greenCode.contactList.entity.Contact;

/**
 * ContactRepository
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
  Optional<Contact> findById(String id);
}
