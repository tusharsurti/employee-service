package com.transunion.employeemanagement.controller;

import com.transunion.employeemanagement.dto.CreateEmployeeRequest;
import com.transunion.employeemanagement.dto.EmployeeRecord;
import com.transunion.employeemanagement.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee API", description = "Operations for managing employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new employee")
    public EmployeeRecord createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        return employeeService.createEmployee(request);
    }

    @GetMapping
    @Operation(summary = "Get all employees with optional filters",
               description = "Returns employees. If minAge is provided, returns employees with age > minAge. If title is provided, filters by title.")
    public List<EmployeeRecord> getAllEmployees(
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) String title) {
        return employeeService.getAllEmployees(minAge, title);
    }
}
