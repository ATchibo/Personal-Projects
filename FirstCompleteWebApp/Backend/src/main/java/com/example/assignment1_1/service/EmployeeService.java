package com.example.assignment1_1.service;

import com.example.assignment1_1.components.EmployeeModelAssembler;
import com.example.assignment1_1.controller.EmployeeController;
import com.example.assignment1_1.domain.Dealership;
import com.example.assignment1_1.domain.Employee;
import com.example.assignment1_1.dtos.CarDTO;
import com.example.assignment1_1.dtos.EmployeeDTO;
import com.example.assignment1_1.dtos.UserDto;
import com.example.assignment1_1.exceptions.AppException;
import com.example.assignment1_1.exceptions.EmployeeNotFoundException;
import com.example.assignment1_1.repo.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private MyEmployeeRepo myEmployeeRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DealershipRepo dealershipRepo;

    @Autowired
    private PageSizeRepo pageSizeRepo;

    @Autowired
    private EmployeeModelAssembler employeeModelAssembler;

    public ResponseEntity<List<EmployeeDTO>> findAllEmployees(int page, int size){
        if (!pageSizeRepo.findAll().isEmpty()) {
            size = pageSizeRepo.findAll().get(0).getPageSize().intValue();
        }

        Pageable paging = PageRequest.of(page, size);
        Page<EmployeeDTO> pagedResult = myEmployeeRepo.findAllBy(paging);

        return new ResponseEntity<>(pagedResult.getContent(), HttpStatus.OK);
    }

    public EntityModel<Employee> findEmployeeById(@PathVariable Long id) {
        Employee employee = employeeRepo.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));

        return employeeModelAssembler.toModel(employee);
    }

    public ResponseEntity<?> saveEmployee(@Valid @RequestBody Employee employee) {
        EntityModel<Employee> entityModel = employeeModelAssembler.toModel(employeeRepo.save(employee));

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    public ResponseEntity<?> updateEmployee(Employee newEmployee, Long id) {
        Employee updatedEmployee = employeeRepo.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    employee.setEmail(newEmployee.getEmail());
                    employee.setPhone(newEmployee.getPhone());
                    employee.setSalary(newEmployee.getSalary());
                    employee.setDealership(newEmployee.getDealership());
                    return employeeRepo.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return employeeRepo.save(newEmployee);
                });

        EntityModel<Employee> entityModel = employeeModelAssembler.toModel(updatedEmployee);

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    public ResponseEntity<?> updateEmployeesDto(List<EmployeeDTO> employeeDTOS) {
        for (EmployeeDTO employeeDTO: employeeDTOS) {
            Employee updatedEmployee = employeeRepo.findById(employeeDTO.getId())
                    .map(employee -> {
                        return getEmployee(employeeDTO, employee);
                    })
                    .orElseGet(() -> {
                        Employee newEmployee = new Employee();
                        newEmployee.setId(employeeDTO.getId());
                        return getEmployee(employeeDTO, newEmployee);
                    });
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Employee getEmployee(EmployeeDTO employeeDTO, Employee newEmployee) {
        newEmployee.setName(employeeDTO.getName());
        newEmployee.setRole(employeeDTO.getRole());
        newEmployee.setEmail(employeeDTO.getEmail());
        newEmployee.setPhone(employeeDTO.getPhone());
        newEmployee.setSalary(employeeDTO.getSalary());
        newEmployee.setDealership(dealershipRepo.findById(employeeDTO.getDealershipId())
                .orElseThrow(() -> new EmployeeNotFoundException(employeeDTO.getDealershipId())));
        newEmployee.setAuthor(userRepo.findByUsername(employeeDTO.getAuthorUsername())
                .orElseThrow(() -> new EmployeeNotFoundException(employeeDTO.getAuthorId())));
        return employeeRepo.save(newEmployee);
    }

    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        employeeRepo.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    public boolean isEmployeeAuthor(Long id, Authentication authentication) {
        UserDto user = (UserDto) authentication.getPrincipal();
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new AppException("Employee not found", HttpStatus.NOT_FOUND));

        return employee.getAuthor().getUsername().equals(user.getUsername());
    }

    public boolean isEmployeesAuthor(List<EmployeeDTO> employees, Authentication authentication) {
        for (EmployeeDTO d : employees) {
            if (!isEmployeeAuthor(d.getId(), authentication)) {
                return false;
            }
        }

        return true;
    }

    public ResponseEntity<?> deleteAll() {
        try {
            myEmployeeRepo.deleteAll();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }

        return ResponseEntity.ok().build();
    }
}
