package com.example.assignment1_1.service;

import com.example.assignment1_1.components.ShippingModelAssembler;
import com.example.assignment1_1.controller.ContractController;
import com.example.assignment1_1.domain.Dealership;
import com.example.assignment1_1.domain.ShippingContract;
import com.example.assignment1_1.dtos.ContractDTO;
import com.example.assignment1_1.dtos.EmployeeDTO;
import com.example.assignment1_1.dtos.UserDto;
import com.example.assignment1_1.exceptions.AppException;
import com.example.assignment1_1.repo.*;
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

import java.util.Date;
import java.util.List;

@Service
public class ContractService {

    @Autowired
    private ShippingContractRepo shippingContractRepo;

    @Autowired
    private MyContractRepo myContractRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DealershipRepo dealershipRepo;

    @Autowired
    private SupplierRepo supplierRepo;

    @Autowired
    private PageSizeRepo pageSizeRepo;

    @Autowired
    private ShippingModelAssembler shippingModelAssembler;


    public ResponseEntity<List<ContractDTO>> findAllContracts(int page, int size){
        if (!pageSizeRepo.findAll().isEmpty()) {
            size = pageSizeRepo.findAll().get(0).getPageSize().intValue();
        }

        Pageable paging = PageRequest.of(page, size);
        Page<ContractDTO> pagedResult = myContractRepo.findAllBy(paging);

        return new ResponseEntity<>(pagedResult.getContent(), HttpStatus.OK);
    }

    public EntityModel<ShippingContract> findShippingById(@PathVariable Long id) {
        return shippingModelAssembler.toModel(shippingContractRepo.findById(id).orElseThrow(() -> new RuntimeException("Shipping not found")));
    }

    public ResponseEntity<?> saveShipping(@RequestBody ShippingContract shippingContract) {
        EntityModel<ShippingContract> entityModel = shippingModelAssembler.toModel(shippingContractRepo.save(shippingContract));

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    public ResponseEntity<?> updateShipping(@RequestBody ShippingContract shippingContract, @PathVariable Long id) {
        ShippingContract updatedShippingContract = shippingContractRepo.findById(id)
                .map(s -> {
                    s.setContractDate(shippingContract.getContractDate());
                    s.setContractYearsDuration(shippingContract.getContractYearsDuration());
                    return shippingContractRepo.save(s);
                })
                .orElseGet(() -> {
                    shippingContract.setId(id);
                    return shippingContractRepo.save(shippingContract);
                });

        EntityModel<ShippingContract> entityModel = shippingModelAssembler.toModel(updatedShippingContract);

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    public ResponseEntity<?> updateShippingsDto(List<ContractDTO> contractDTOs) {
        for (ContractDTO contractDTO : contractDTOs) {
            ShippingContract updatedShippingContract = shippingContractRepo.findById(contractDTO.getId())
                    .map(s -> {
                        s.setContractDate(contractDTO.getContractDate());
                        s.setContractYearsDuration(contractDTO.getContractYearsDuration());
                        s.setSupplier(supplierRepo.findById(contractDTO.getSupplierId()).orElseThrow(() -> new RuntimeException("Supplier not found")));
                        s.setDealership(dealershipRepo.findById(contractDTO.getDealershipId()).orElseThrow(() -> new RuntimeException("Dealership not found")));

                        return shippingContractRepo.save(s);
                    })
                    .orElseGet(() -> {
                        ShippingContract shippingContract = new ShippingContract();
                        shippingContract.setId(contractDTO.getId());
                        shippingContract.setContractDate(contractDTO.getContractDate());
                        shippingContract.setContractYearsDuration(contractDTO.getContractYearsDuration());
                        shippingContract.setSupplier(supplierRepo.findById(contractDTO.getSupplierId()).orElseThrow(() -> new RuntimeException("Supplier not found")));
                        shippingContract.setDealership(dealershipRepo.findById(contractDTO.getDealershipId()).orElseThrow(() -> new RuntimeException("Dealership not found")));
                        shippingContract.setAuthor(userRepo.findByUsername(contractDTO.getAuthorUsername()).orElseThrow(() -> new RuntimeException("User not found")));
                        return shippingContractRepo.save(shippingContract);
                    });
        }

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> deleteShipping(@PathVariable Long id) {
        shippingContractRepo.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    public boolean isContractAuthor(Long id, Authentication authentication) {
        UserDto user = (UserDto) authentication.getPrincipal();
        ShippingContract contract = shippingContractRepo.findById(id)
                .orElseThrow(() -> new AppException("Contract not found", HttpStatus.NOT_FOUND));

        return contract.getAuthor().getUsername().equals(user.getUsername());
    }

    public boolean isContractsAuthor(List<ContractDTO> contracts, Authentication authentication) {
        for (ContractDTO d: contracts) {
            if (!isContractAuthor(d.getId(), authentication)) {
                return false;
            }
        }

        return true;
    }

    public ResponseEntity<?> deleteAll() {
        try {
            myContractRepo.deleteAll();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }

        return ResponseEntity.ok().build();
    }
}
