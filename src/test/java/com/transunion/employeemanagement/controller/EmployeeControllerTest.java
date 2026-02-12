package com.transunion.employeemanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transunion.employeemanagement.dto.CreateEmployeeRequest;
import com.transunion.employeemanagement.model.Employee;
import com.transunion.employeemanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @Test
    void shouldCreateEmployee() throws Exception {
        CreateEmployeeRequest request = new CreateEmployeeRequest(
                "John Doe", "john.doe@example.com", "Engineering", 35, "Manager"
        );

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.title").value("Manager"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void shouldGetAllEmployees() throws Exception {
        employeeRepository.saveAll(List.of(
                new Employee("Alice", "alice@example.com", "HR", 25, "Recruiter"),
                new Employee("Bob", "bob@example.com", "IT", 40, "Architect")
        ));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldFilterEmployeesByAgeAndTitle() throws Exception {
        employeeRepository.saveAll(List.of(
                new Employee("Alice", "alice@example.com", "HR", 25, "Manager"),
                new Employee("Bob", "bob@example.com", "IT", 40, "Manager"),
                new Employee("Charlie", "charlie@example.com", "IT", 45, "Developer")
        ));

        // Filter: age > 30 and title = "Manager"
        // Should only return Bob (40 > 30)
        mockMvc.perform(get("/api/employees")
                .param("minAge", "30")
                .param("title", "Manager"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Bob"));
    }

    @Test
    void shouldReturnEmptyListWhenNoEmployeeMatchesFilter() throws Exception {
        employeeRepository.saveAll(List.of(
                new Employee("Alice", "alice@example.com", "HR", 25, "Manager")
        ));

        mockMvc.perform(get("/api/employees")
                .param("minAge", "30")
                .param("title", "Manager"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldFailToCreateEmployeeWithInvalidEmail() throws Exception {
        CreateEmployeeRequest request = new CreateEmployeeRequest(
                "John Doe", "invalid-email", "Engineering", 35, "Manager"
        );

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());
    }
}
