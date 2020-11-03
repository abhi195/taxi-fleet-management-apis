package com.triptaxi.service.driver;

import com.triptaxi.domainobject.DriverDO;
import com.triptaxi.domainvalue.OnlineStatus;
import com.triptaxi.exception.CarAlreadyInUseException;
import com.triptaxi.exception.ConstraintsViolationException;
import com.triptaxi.exception.EntityNotFoundException;
import java.util.List;

public interface DriverService {

    DriverDO find(Long driverId) throws EntityNotFoundException;

    DriverDO create(DriverDO driverDO) throws ConstraintsViolationException;

    void delete(Long driverId) throws EntityNotFoundException;

    void updateLocation(long driverId, double longitude, double latitude)
        throws EntityNotFoundException, ConstraintsViolationException;

    List<DriverDO> find(OnlineStatus onlineStatus);

    void selectCar(Long driverId, Long carId)
        throws EntityNotFoundException, CarAlreadyInUseException, ConstraintsViolationException;

    void deselectCar(Long driverId) throws EntityNotFoundException;
}
