package com.transunion.employeemanagement.dto;

public record EmployeeRecord(
    Long id,
    String name,
    String email,
    String department,
    int age,
    String title
) {}
