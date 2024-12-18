package com.example.rent_read.data;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class BookEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String author;

  private String genre;

  private Boolean isAvailable;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private CustomerEntity customerEntity;

}
