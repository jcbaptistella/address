package com.apirest.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apirest.address.models.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

	Address findById(long id);

}
