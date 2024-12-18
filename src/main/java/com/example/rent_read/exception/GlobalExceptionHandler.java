package com.example.rent_read.exception;

import com.example.rent_read.exception.book.*;
import com.example.rent_read.exception.customer.CustomerAlreadyExistsException;
import com.example.rent_read.exception.customer.CustomerNotFoundException;
import com.example.rent_read.exception.customer.UnauthorizedAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorMessage handleException(Exception ex, WebRequest request) {
    return new ErrorMessage(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        new Date(),
        ex.getMessage(),
        request.getDescription(false)
    );
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorMessage handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
    Map<String, String> map = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      map.put(fieldName, errorMessage);
    });

    return new ErrorMessage(
        HttpStatus.BAD_REQUEST.value(),
        new Date(),
        "Validation error(s)",
        request.getDescription(false),
        map
    );
  }

  @ExceptionHandler(CustomerAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorMessage handleCustomerAlreadyExistsException(CustomerAlreadyExistsException ex, WebRequest request) {
    return new ErrorMessage(
        HttpStatus.CONFLICT.value(),
        new Date(),
        ex.getMessage(),
        request.getDescription(false)
    );
  }

  @ExceptionHandler(CustomerNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorMessage handleCustomerNotFoundException(CustomerNotFoundException ex, WebRequest request) {
    return new ErrorMessage(
        HttpStatus.NOT_FOUND.value(),
        new Date(),
        ex.getMessage(),
        request.getDescription(false)
    );
  }

  @ExceptionHandler(UnauthorizedAccessException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorMessage handleUnauthorizedAccessException(UnauthorizedAccessException ex, WebRequest request) {
    return new ErrorMessage(
        HttpStatus.CONFLICT.value(),
        new Date(),
        ex.getMessage(),
        request.getDescription(false)
    );
  }

  @ExceptionHandler(BookNotAvailableException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorMessage handleBookNotAvailableException(BookNotAvailableException ex, WebRequest request) {
    return new ErrorMessage(
        HttpStatus.CONFLICT.value(),
        new Date(),
        ex.getMessage(),
        request.getDescription(false)
    );
  }

  @ExceptionHandler(BookNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorMessage handleBookNotFoundException(BookNotFoundException ex, WebRequest request) {
    return new ErrorMessage(
        HttpStatus.NOT_FOUND.value(),
        new Date(),
        ex.getMessage(),
        request.getDescription(false)
    );
  }

  @ExceptionHandler(BookNotRentedException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorMessage handleBookNotRentedException(BookNotRentedException ex, WebRequest request) {
    return new ErrorMessage(
        HttpStatus.CONFLICT.value(),
        new Date(),
        ex.getMessage(),
        request.getDescription(false)
    );
  }

  @ExceptionHandler(BookRentalLimitReachedException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorMessage handleBookRentalLimitReachedException(BookRentalLimitReachedException ex, WebRequest request) {
    return new ErrorMessage(
        HttpStatus.CONFLICT.value(),
        new Date(),
        ex.getMessage(),
        request.getDescription(false)
    );
  }

  @ExceptionHandler(BookRentedByDifferentCustomerException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorMessage handleBookRentedByDifferentCustomerException(BookRentedByDifferentCustomerException ex, WebRequest request) {
    return new ErrorMessage(
        HttpStatus.CONFLICT.value(),
        new Date(),
        ex.getMessage(),
        request.getDescription(false)
    );
  }

  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ErrorMessage handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {

    return new ErrorMessage(
        HttpStatus.UNAUTHORIZED.value(),
        new Date(),
        ex.getMessage(),
        request.getDescription(false)
    );
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  public ErrorMessage handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest request) {
    return new ErrorMessage(
        HttpStatus.METHOD_NOT_ALLOWED.value(),
        new Date(),
        ex.getMessage(),
        request.getDescription(false)
    );

  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorMessage handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {

    return new ErrorMessage(
        HttpStatus.BAD_REQUEST.value(),
        new Date(),
        "Invalid type for path variable. Expected type: " + ex.getRequiredType().getSimpleName(),
        request.getDescription(false)
    );
  }

  @ExceptionHandler(NoResourceFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorMessage handleNoResourceFoundException(NoResourceFoundException ex, WebRequest request) {
    return new ErrorMessage(
        HttpStatus.BAD_REQUEST.value(),
        new Date(),
        "No resource found",
        request.getDescription(false)
    );

  }
}
