package com.example.assignment1_1.service;

import com.example.assignment1_1.components.CarModelAssembler;
import com.example.assignment1_1.controller.CarController;
import com.example.assignment1_1.domain.AppUser;
import com.example.assignment1_1.domain.Car;
import com.example.assignment1_1.domain.Dealership;
import com.example.assignment1_1.dtos.CarDTO;
import com.example.assignment1_1.dtos.DealershipDTO;
import com.example.assignment1_1.dtos.UserDto;
import com.example.assignment1_1.exceptions.AppException;
import com.example.assignment1_1.exceptions.CarNotFoundException;
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

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    @Autowired
    private CarRepo carRepo;

    @Autowired
    private MyCarRepo myCarRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DealershipRepo dealershipRepo;

    @Autowired
    private PageSizeRepo pageSizeRepo;

    @Autowired
    private CarModelAssembler carModelAssembler;

    public ResponseEntity<List<CarDTO>> getAllCars(int page, int size){
        if (!pageSizeRepo.findAll().isEmpty()) {
            size = pageSizeRepo.findAll().get(0).getPageSize().intValue();
        }

        Pageable paging = PageRequest.of(page, size);
        Page<CarDTO> pagedResult = myCarRepo.findAllBy(paging);

        return new ResponseEntity<>(pagedResult.getContent(), HttpStatus.OK);
    }

    public EntityModel<Car> getCarById(Long id) {
        Car car = carRepo.findById(id).orElseThrow(() -> new CarNotFoundException(id));

        return carModelAssembler.toModel(car);
    }

    public ResponseEntity<List<CarDTO>> getCarsByMinPrice(int page, int size, Integer minPrice) {
        if (!pageSizeRepo.findAll().isEmpty()) {
            size = pageSizeRepo.findAll().get(0).getPageSize().intValue();
        }

        Pageable paging = PageRequest.of(page, size);
        Page<CarDTO> pagedResult = myCarRepo.findByPriceGreaterThan(minPrice, paging);

        return new ResponseEntity<>(pagedResult.getContent(), HttpStatus.OK);
    }

    public ResponseEntity<?> saveCar(Car car) {
        EntityModel<Car> entityModel = carModelAssembler.toModel(carRepo.save(car));

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    public ResponseEntity<?> saveCarDto(CarDTO carDTO) {
        Dealership dealership = dealershipRepo.findById(carDTO.getDealershipId())
                .orElseThrow(() -> new DelershipNotFoundException(carDTO.getDealershipId()));
        AppUser appUser = userRepo.findById(carDTO.getAuthorId())
                .orElseThrow(() -> new AppException("User not found: " + carDTO.getAuthorUsername(), HttpStatus.NOT_FOUND));
        Car car = new Car(
                carDTO.getId(),
                carDTO.getBrand(),
                carDTO.getModel(),
                carDTO.getYear(),
                carDTO.getColor(),
                carDTO.getPrice(),
                carDTO.getDescription(),
                dealership,
                appUser
        );

        EntityModel<Car> entityModel = carModelAssembler.toModel(carRepo.save(car));

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    public ResponseEntity<?> updateCar(@PathVariable Long id,
                                       @RequestBody Car car) {

        Car carToUpdate = carRepo.findById(id)
                .map(car1 -> {
                    car1.setBrand(car.getBrand());
                    car1.setModel(car.getModel());
                    car1.setYear(car.getYear());
                    car1.setColor(car.getColor());
                    car1.setPrice(car.getPrice());

                    return carRepo.save(car1);
                }).orElseGet(() -> {
                    car.setId(id);
                    return carRepo.save(car);
                });

        EntityModel<Car> entityModel = carModelAssembler.toModel(carRepo.save(carToUpdate));

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    public ResponseEntity<?> updateCarDtos(List<CarDTO> carDTOs) {
        for (CarDTO carDTO: carDTOs) {
            carRepo.findById(carDTO.getId())
                    .map(car1 -> {
                        car1.setBrand(carDTO.getBrand());
                        car1.setModel(carDTO.getModel());
                        car1.setYear(carDTO.getYear());
                        car1.setColor(carDTO.getColor());
                        car1.setPrice(carDTO.getPrice());
                        car1.setDealership(dealershipRepo.findById(carDTO.getDealershipId())
                                .orElseThrow(() -> new DelershipNotFoundException(carDTO.getDealershipId())));

                        return carRepo.save(car1);
                    }).orElseGet(() -> {
                        Car car = new Car(
                                carDTO.getBrand(),
                                carDTO.getModel(),
                                carDTO.getYear(),
                                carDTO.getColor(),
                                carDTO.getPrice(),
                                carDTO.getDescription(),
                                dealershipRepo.findById(carDTO.getDealershipId())
                                        .orElseThrow(() -> new DelershipNotFoundException(carDTO.getDealershipId())),
                                userRepo.findByUsername(carDTO.getAuthorUsername())
                                        .orElseThrow(() -> new AppException("User not found: " + carDTO.getAuthorUsername(), HttpStatus.NOT_FOUND))
                        );

                        return carRepo.save(car);
                    });
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteCar(@PathVariable Long id) {
        carRepo.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    public boolean isCarAuthor(Long id, Authentication authentication) {
        UserDto user = (UserDto) authentication.getPrincipal();
        Optional<Car> car = carRepo.findById(id);

        return car.map(value -> value.getAuthor().getUsername().equals(user.getUsername())).orElse(true);
    }

    public boolean isCarsAuthor(List<CarDTO> cars, Authentication authentication) {
        for (CarDTO d: cars) {
            if (!isCarAuthor(d.getId(), authentication)) {
                return false;
            }
        }

        return true;
    }

    public ResponseEntity<?> deleteAll() {
        try {
            myCarRepo.deleteAll();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }

        return ResponseEntity.ok().build();
    }
}
