package com.api.Application.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.api.Application.Constant.Constant;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import com.api.Application.Model.Contact;
import com.api.Application.Service.ContactService;
import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/contacts")
public class ContactController 
{
	@Autowired
	private ContactService service;
	
	@PostMapping("/create")
	public Contact create(@RequestBody Contact contact)
	{
		return service.createContact(contact);
	}
	
	@GetMapping("/getAllContacts")
	public Page<Contact> getAll(@RequestParam(value="page", defaultValue="0") int page, @RequestParam(value="size", defaultValue="10") int size)
	{
		return service.getAllContacts(page, size);
	}
	
	@GetMapping("/getContact/{id}")
	public Contact getContact(@PathVariable String id)
	{
		return service.getContact(id);
	}
	
	@PutMapping("/photo")
	public String uploadPhoto(@RequestParam("id") String id, @RequestParam("file") MultipartFile file)
	{
		return service.photoUpload(id, file);
	}
	
	@GetMapping(value = "/image/{filename}", produces = {IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE})
    public byte[] getPhoto(@PathVariable String filename) throws IOException {
        return Files.readAllBytes(Paths.get(Constant.PhotoDirectory() + filename));
    }
	
	@DeleteMapping("/remove/{id}")
	public String delete(@PathVariable String id)
	{
		return service.deleteContact(id);
	}
}
