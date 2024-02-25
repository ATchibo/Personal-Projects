package com.example.assignment1_1.components;

import com.example.assignment1_1.controller.SupplierController;
import com.example.assignment1_1.domain.Supplier;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SupplierModelAssembler implements RepresentationModelAssembler<Supplier, EntityModel<Supplier>> {
    @Override
    public EntityModel<Supplier> toModel(Supplier supplier) {
        return EntityModel.of(supplier,
                linkTo(methodOn(SupplierController.class).findSupplierById(supplier.getId())).withSelfRel(),
                linkTo(methodOn(SupplierController.class).findAllSuppliers(0, 25)).withRel("suppliers")
        );
    }
}
