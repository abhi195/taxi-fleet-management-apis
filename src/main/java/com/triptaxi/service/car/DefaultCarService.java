package com.triptaxi.service.car;

import com.google.common.base.Preconditions;
import com.triptaxi.dataaccessobject.CarRepository;
import com.triptaxi.domainobject.CarDO;
import com.triptaxi.domainobject.ManufacturerDO;
import com.triptaxi.domainvalue.EngineType;
import com.triptaxi.exception.ConstraintsViolationException;
import com.triptaxi.exception.EntityNotFoundException;
import com.triptaxi.service.manufacturer.ManufacturerService;
import java.util.List;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to encapsulate the link between DAO and controller and to have business logic for some
 * car specific things.
 */
@Service
public class DefaultCarService implements CarService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultCarService.class);

    private final CarRepository carRepository;
    private final ManufacturerService manufacturerService;

    public DefaultCarService(final CarRepository carRepository,
        final ManufacturerService manufacturerService) {
        this.carRepository = carRepository;
        this.manufacturerService = manufacturerService;
    }

    /**
     * Find all cars except deleted.
     *
     * @return all un-deleted cars
     */
    @Override
    public List<CarDO> findAll() {
        return carRepository.findByDeletedFalse();
    }

    /**
     * Selects a cars by EngineType.
     *
     * @param engineType
     * @return found cars
     */
    @Override
    public List<CarDO> find(EngineType engineType) {
        return carRepository.findByEngineType(engineType);
    }

    /**
     * Selects a car by id.
     *
     * @param carId
     * @return found car
     * @throws EntityNotFoundException if no car with the given id was found.
     */
    @Override
    public CarDO find(Long carId) throws EntityNotFoundException {
        return findCarChecked(carId);
    }

    /**
     * Creates a new car.
     *
     * @param carDO
     * @param manufacturerId
     * @return created car
     * @throws EntityNotFoundException       if mentioned manufacturerId doesn't exists
     * @throws ConstraintsViolationException if a car already exists with the given license_plate or
     *                                       rating is not in defined range
     */
    @Override
    public CarDO create(CarDO carDO, Long manufacturerId)
        throws EntityNotFoundException, ConstraintsViolationException {
        CarDO car;
        try {
            ManufacturerDO manufacturer = manufacturerService.find(manufacturerId);
            carDO.setManufacturer(manufacturer);
            car = carRepository.save(carDO);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            LOG.warn("ConstraintsViolationException while creating a car: {}", carDO, e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        return car;
    }

    /**
     * Deletes an existing car by id.
     *
     * @param carId
     * @throws EntityNotFoundException if no car with the given id was found.
     */
    @Override
    @Transactional
    public void delete(Long carId) throws EntityNotFoundException {
        CarDO carDO = findCarChecked(carId);
        carDO.setDeleted(true);
    }

    /**
     * Updates rating of an existing car by id.
     *
     * @param carId
     * @param rating
     * @throws EntityNotFoundException       if no car with the given id was found.
     * @throws ConstraintsViolationException if rating is not in defined range
     */
    @Override
    @Transactional
    public void updateRating(Long carId, Float rating)
        throws EntityNotFoundException, ConstraintsViolationException {
        try {
            Preconditions.checkArgument(rating >= 0.0 && rating <= 5.0,
                " rating should be between [0.0, 5.0]");
            CarDO carDO = findCarChecked(carId);
            carDO.setRating(rating);
        } catch (IllegalArgumentException e) {
            LOG.warn("ConstraintsViolationException while updating rating for carId: {}", carId, e);
            throw new ConstraintsViolationException(e.getMessage());
        }
    }

    private CarDO findCarChecked(Long carId) throws EntityNotFoundException {
        return carRepository.findById(carId)
            .orElseThrow(
                () -> new EntityNotFoundException("Could not find entity with id: " + carId));
    }
}
