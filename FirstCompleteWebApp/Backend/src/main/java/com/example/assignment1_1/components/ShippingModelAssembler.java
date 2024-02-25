package com.example.assignment1_1.components;

import com.example.assignment1_1.controller.ContractController;
import com.example.assignment1_1.domain.ShippingContract;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ShippingModelAssembler implements RepresentationModelAssembler<ShippingContract, EntityModel<ShippingContract>> {
    @Override
    public EntityModel<ShippingContract> toModel(ShippingContract shippingContract) {
        return EntityModel.of(shippingContract,
                linkTo(methodOn(ContractController.class).findShippingById(shippingContract.getId())).withSelfRel(),
                linkTo(methodOn(ContractController.class).findAll(0, 25)).withRel("suppliers")
        );
    }
}
