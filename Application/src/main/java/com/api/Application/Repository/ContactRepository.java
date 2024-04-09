package com.api.Application.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.Application.Model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
	Optional<Contact> findById(String id);
}
