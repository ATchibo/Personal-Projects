package com.example.assignment1_1.service;

import com.example.assignment1_1.components.DealershipModelAssembler;
import com.example.assignment1_1.domain.AppUser;
import com.example.assignment1_1.domain.Dealership;
import com.example.assignment1_1.domain.ShippingContract;
import com.example.assignment1_1.domain.Supplier;
import com.example.assignment1_1.dtos.DealershipByAvgCarPrice;
import com.example.assignment1_1.dtos.DealershipDTO;
import com.example.assignment1_1.dtos.UserDto;
import com.example.assignment1_1.exceptions.AppException;
import com.example.assignment1_1.exceptions.DelershipNotFoundException;
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
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@Service
public class DealershipService {
    @Autowired
    private DealershipRepo dealershipRepo;

    @Autowired
    private MyDealershipRepo myDealershipRepo;

    @Autowired
    private SupplierRepo supplierRepo;

    @Autowired
    private ShippingContractRepo shippingContractRepo;

    @Autowired
    private PageSizeRepo pageSizeRepo;

    @Autowired
    private DealershipModelAssembler dealershipModelAssembler;

    public ResponseEntity<List<DealershipDTO>> findAllDealerships(int page, int size){
        if (!pageSizeRepo.findAll().isEmpty()) {
            size = pageSizeRepo.findAll().get(0).getPageSize().intValue();
        }

        Pageable paging = PageRequest.of(page, size);
        Page<DealershipDTO> pagedResult = myDealershipRepo.findAllCountCars(paging);

        return new ResponseEntity<>(pagedResult.getContent(), HttpStatus.OK);
    }

    public List<Dealership> findDealershipByParams(Long id, String name, String address, String phone, String email, String website, int page, int size) {
        return myDealershipRepo.findAll(id, name, address, phone, email, website, PageRequest.of(page, size)).toList();
    }

    public EntityModel<Dealership> findDealershipById(Long id) {
        Dealership dealership = dealershipRepo.findById(id).orElseThrow(() -> new DelershipNotFoundException(id));

        return dealershipModelAssembler.toModel(dealership);
    }

    public ResponseEntity<List<DealershipDTO>> findDealershipsByName(String name, int page, int size) {
        if (!pageSizeRepo.findAll().isEmpty()) {
            size = pageSizeRepo.findAll().get(0).getPageSize().intValue();
        }

        Pageable paging = PageRequest.of(page, size);
        Page<Dealership> pagedResult = myDealershipRepo.findAllByName(name, paging);

        if (pagedResult.hasContent()) {
            return new ResponseEntity<>(pagedResult.getContent().stream()
                    .map(d -> new DealershipDTO(d.getId(), d.getName(), d.getAddress(), d.getPhone(), d.getEmail(),
                            d.getWebsite(), (long) d.getCars().size(), d.getAuthor().getId(), d.getAuthor().getUsername()))
                    .toList()
                    , HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<List<DealershipByAvgCarPrice>> findDealershipsByAvgCarPrice(int page, int size) {
        if (!pageSizeRepo.findAll().isEmpty()) {
            size = pageSizeRepo.findAll().get(0).getPageSize().intValue();
        }

        Pageable paging = PageRequest.of(page, size);
        Page<DealershipByAvgCarPrice> pagedResult = myDealershipRepo.findDealershipsByAvgCarPrice(paging);

        return new ResponseEntity<>(pagedResult.getContent(), HttpStatus.OK);
    }


    public ResponseEntity<?> saveDealership(@RequestBody Dealership dealership) {
        EntityModel<Dealership> entityModel = dealershipModelAssembler.toModel(dealershipRepo.save(dealership));

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    public EntityModel<Dealership> addDealershipToSupplier(@PathVariable Long id, @RequestBody List<Supplier> suppliers) {
        Dealership dealership = dealershipRepo.findById(id).orElseThrow(() -> new DelershipNotFoundException(id));

        for (Supplier supplier: suppliers) {
            ShippingContract shippingContract = new ShippingContract(new Date(), 1);

            shippingContractRepo.save(shippingContract);

            dealership.getShippingContracts().add(shippingContract);
            dealershipRepo.save(dealership);
            supplier.getShippingContracts().add(shippingContract);
            supplierRepo.save(supplier);
        }

        return dealershipModelAssembler.toModel(dealership);
    }

    public ResponseEntity<?> updateDealership(@PathVariable Long id, @RequestBody Dealership newDealership) {
        Dealership updatedDealership = dealershipRepo.findById(id)
                .map(dealership -> {
                    dealership.setName(newDealership.getName());
                    dealership.setAddress(newDealership.getAddress());
                    dealership.setPhone(newDealership.getPhone());
                    dealership.setEmail(newDealership.getEmail());
                    dealership.setWebsite(newDealership.getWebsite());

                    return dealershipRepo.save(dealership);
                })
                .orElseGet(() -> {
                    newDealership.setId(id);
                    return dealershipRepo.save(newDealership);
                });

        EntityModel<Dealership> entityModel = dealershipModelAssembler.toModel(updatedDealership);

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    public ResponseEntity<?> updateDealerships(@RequestBody List<Dealership> newDealerships) {
        for (Dealership newDealership: newDealerships) {
            Dealership updatedDealership = dealershipRepo.findById(newDealership.getId())
                    .map(dealership -> {
                        dealership.setName(newDealership.getName());
                        dealership.setAddress(newDealership.getAddress());
                        dealership.setPhone(newDealership.getPhone());
                        dealership.setEmail(newDealership.getEmail());
                        dealership.setWebsite(newDealership.getWebsite());

                        return dealershipRepo.save(dealership);
                    })
                    .orElseGet(() -> {
                        newDealership.setId(newDealership.getId());
                        return dealershipRepo.save(newDealership);
                    });
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> deleteDealership(@PathVariable Long id) {
        dealershipRepo.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    public boolean isDealershipAuthor(Long id, Authentication authentication) {
        UserDto user = (UserDto) authentication.getPrincipal();
        Dealership dealership = dealershipRepo.findById(id)
                .orElseThrow(() -> new AppException("Dealership not found", HttpStatus.NOT_FOUND));

        return dealership.getAuthor().getUsername().equals(user.getUsername());
    }

    public boolean isDealershipsAuthor(List<Dealership> dealerships, Authentication authentication) {
        for (Dealership d: dealerships) {
            if (!isDealershipAuthor(d.getId(), authentication)) {
                return false;
            }
        }

        return true;
    }

    public ResponseEntity<?> deleteAll() {
        try {
            myDealershipRepo.deleteAll();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }

        return ResponseEntity.ok().build();
    }
}
