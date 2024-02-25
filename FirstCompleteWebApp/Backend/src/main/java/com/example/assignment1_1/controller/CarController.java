package com.example.assignment1_1.controller;

import com.example.assignment1_1.components.CarModelAssembler;
import com.example.assignment1_1.domain.Car;
import com.example.assignment1_1.dtos.CarDTO;
import com.example.assignment1_1.exceptions.CarNotFoundException;
import com.example.assignment1_1.repo.CarRepo;
import com.example.assignment1_1.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/api/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping(path = "/")
    public ResponseEntity<List<CarDTO>> getAllCars(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                   @RequestParam(value = "size", defaultValue = "25", required = false) int size)  {
        return carService.getAllCars(page, size);
    }

    @GetMapping(path = "/{id}")
    public EntityModel<Car> getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }

    @GetMapping(path = "minPrice/{minPrice}")
    public ResponseEntity<List<CarDTO>> getCarsByMinPrice(@PathVariable Integer minPrice,
                                                               @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                               @RequestParam(value = "size", defaultValue = "25", required = false) int size) {
        return carService.getCarsByMinPrice(page, size, minPrice);
    }

    @PostMapping()
    public ResponseEntity<?> saveCarDto(@RequestBody CarDTO carDTO) {
        return carService.saveCarDto(carDTO);
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or" +
            " hasRole('ROLE_MODERATOR') or" +
            " (hasRole('ROLE_REGULAR') and @carService.isCarAuthor(#id, authentication))")
    public ResponseEntity<?> updateCar(@PathVariable Long id, @RequestBody Car car) {
        return carService.updateCar(id, car);
    }

    @PutMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN') or" +
            " hasRole('ROLE_MODERATOR') or" +
            " (hasRole('ROLE_REGULAR') and @carService.isCarsAuthor(#carDtos, authentication))")
    public ResponseEntity<?> updateCarDtos(@RequestBody List<CarDTO> carDtos) {
        return carService.updateCarDtos(carDtos);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or" +
            " hasRole('ROLE_MODERATOR') or" +
            " (hasRole('ROLE_REGULAR') and @carService.isCarAuthor(#id, authentication))")
    public ResponseEntity<?> deleteCar(@PathVariable Long id) {
        return carService.deleteCar(id);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAll() {
        return carService.deleteAll();
    }
}
