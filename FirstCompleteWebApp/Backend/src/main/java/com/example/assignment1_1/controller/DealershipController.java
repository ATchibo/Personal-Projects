package com.example.assignment1_1.controller;


import com.example.assignment1_1.domain.Dealership;
import com.example.assignment1_1.domain.Supplier;
import com.example.assignment1_1.dtos.DealershipDTO;
import com.example.assignment1_1.dtos.DealershipByAvgCarPrice;
import com.example.assignment1_1.service.DealershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/dealerships")
@CrossOrigin(origins = "http://localhost:3000")
public class DealershipController {

    @Autowired
    private DealershipService dealershipService;

    @GetMapping(path = "/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<DealershipDTO>> findAllDealerships(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                               @RequestParam(value = "size", defaultValue = "25", required = false) int size) {
        return dealershipService.findAllDealerships(page, size);
    }

    @GetMapping(path = "/id={id}&name={name}&address={address}&phone={phone}&email={email}&website={website}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Dealership> findDealershipByParams(@PathVariable Long id, @PathVariable String name, @PathVariable String address,
                                                   @PathVariable String phone, @PathVariable String email, @PathVariable String website,
                                                   @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                   @RequestParam(value = "size", defaultValue = "25", required = false) int size) {
        return dealershipService.findDealershipByParams(id, name, address, phone, email, website, page, size);
    }

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public EntityModel<Dealership> findDealershipById(@PathVariable Long id) {
        return dealershipService.findDealershipById(id);
    }

    @GetMapping(path = "/autocomplete", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<DealershipDTO>> findDealershipsByName(@RequestParam(value = "name") String name) {
        return dealershipService.findDealershipsByName(name, 0, 20);
    }

    @GetMapping(path = "/sort-by-avg-car-price")
    public ResponseEntity<List<DealershipByAvgCarPrice>> findDealershipByAvgCarPrice(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                                                     @RequestParam(value = "size", defaultValue = "25", required = false) int size) {
        return dealershipService.findDealershipsByAvgCarPrice(page, size);
    }

    @PostMapping()
    public ResponseEntity<?> saveDealership(@RequestBody Dealership dealership) {
        return dealershipService.saveDealership(dealership);
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or" +
            " hasRole('ROLE_MODERATOR') or" +
            " (hasRole('ROLE_REGULAR') and @dealershipService.isDealershipAuthor(#id, authentication))")
    public ResponseEntity<?> updateDealership(@PathVariable Long id, @RequestBody Dealership newDealership) {
        return dealershipService.updateDealership(id, newDealership);
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or" +
            " hasRole('ROLE_MODERATOR') or" +
            " (hasRole('ROLE_REGULAR') and @dealershipService.isDealershipsAuthor(#newDealerships, authentication))")
    public ResponseEntity<?> updateDealerships(@RequestBody List<Dealership> newDealerships) {
        return dealershipService.updateDealerships(newDealerships);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or" +
            " hasRole('ROLE_MODERATOR') or" +
            " (hasRole('ROLE_REGULAR') and @dealershipService.isDealershipAuthor(#id, authentication))")
    public ResponseEntity<?> deleteDealership(@PathVariable Long id) {
        return dealershipService.deleteDealership(id);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAll() {
        return dealershipService.deleteAll();
    }
}
