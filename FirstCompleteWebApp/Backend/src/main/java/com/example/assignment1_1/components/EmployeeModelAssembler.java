package com.example.assignment1_1.components;

import com.example.assignment1_1.controller.EmployeeController;
import com.example.assignment1_1.domain.Employee;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {
    @Override
    public EntityModel<Employee> toModel(Employee employee) {
        return EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).findEmployeeById(employee.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).findAllEmployees(0, 25)).withRel("employees")
        );
    }
}
