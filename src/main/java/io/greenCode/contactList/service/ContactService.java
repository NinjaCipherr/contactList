package io.greenCode.contactList.service;

import static io.greenCode.contactList.constant.Constant.PHOTO_DIRECTORY;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.greenCode.contactList.entity.Contact;
import io.greenCode.contactList.repository.ContactRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * ContactService
 * 
 */
@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
public class ContactService {

  private ContactRepository contactRepository;

  public ContactService(ContactRepository contactRepository) {
    this.contactRepository = contactRepository;
  }

  public Page<Contact> getAllContacts(int page, int size) {
    return contactRepository.findAll(PageRequest.of(page, size, Sort.by("name")));
  }

  public Contact getContact(String id) {
    return contactRepository.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
  }

  public Contact createContact(Contact contact) {
    return contactRepository.save(contact);
  }

  // public void deleteContact(Contact contact) {
  // log.info("Deleting contact: {}", contact);

  // String id = contact.getId();

  // if(contact.getPhotoUrl() != null){
  // String filename
  // }
  // }

  public String upLoadPhoto(String id, MultipartFile file) {
    log.info("Saving picture for user ID: {}", id);
    Contact contact = getContact(id);
    String photoUrl = photoFunction.apply(id, file);
    contact.setPhotoUrl(photoUrl);
    contactRepository.save(contact);
    return photoUrl;
  }

  private final Function<String, String> fileExtension = (filename) -> Optional.of(filename)
      .filter(name -> name.contains("."))
      .map(name -> "." + name.substring(name.lastIndexOf(".") + 1))
      .orElse(".png");
  private final BiFunction<String, MultipartFile, String> photoFunction = (id, image) -> {
    String fileName = id + fileExtension.apply(image.getOriginalFilename());
    try {
      Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
      if (!Files.exists(fileStorageLocation)) {
        Files.createDirectories(fileStorageLocation);
      }
      Files.copy(image.getInputStream(), fileStorageLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
      return ServletUriComponentsBuilder
          .fromCurrentContextPath()
          .path("/contacts/image/" + fileName).toUriString();
    } catch (Exception e) {
      // TODO: handle exception
      throw new RuntimeException("Unable to save image");
    }
  };
}
