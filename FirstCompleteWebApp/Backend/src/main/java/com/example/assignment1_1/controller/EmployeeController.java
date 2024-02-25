package com.example.assignment1_1.controller;

import com.example.assignment1_1.components.EmployeeModelAssembler;
import com.example.assignment1_1.domain.Employee;
import com.example.assignment1_1.dtos.EmployeeDTO;
import com.example.assignment1_1.exceptions.EmployeeNotFoundException;
import com.example.assignment1_1.repo.EmployeeRepo;
import com.example.assignment1_1.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping(path = "/")
    public ResponseEntity<List<EmployeeDTO>> findAllEmployees(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                              @RequestParam(value = "size", defaultValue = "25", required = false) int size) {
        return employeeService.findAllEmployees(page, size);
    }

    @GetMapping(path = "/{id}")
    public EntityModel<Employee> findEmployeeById(@PathVariable Long id) {
        return employeeService.findEmployeeById(id);
    }

    @PostMapping()
    public ResponseEntity<?> saveEmployee(@Valid @RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or" +
            " hasRole('ROLE_MODERATOR') or" +
            " (hasRole('ROLE_REGULAR') and @employeeService.isEmployeeAuthor(#id, authentication))")
    public ResponseEntity<?> updateEmployee(@Valid @RequestBody Employee newEmployee, @PathVariable Long id) {
        return employeeService.updateEmployee(newEmployee, id);
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or" +
            " hasRole('ROLE_MODERATOR') or" +
            " (hasRole('ROLE_REGULAR') and @employeeService.isEmployeesAuthor(#employeeDtos, authentication))")
    public ResponseEntity<?> updateEmployeesDto(@RequestBody List<EmployeeDTO> employeeDtos) {
        return employeeService.updateEmployeesDto(employeeDtos);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or" +
            " hasRole('ROLE_MODERATOR') or" +
            " (hasRole('ROLE_REGULAR') and @employeeService.isEmployeeAuthor(#id, authentication))")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        return employeeService.deleteEmployee(id);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAll() {
        return employeeService.deleteAll();
    }
}
