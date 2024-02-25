package com.example.assignment1_1.controller;

import com.example.assignment1_1.domain.Dealership;
import com.example.assignment1_1.domain.Supplier;
import com.example.assignment1_1.dtos.DealershipDTO;
import com.example.assignment1_1.dtos.SupplierByNrShippings;
import com.example.assignment1_1.dtos.SupplierDTO;
import com.example.assignment1_1.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping(path = "/")
    public ResponseEntity<List<SupplierDTO>> findAllSuppliers(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                                   @RequestParam(value = "size", defaultValue = "25", required = false) int size) {
        return supplierService.findAllSuppliers(page, size);
    }

    @GetMapping(path = "/{id}")
    public EntityModel<Supplier> findSupplierById(@PathVariable Long id) {
        return supplierService.findSupplierById(id);
    }

    @GetMapping(path = "/by-nr-contracts")
    public List<SupplierByNrShippings> findAllSuppliersByNrShippings(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                                     @RequestParam(value = "size", defaultValue = "25", required = false) int size) {
        return supplierService.findAllSuppliersByNrShippings(page, size);
    }

    @GetMapping(path = "/autocomplete", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<SupplierDTO>> findSuppliersByName(@RequestParam(value = "name") String name) {
        return supplierService.findSuppliersByName(name, 0, 20);
    }

    @PostMapping()
    public ResponseEntity<?> saveSupplier(@RequestBody Supplier supplier) {
        return supplierService.saveSupplier(supplier);
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or" +
            " hasRole('ROLE_MODERATOR') or" +
            " (hasRole('ROLE_REGULAR') and @supplierService.isSupplierAuthor(#id, authentication))")
    public ResponseEntity<?> updateSupplier(@RequestBody Supplier supplier, @PathVariable Long id) {
        return supplierService.updateSupplier(supplier, id);
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or" +
            " hasRole('ROLE_MODERATOR') or" +
            " (hasRole('ROLE_REGULAR') and @supplierService.isSuppliersAuthor(#suppliersDTO, authentication))")
    public ResponseEntity<?> updateSuppliers(@RequestBody List<SupplierDTO> suppliersDTO) {
        return supplierService.updateSuppliersDTO(suppliersDTO);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or" +
            " hasRole('ROLE_MODERATOR') or" +
            " (hasRole('ROLE_REGULAR') and @supplierService.isSupplierAuthor(#id, authentication))")
    public ResponseEntity<?> deleteSupplier(@PathVariable Long id) {
        return supplierService.deleteSupplier(id);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAll() {
        return supplierService.deleteAll();
    }
}
