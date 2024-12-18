package com.example.rent_read.repository;

import com.example.rent_read.data.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

  Optional<CustomerEntity> findOneByEmail(String email);
}
