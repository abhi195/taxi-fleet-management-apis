package com.triptaxi.controller;

import com.triptaxi.controller.mapper.CarMapper;
import com.triptaxi.datatransferobject.CarDTO;
import com.triptaxi.domainobject.CarDO;
import com.triptaxi.domainvalue.EngineType;
import com.triptaxi.exception.ConstraintsViolationException;
import com.triptaxi.exception.EntityNotFoundException;
import com.triptaxi.service.car.CarService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * All operations with a car will be routed by this controller.
 */
@RestController
@RequestMapping("v1/cars")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(final CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public List<CarDTO> getCars() {
        return CarMapper.makeCarDTOList(carService.findAll());
    }

    @GetMapping("/engine")
    public List<CarDTO> findCars(@RequestParam EngineType engineType) {
        return CarMapper.makeCarDTOList(carService.find(engineType));
    }

    @GetMapping("/{carId}")
    public CarDTO getCar(@PathVariable long carId) throws EntityNotFoundException {
        return CarMapper.makeCarDTO(carService.find(carId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarDTO createCar(@Valid @RequestBody CarDTO carDTO)
        throws EntityNotFoundException, ConstraintsViolationException {

        CarDO carDO = CarMapper.makeCarDO(carDTO);
        return CarMapper.makeCarDTO(carService.create(carDO, carDTO.getManufacturerId()));
    }

    @DeleteMapping("/{carId}")
    public void deleteCar(@PathVariable long carId) throws EntityNotFoundException {
        carService.delete(carId);
    }

    @PutMapping("/{carId}")
    public void updateRating(@PathVariable long carId, @RequestParam Float rating)
        throws EntityNotFoundException, ConstraintsViolationException {
        carService.updateRating(carId, rating);
    }
}