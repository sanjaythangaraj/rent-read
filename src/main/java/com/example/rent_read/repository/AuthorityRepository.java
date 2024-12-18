package com.example.rent_read.repository;

import com.example.rent_read.data.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {
  Optional<AuthorityEntity> findByName(String name);
}
