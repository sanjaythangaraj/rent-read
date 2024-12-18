package com.example.rent_read.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class ErrorMessage {
  private final int statusCode;
  private final Date timestamp;
  private final String message;
  private final String description;
  private Map<String, String> details = new HashMap<>();
}
