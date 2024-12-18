package com.example.rent_read.exchange.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class UpdateBookRequest {
  private String title;

  private String author;

  private String genre;

  private Boolean isAvailable;
}
