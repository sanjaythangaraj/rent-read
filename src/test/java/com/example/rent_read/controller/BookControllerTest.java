package com.example.rent_read.controller;

import com.example.rent_read.exception.book.*;
import com.example.rent_read.exchange.book.BookResponse;
import com.example.rent_read.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookController.class)
public class BookControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private BookService bookService;

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "CUSTOMER")
  void testRentBookSuccess() throws Exception {
    Long bookId = 1L;
    BookResponse bookResponse = new BookResponse(bookId, "Fight Club", "Chuck Palahniuk", "Drama", false);

    when(bookService.rentBook(bookId)).thenReturn(bookResponse);

    mockMvc.perform(post("/api/books/{id}/rent", bookId).with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(bookId))
        .andExpect(jsonPath("$.title").value("Fight Club"))
        .andExpect(jsonPath("$.author").value("Chuck Palahniuk"))
        .andExpect(jsonPath("$.genre").value("Drama"))
        .andExpect(jsonPath("$.isAvailable").value(false));
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "CUSTOMER")
  void testRentBookForRentalLimitReached() throws Exception {
    Long bookId = 1L;

    when(bookService.rentBook(bookId)).thenThrow(new BookRentalLimitReachedException());

    mockMvc.perform(post("/api/books/{id}/rent", bookId).with(csrf()))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.statusCode").value(409))
        .andExpect(jsonPath("$.message").value("Book rental limit of 2 already reached"))
        .andExpect(jsonPath("$.description").value("uri=/api/books/" + bookId + "/rent"));
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "CUSTOMER")
  void testRentBookForBookNotFound() throws Exception {
    Long bookId = 1L;

    when(bookService.rentBook(bookId)).thenThrow(new BookNotFoundException());

    mockMvc.perform(post("/api/books/{id}/rent", bookId).with(csrf()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.statusCode").value(404))
        .andExpect(jsonPath("$.message").value("Book with given id not found"))
        .andExpect(jsonPath("$.description").value("uri=/api/books/" + bookId + "/rent"));

  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "CUSTOMER")
  void testRentBookForBookNotAvailable() throws Exception {
    Long bookId = 1L;

    when(bookService.rentBook(bookId)).thenThrow(new BookNotAvailableException());

    mockMvc.perform(post("/api/books/{id}/rent", bookId).with(csrf()))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.statusCode").value(409))
        .andExpect(jsonPath("$.message").value("Book currently not available to rent"))
        .andExpect(jsonPath("$.description").value("uri=/api/books/" + bookId + "/rent"));

  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "CUSTOMER")
  void testReturnBookSuccess() throws Exception {
    Long bookId = 1L;
    BookResponse bookResponse = new BookResponse(bookId, "Fight Club", "Chuck Palahniuk", "Drama", true);

    when(bookService.returnBook(bookId)).thenReturn(bookResponse);

    mockMvc.perform(post("/api/books/{id}/return", bookId).with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(bookId))
        .andExpect(jsonPath("$.title").value("Fight Club"))
        .andExpect(jsonPath("$.author").value("Chuck Palahniuk"))
        .andExpect(jsonPath("$.genre").value("Drama"))
        .andExpect(jsonPath("$.isAvailable").value(true));
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "CUSTOMER")
  void testReturnBookForBookAvailableToRent() throws Exception {
    Long bookId = 1L;

    when(bookService.returnBook(bookId)).thenThrow(new BookNotRentedException());

    mockMvc.perform(post("/api/books/{id}/return", bookId).with(csrf()))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.statusCode").value(409))
        .andExpect(jsonPath("$.message").value("Book has not been rented yet or has been returned"))
        .andExpect(jsonPath("$.description").value("uri=/api/books/" + bookId + "/return"));
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "CUSTOMER")
  void testReturnBookForBookRentedByDifferentCustomer() throws Exception {
    Long bookId = 1L;

    when(bookService.returnBook(bookId)).thenThrow(new BookRentedByDifferentCustomerException());

    mockMvc.perform(post("/api/books/{id}/return", bookId).with(csrf()))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.statusCode").value(409))
        .andExpect(jsonPath("$.message").value("Can't return a book that hasn't been rented by you"))
        .andExpect(jsonPath("$.description").value("uri=/api/books/" + bookId + "/return"));
  }
}
