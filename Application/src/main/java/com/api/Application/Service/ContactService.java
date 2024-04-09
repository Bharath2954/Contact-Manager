package com.api.Application.Service;
import com.api.Application.Model.*;
import com.api.Application.Constant.Constant;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.Application.Repository.ContactRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ContactService {
	@Autowired
	private ContactRepository repo;
	
	public Page<Contact> getAllContacts(int page, int size)
	{
		return repo.findAll(PageRequest.of(page, size, Sort.by("name")));
	}
	
	public Contact getContact(String id)
	{
		return repo.findById(id).orElseThrow(() -> new RuntimeException("Contact not Found"));
	}
	
	public Contact createContact(Contact contact)
	{
		return repo.save(contact);
	}
	
	public String deleteContact(String id)
	{
		if(getContact(id)!=null)
		{
			repo.deleteById(id);
			return "Deletion Successfull";
		}
		return "Contact not Found";
	}
	
	public String photoUpload(String id, MultipartFile file)
	{
		Contact contact = getContact(id);
		String photoUrl = photoFunction(id, file);
		contact.setPhotoURL(photoUrl);
		repo.save(contact);
		return photoUrl;
	}
	private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains("."))
			.map(name -> "."+name.substring(name.lastIndexOf(".")+1)).orElse(".png");
	
	private String photoFunction(String id, MultipartFile image) {
	    try {
	        java.nio.file.Path filePath = Paths.get(Constant.PhotoDirectory()).toAbsolutePath().normalize();
	        if (!Files.exists(filePath)) {
	            Files.createDirectories(filePath);
	        }
	        
	        // Check if file with the same name exists
	        String fileName = id + fileExtension.apply(image.getOriginalFilename());
	        java.nio.file.Path existingFilePath = filePath.resolve(fileName);
	        if (Files.exists(existingFilePath)) {
	            Files.delete(existingFilePath); // Delete existing file
	        }
	        
	        Files.copy(image.getInputStream(), existingFilePath); // Copy new file
	        return ServletUriComponentsBuilder
	                .fromCurrentContextPath()
	                .path("/contacts/image/" + fileName)
	                .toUriString();
	    } catch (Exception exception) {
	        throw new RuntimeException("Unable to save image");
	    }
	}
}
