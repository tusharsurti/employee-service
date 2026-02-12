package com.transunion.employeemanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateEmployeeRequest(
    @NotBlank(message = "Name is mandatory")
    String name,
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    String email,
    @NotBlank(message = "Department is mandatory")
    String department,
    @Positive(message = "Age must be positive")
    int age,
    @NotBlank(message = "Title is mandatory")
    String title
) {}
