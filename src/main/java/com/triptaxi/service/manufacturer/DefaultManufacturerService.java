package com.triptaxi.service.manufacturer;

import com.google.common.collect.ImmutableList;
import com.triptaxi.dataaccessobject.ManufacturerRepository;
import com.triptaxi.domainobject.ManufacturerDO;
import com.triptaxi.exception.ConstraintsViolationException;
import com.triptaxi.exception.EntityNotFoundException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to encapsulate the link between DAO and controller and to have business logic for some
 * manufacturer specific things.
 */
@Service
public class DefaultManufacturerService implements ManufacturerService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultManufacturerService.class);

    private final ManufacturerRepository manufacturerRepository;

    public DefaultManufacturerService(final ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    /**
     * Find all manufacturers.
     *
     * @return all manufacturers
     */
    @Override
    public List<ManufacturerDO> findAll() {
        return ImmutableList.copyOf(manufacturerRepository.findByDeletedFalse());
    }

    /**
     * Selects a manufacturer by id.
     *
     * @param manufacturerId
     * @return found manufacturer
     * @throws EntityNotFoundException if no manufacturer with the given id was found.
     */
    @Override
    public ManufacturerDO find(Long manufacturerId) throws EntityNotFoundException {
        return findManufacturerChecked(manufacturerId);
    }

    /**
     * Creates a new manufacturer.
     *
     * @param manufacturerDO
     * @return created manufacturer
     * @throws ConstraintsViolationException if a manufacturer already exists with the given
     *                                       manufacturer_name
     */
    @Override
    public ManufacturerDO create(ManufacturerDO manufacturerDO)
        throws ConstraintsViolationException {
        ManufacturerDO manufacturer;
        try {
            manufacturer = manufacturerRepository.save(manufacturerDO);
        } catch (DataIntegrityViolationException e) {
            LOG.warn("ConstraintsViolationException while creating a manufacturer: {}",
                manufacturerDO, e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        return manufacturer;
    }

    /**
     * Deletes an existing manufacturer by id.
     *
     * @param manufacturerId
     * @throws EntityNotFoundException if no manufacturer with the given id was found.
     */
    @Override
    @Transactional
    public void delete(Long manufacturerId) throws EntityNotFoundException {
        ManufacturerDO manufacturerDO = findManufacturerChecked(manufacturerId);
        manufacturerDO.setDeleted(true);
    }

    /**
     * Updates manufacturer_name of an existing manufacturer by id.
     *
     * @param manufacturerId
     * @param manufacturerName
     * @throws EntityNotFoundException       if no manufacturer with the given id was found.
     * @throws ConstraintsViolationException if a manufacturer already exists with the given
     *                                       manufacturer_name
     */
    @Override
    public void updateManufacturerName(Long manufacturerId, String manufacturerName)
        throws EntityNotFoundException, ConstraintsViolationException {
        try {
            ManufacturerDO manufacturerDO = findManufacturerChecked(manufacturerId);
            manufacturerDO.setManufacturerName(manufacturerName);
            manufacturerRepository.save(manufacturerDO);
        } catch (DataIntegrityViolationException e) {
            LOG.warn(
                "ConstraintsViolationException while updating a manufacturerId: {} with manufacturerName: {}",
                manufacturerId, manufacturerName, e);
            throw new ConstraintsViolationException(e.getMessage());
        }
    }

    private ManufacturerDO findManufacturerChecked(Long manufacturerId)
        throws EntityNotFoundException {
        return manufacturerRepository.findById(manufacturerId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Could not find entity with id: " + manufacturerId));
    }
}
