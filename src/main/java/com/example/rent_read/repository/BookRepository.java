package com.example.rent_read.repository;

import com.example.rent_read.data.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
  List<BookEntity> findAllByIsAvailableIsTrue();
}
