package com.transunion.employeemanagement.service;

import com.transunion.employeemanagement.dto.CreateEmployeeRequest;
import com.transunion.employeemanagement.dto.EmployeeRecord;
import com.transunion.employeemanagement.model.Employee;
import com.transunion.employeemanagement.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public EmployeeRecord createEmployee(CreateEmployeeRequest request) {
        Employee employee = new Employee(
                request.name(),
                request.email(),
                request.department(),
                request.age(),
                request.title()
        );
        Employee savedEmployee = employeeRepository.save(employee);

        log.info("New employee added: ID={}, Name={}, Title={}, Department={}",
                savedEmployee.getId(), savedEmployee.getName(), savedEmployee.getTitle(), savedEmployee.getDepartment());

        return mapToRecord(savedEmployee);
    }

    public List<EmployeeRecord> getAllEmployees(Integer minAge, String title) {
        Specification<Employee> spec = Specification.where(null);

        if (minAge != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThan(root.get("age"), minAge));
        }

        if (title != null && !title.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("title"), title));
        }

        return employeeRepository.findAll(spec).stream()
                .map(this::mapToRecord)
                .collect(Collectors.toList());
    }

    private EmployeeRecord mapToRecord(Employee employee) {
        return new EmployeeRecord(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment(),
                employee.getAge(),
                employee.getTitle()
        );
    }
}
