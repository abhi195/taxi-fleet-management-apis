package com.triptaxi.service.car;

import com.triptaxi.domainobject.CarDO;
import com.triptaxi.domainvalue.EngineType;
import com.triptaxi.exception.ConstraintsViolationException;
import com.triptaxi.exception.EntityNotFoundException;
import java.util.List;

public interface CarService {

    List<CarDO> findAll();

    List<CarDO> find(EngineType engineType);

    CarDO find(Long carId) throws EntityNotFoundException;

    CarDO create(CarDO carDO, Long manufacturerId)
        throws EntityNotFoundException, ConstraintsViolationException;

    void delete(Long carId) throws EntityNotFoundException;

    void updateRating(Long carId, Float rating)
        throws EntityNotFoundException, ConstraintsViolationException;
}
