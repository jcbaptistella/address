package com.apirest.address.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.address.models.Address;
import com.apirest.address.service.AddressService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping(value = "/api")
public class AddressController {

	@Autowired
	AddressService addressService;

	@GetMapping("/address")
	public List<Address> get() {

		List<Address> address = addressService.get();

		return address;
	}
	
	@GetMapping("/address/{id}")
	public Address getById(@PathVariable(value="id") long id) {

		Address address = addressService.findById(id);

		return address;
	}

	@PostMapping("/address")
	public ResponseEntity<Object> insert(@RequestBody Address request) throws JsonMappingException, JsonProcessingException {

		ResponseEntity<Object> response = addressService.insert(request);

		return response;
	}

	@DeleteMapping("/address/{id}")
	public ResponseEntity<Object> delete(@PathVariable(value = "id") long id) {
		ResponseEntity<Object> response = addressService.delete(id);

		return response;
	}

	@PutMapping("/address/{id}")
	public ResponseEntity<Object> update(@PathVariable long id, @RequestBody Address request) throws JsonMappingException, JsonProcessingException {
		ResponseEntity<Object> response = addressService.update(id, request);

		return response;
	}
}