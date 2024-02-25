package com.example.assignment1_1.components;

import com.example.assignment1_1.controller.DealershipController;
import com.example.assignment1_1.domain.Dealership;
import com.example.assignment1_1.dtos.DealershipDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DealershipModelAssembler implements RepresentationModelAssembler<Dealership, EntityModel<Dealership>> {
    @Override
    public EntityModel<Dealership> toModel(Dealership dealership) {
        return EntityModel.of(dealership,
                linkTo(methodOn(DealershipController.class).findDealershipById(dealership.getId())).withSelfRel(),
                linkTo(methodOn(DealershipController.class).findAllDealerships(0, 25)).withRel("dealerships")
        );
    }
}
