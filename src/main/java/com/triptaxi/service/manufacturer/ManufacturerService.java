package com.triptaxi.service.manufacturer;

import com.triptaxi.domainobject.ManufacturerDO;
import com.triptaxi.exception.ConstraintsViolationException;
import com.triptaxi.exception.EntityNotFoundException;
import java.util.List;

public interface ManufacturerService {

    List<ManufacturerDO> findAll();

    ManufacturerDO find(Long manufacturerId) throws EntityNotFoundException;

    ManufacturerDO create(ManufacturerDO manufacturerDO) throws ConstraintsViolationException;

    void delete(Long manufacturerId) throws EntityNotFoundException;

    void updateManufacturerName(Long manufacturerId, String manufacturerName)
        throws EntityNotFoundException, ConstraintsViolationException;
}
