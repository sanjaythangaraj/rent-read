package com.example.rent_read.controller;

import com.example.rent_read.exception.customer.UnauthorizedAccessException;
import com.example.rent_read.exchange.book.BookResponse;
import com.example.rent_read.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CustomerService customerService;

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "CUSTOMER")
  void testGetRentedBooksSuccess() throws Exception {
    Long customerId = 1L;
    List<BookResponse> bookResponses = List.of(new BookResponse(1L, "Fight Club", "Chuck Palahniuk", "Drama", false));

    when(customerService.getRentedBooks(customerId)).thenReturn(bookResponses);

    mockMvc.perform(get("/api/customers/{id}/books", customerId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].title").value("Fight Club"))
        .andExpect(jsonPath("$[0].author").value("Chuck Palahniuk"))
        .andExpect(jsonPath("$[0].genre").value("Drama"))
        .andExpect(jsonPath("$[0].isAvailable").value(false));

  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "CUSTOMER")
  void testGetRentedBookForUnauthorizedAccess() throws Exception {
    Long customerId = 1L;

    when(customerService.getRentedBooks(customerId)).thenThrow(new UnauthorizedAccessException());

    mockMvc.perform(get("/api/customers/{id}/books", customerId))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.statusCode").value(409))
        .andExpect(jsonPath("$.message").value("Unauthorized access - customers can only access their own resources"))
        .andExpect(jsonPath("$.description").value("uri=/api/customers/" + customerId + "/books"));

  }

}
