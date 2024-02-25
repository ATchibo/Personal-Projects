package com.example.assignment1_1.service;

import com.example.assignment1_1.components.SupplierModelAssembler;
import com.example.assignment1_1.domain.AppUser;
import com.example.assignment1_1.domain.Dealership;
import com.example.assignment1_1.domain.ShippingContract;
import com.example.assignment1_1.domain.Supplier;
import com.example.assignment1_1.dtos.SupplierByNrShippings;
import com.example.assignment1_1.dtos.SupplierDTO;
import com.example.assignment1_1.dtos.UserDto;
import com.example.assignment1_1.exceptions.AppException;
import com.example.assignment1_1.exceptions.SupplierNotFoundException;
import com.example.assignment1_1.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepo supplierRepo;

    @Autowired
    private MySupplierRepo mySupplierRepo;

    @Autowired
    private DealershipRepo dealershipRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ShippingContractRepo shippingContractRepo;

    @Autowired
    private PageSizeRepo pageSizeRepo;

    @Autowired
    private SupplierModelAssembler supplierModelAssembler;

    public ResponseEntity<List<SupplierDTO>> findAllSuppliers(int page, int size) {
        if (!pageSizeRepo.findAll().isEmpty()) {
            size = pageSizeRepo.findAll().get(0).getPageSize().intValue();
        }

        Pageable paging = PageRequest.of(page, size);
        Page<SupplierDTO> pagedResult = mySupplierRepo.findAllBy(paging);

        return new ResponseEntity<>(pagedResult.getContent(), HttpStatus.OK);
    }

    public EntityModel<Supplier> findSupplierById(@PathVariable Long id) {
        Supplier supplier = supplierRepo.findById(id).orElseThrow(() -> new SupplierNotFoundException(id));

        return supplierModelAssembler.toModel(supplier);
    }

    public ResponseEntity<List<SupplierDTO>> findSuppliersByName(String name, int page, int size) {
        if (!pageSizeRepo.findAll().isEmpty()) {
            size = pageSizeRepo.findAll().get(0).getPageSize().intValue();
        }

        Pageable paging = PageRequest.of(page, size);
        Page<SupplierDTO> pagedResult = mySupplierRepo.findAllByName(name, paging);

        return new ResponseEntity<>(pagedResult.getContent(), HttpStatus.OK);
    }

    public List<SupplierByNrShippings> findAllSuppliersByNrShippings(int page, int size) {
        if (!pageSizeRepo.findAll().isEmpty()) {
            size = pageSizeRepo.findAll().get(0).getPageSize().intValue();
        }

        Pageable paging = PageRequest.of(page, size);
        return mySupplierRepo.findAllByNumberOfShippings(paging).stream().toList();
    }

    public ResponseEntity<?> saveSupplier(@RequestBody Supplier supplier) {
        EntityModel<Supplier> entityModel = supplierModelAssembler.toModel(supplierRepo.save(supplier));

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    public EntityModel<Supplier> addDealershipToSupplier(@PathVariable Long id, @RequestBody List<Dealership> dealerships) {
        Supplier supplier = supplierRepo.findById(id).orElseThrow(() -> new SupplierNotFoundException(id));

        for (Dealership dealership: dealerships) {
            ShippingContract shippingContract = new ShippingContract(new Date(), 1);

            shippingContractRepo.save(shippingContract);

            dealership.getShippingContracts().add(shippingContract);
            dealershipRepo.save(dealership);
            supplier.getShippingContracts().add(shippingContract);
            supplierRepo.save(supplier);
        }

        return supplierModelAssembler.toModel(supplier);
    }

    public ResponseEntity<?> updateSupplier(@RequestBody Supplier supplier, @PathVariable Long id) {
        Supplier updatedSupplier = supplierRepo.findById(id)
                .map(s -> {
                    s.setName(supplier.getName());
                    s.setEmail(supplier.getEmail());
                    s.setPhone(supplier.getPhone());
                    return supplierRepo.save(s);
                })
                .orElseGet(() -> {
                    supplier.setId(id);
                    return supplierRepo.save(supplier);
                });

        EntityModel<Supplier> entityModel = supplierModelAssembler.toModel(updatedSupplier);

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    public ResponseEntity<?> updateSuppliersDTO(List<SupplierDTO> suppliersDTO) {
        for (SupplierDTO supplierDTO: suppliersDTO) {
            supplierRepo.findById(supplierDTO.getId()).map(
                    s -> {
                        s.setName(supplierDTO.getName());
                        s.setEmail(supplierDTO.getEmail());
                        s.setPhone(supplierDTO.getPhone());
                        s.setPhone(supplierDTO.getPhone());

                        return supplierRepo.save(s);
                    }
            ).orElseGet(() -> {
                AppUser appUser = userRepo.findByUsername(supplierDTO.getAuthorUsername())
                        .orElseThrow(() -> new AppException("User not found: " + supplierDTO.getAuthorUsername(), HttpStatus.NOT_FOUND));

                Supplier supplier = new Supplier(supplierDTO.getId(), supplierDTO.getName(), supplierDTO.getEmail(),
                        supplierDTO.getPhone(), new ArrayList<>(), appUser);
                supplier.setId(supplierDTO.getId());
                return supplierRepo.save(supplier);
            });
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> deleteSupplier(Long id) {
        supplierRepo.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    public boolean isSupplierAuthor(Long id, Authentication authentication) {
        UserDto user = (UserDto) authentication.getPrincipal();
        Supplier supplier = supplierRepo.findById(id)
                .orElseThrow(() -> new AppException("Supplier not found", HttpStatus.NOT_FOUND));

        return supplier.getAuthor().getUsername().equals(user.getUsername());
    }

    public boolean isSuppliersAuthor(List<SupplierDTO> suppliers, Authentication authentication) {
        for (SupplierDTO d: suppliers) {
            if (!isSupplierAuthor(d.getId(), authentication)) {
                return false;
            }
        }

        return true;
    }

    public ResponseEntity<?> deleteAll() {
        try {
            mySupplierRepo.deleteAll();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }

        return ResponseEntity.ok().build();
    }
}
