package com.triptaxi.controller;

import com.triptaxi.controller.mapper.DriverMapper;
import com.triptaxi.datatransferobject.DriverDTO;
import com.triptaxi.domainobject.DriverDO;
import com.triptaxi.domainvalue.OnlineStatus;
import com.triptaxi.exception.CarAlreadyInUseException;
import com.triptaxi.exception.ConstraintsViolationException;
import com.triptaxi.exception.EntityNotFoundException;
import com.triptaxi.service.driver.DriverService;
import java.util.List;
import javax.validation.Valid;
import net.kaczmarzyk.spring.data.jpa.domain.EqualIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.domain.GreaterThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.LessThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.domain.NotNull;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
 * All operations with a driver will be routed by this controller.
 */
@RestController
@RequestMapping("v1/drivers")
public class DriverController {

    private final DriverService driverService;

    @Autowired
    public DriverController(final DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping("/{driverId}")
    public DriverDTO getDriver(@PathVariable long driverId) throws EntityNotFoundException {
        return DriverMapper.makeDriverDTO(driverService.find(driverId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverDTO createDriver(@Valid @RequestBody DriverDTO driverDTO)
        throws ConstraintsViolationException {
        DriverDO driverDO = DriverMapper.makeDriverDO(driverDTO);
        return DriverMapper.makeDriverDTO(driverService.create(driverDO));
    }

    @DeleteMapping("/{driverId}")
    public void deleteDriver(@PathVariable long driverId) throws EntityNotFoundException {
        driverService.delete(driverId);
    }

    @PutMapping("/{driverId}")
    public void updateLocation(@PathVariable long driverId, @RequestParam double longitude,
        @RequestParam double latitude)
        throws EntityNotFoundException, ConstraintsViolationException {
        driverService.updateLocation(driverId, longitude, latitude);
    }

    @GetMapping
    public List<DriverDTO> findDrivers(@RequestParam OnlineStatus onlineStatus) {
        return DriverMapper.makeDriverDTOList(driverService.find(onlineStatus));
    }

    @PutMapping("/{driverId}/selectCar")
    public void selectCar(@PathVariable long driverId, @RequestParam Long carId)
        throws EntityNotFoundException, CarAlreadyInUseException, ConstraintsViolationException {
        driverService.selectCar(driverId, carId);
    }

    @PutMapping("/{driverId}/deselectCar")
    public void deselectCar(@PathVariable long driverId) throws EntityNotFoundException {
        driverService.deselectCar(driverId);
    }

    @GetMapping(value = "/search")
    public List<DriverDTO> search(
        @And({
            @Spec(path = "username", params = "username", spec = LikeIgnoreCase.class),
            @Spec(path = "onlineStatus", params = "onlineStatus", spec = EqualIgnoreCase.class),
            @Spec(path = "deleted", params = "deleted", spec = EqualIgnoreCase.class),
            @Spec(path = "car", params = "carAssigned", spec = NotNull.class),
            @Spec(path = "car.licensePlate", params = "licensePlate", spec = LikeIgnoreCase.class),
            @Spec(path = "car.rating", params = "ratingAvailable", spec = NotNull.class),
            @Spec(path = "car.rating", params = "ratingGreaterThan", spec = GreaterThanOrEqual.class),
            @Spec(path = "car.rating", params = "ratingLessThan", spec = LessThanOrEqual.class),
            @Spec(path = "car.convertible", params = "convertible", spec = EqualIgnoreCase.class),
            @Spec(path = "car.manufacturer.manufacturerName", params = "manufacturer", spec = LikeIgnoreCase.class),
            @Spec(path = "car.engineType", params = "engineType", spec = EqualIgnoreCase.class)
        }) Specification<DriverDO> spec, Sort sort) {
        return DriverMapper.makeDriverDTOList(driverService.search(spec, sort));
    }
}