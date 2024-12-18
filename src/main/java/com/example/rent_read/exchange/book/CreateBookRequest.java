package com.example.rent_read.exchange.book;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class CreateBookRequest {

  @NotBlank(message = "Title is mandatory")
  private String title;

  @NotBlank(message = "Author is mandatory")
  private String author;

  @NotBlank(message = "Genre is mandatory")
  private String genre;

  private Boolean isAvailable;
}
