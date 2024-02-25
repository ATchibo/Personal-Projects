package com.example.assignment1_1.controller;

import com.example.assignment1_1.components.ShippingModelAssembler;
import com.example.assignment1_1.domain.ShippingContract;
import com.example.assignment1_1.dtos.CarDTO;
import com.example.assignment1_1.dtos.ContractDTO;
import com.example.assignment1_1.repo.ShippingContractRepo;
import com.example.assignment1_1.service.ContractService;
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
@RequestMapping("/api/contracts")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @GetMapping(path = "/")
    public ResponseEntity<List<ContractDTO>> findAll(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                        @RequestParam(value = "size", defaultValue = "25", required = false) int size)  {
        return contractService.findAllContracts(page, size);
    }

    @GetMapping(path = "/{id}")
    public EntityModel<ShippingContract> findShippingById(@PathVariable Long id) {
        return contractService.findShippingById(id);
    }

    @PostMapping()
    public ResponseEntity<?> saveShipping(@RequestBody ShippingContract shippingContract) {
        return contractService.saveShipping(shippingContract);
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or" +
            " hasRole('ROLE_MODERATOR') or" +
            " (hasRole('ROLE_REGULAR') and @contractService.isContractAuthor(#id, authentication))")
    public ResponseEntity<?> updateShipping(@RequestBody ShippingContract shippingContract, @PathVariable Long id) {
        return contractService.updateShipping(shippingContract, id);
    }

    @PutMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN') or" +
            " hasRole('ROLE_MODERATOR') or" +
            " (hasRole('ROLE_REGULAR') and @contractService.isContractsAuthor(#contractDtos, authentication))")
    public ResponseEntity<?> updateShippingsDto(@RequestBody List<ContractDTO> contractDtos) {
        return contractService.updateShippingsDto(contractDtos);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or" +
            " hasRole('ROLE_MODERATOR') or" +
            " (hasRole('ROLE_REGULAR') and @contractService.isContractAuthor(#id, authentication))")
    public ResponseEntity<?> deleteShipping(@PathVariable Long id) {
        return contractService.deleteShipping(id);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAll() {
        return contractService.deleteAll();
    }
}
