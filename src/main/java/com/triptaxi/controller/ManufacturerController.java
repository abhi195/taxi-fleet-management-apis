package com.triptaxi.controller;

import com.triptaxi.controller.mapper.ManufacturerMapper;
import com.triptaxi.datatransferobject.ManufacturerDTO;
import com.triptaxi.domainobject.ManufacturerDO;
import com.triptaxi.exception.ConstraintsViolationException;
import com.triptaxi.exception.EntityNotFoundException;
import com.triptaxi.service.manufacturer.ManufacturerService;
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
 * All operations with a manufacturer will be routed by this controller.
 */
@RestController
@RequestMapping("v1/manufacturers")
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    @Autowired
    public ManufacturerController(final ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @GetMapping
    public List<ManufacturerDTO> getManufacturers() {
        return ManufacturerMapper.makeDriverDTOList(manufacturerService.findAll());
    }

    @GetMapping("/{manufacturerId}")
    public ManufacturerDTO getManufacturer(@PathVariable long manufacturerId)
        throws EntityNotFoundException {
        return ManufacturerMapper.makeManufacturerDTO(manufacturerService.find(manufacturerId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ManufacturerDTO createManufacturer(@Valid @RequestBody ManufacturerDTO manufacturerDTO)
        throws ConstraintsViolationException {
        ManufacturerDO manufacturerDO = ManufacturerMapper.makeManufacturerDO(manufacturerDTO);
        return ManufacturerMapper.makeManufacturerDTO(manufacturerService.create(manufacturerDO));
    }

    @DeleteMapping("/{manufacturerId}")
    public void deleteManufacturer(@PathVariable long manufacturerId)
        throws EntityNotFoundException {
        manufacturerService.delete(manufacturerId);
    }

    @PutMapping("/{manufacturerId}")
    public void updateName(@PathVariable long manufacturerId, @RequestParam String manufacturerName)
        throws EntityNotFoundException, ConstraintsViolationException {
        manufacturerService.updateManufacturerName(manufacturerId, manufacturerName);
    }
}