package com.example.assignment1_1.components;

import com.example.assignment1_1.controller.CarController;
import com.example.assignment1_1.domain.Car;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CarModelAssembler implements RepresentationModelAssembler<Car, EntityModel<Car>> {
    @Override
    public EntityModel<Car> toModel(Car car) {
        return EntityModel.of(car,
                linkTo(methodOn(CarController.class).getCarById(car.getId())).withSelfRel(),
                linkTo(methodOn(CarController.class).getAllCars(0, 25)).withRel("cars")
        );
    }
}
