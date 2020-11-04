package com.triptaxi.service.manufacturer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.triptaxi.dataaccessobject.ManufacturerRepository;
import com.triptaxi.domainobject.ManufacturerDO;
import com.triptaxi.exception.ConstraintsViolationException;
import com.triptaxi.exception.EntityNotFoundException;
import java.util.Arrays;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;

@RunWith(MockitoJUnitRunner.class)
public class DefaultManufacturerServiceTest {

    private ManufacturerService manufacturerService;
    @Mock
    ManufacturerRepository manufacturerRepository;

    @Before
    public void setUp() throws Exception {
        this.manufacturerService = new DefaultManufacturerService(manufacturerRepository);
    }

    @Test
    public void findAll() {
        ManufacturerDO expectedManufacturer1 = new ManufacturerDO("Tesla");
        ManufacturerDO expectedManufacturer2 = new ManufacturerDO("BMW");

        ManufacturerDO actualManufacturer1 = new ManufacturerDO("Tesla");
        ManufacturerDO actualManufacturer2 = new ManufacturerDO("BMW");

        Mockito.when(manufacturerRepository.findByDeletedFalse())
            .thenReturn(Arrays.asList(actualManufacturer1, actualManufacturer2));
        assertThat(manufacturerService.findAll())
            .isEqualTo(Arrays.asList(expectedManufacturer1, expectedManufacturer2));
    }

    @Test
    public void find_byId_ok() throws EntityNotFoundException {
        ManufacturerDO expectedManufacturer = new ManufacturerDO("Tesla");

        ManufacturerDO actualManufacturer = new ManufacturerDO("Tesla");

        Mockito.when(manufacturerRepository.findById(1L))
            .thenReturn(Optional.of(actualManufacturer));
        assertThat(manufacturerService.find(1L)).isEqualTo(expectedManufacturer);
    }

    @Test(expected = EntityNotFoundException.class)
    public void find_byId_notFound() throws EntityNotFoundException {
        Mockito.when(manufacturerRepository.findById(1L)).thenReturn(Optional.empty());
        manufacturerService.find(1L);
    }

    @Test
    public void create_ok() throws ConstraintsViolationException {
        ManufacturerDO expectedManufacturer = new ManufacturerDO("Tesla");

        ManufacturerDO actualManufacturer = new ManufacturerDO("Tesla");

        Mockito.when(manufacturerRepository.save(any(ManufacturerDO.class)))
            .thenReturn(actualManufacturer);
        assertThat(manufacturerService.create(actualManufacturer)).isEqualTo(expectedManufacturer);
    }

    @Test(expected = ConstraintsViolationException.class)
    public void create_databaseIntegrityViolation() throws ConstraintsViolationException {
        ManufacturerDO actualManufacturer = new ManufacturerDO("Tesla");

        Mockito.when(manufacturerRepository.save(any(ManufacturerDO.class))).thenThrow(
            DataIntegrityViolationException.class);
        manufacturerService.create(actualManufacturer);
    }

    @Test
    public void delete_ok() throws EntityNotFoundException {
        ManufacturerDO expectedManufacturer = new ManufacturerDO("Tesla");
        expectedManufacturer.setDeleted(true);

        ManufacturerDO actualManufacturer = new ManufacturerDO("Tesla");

        Mockito.when(manufacturerRepository.findById(1L))
            .thenReturn(Optional.of(actualManufacturer));
        manufacturerService.delete(1L);
        assertThat(actualManufacturer).isEqualTo(expectedManufacturer);
    }

    @Test(expected = EntityNotFoundException.class)
    public void delete_notFound() throws EntityNotFoundException {
        Mockito.when(manufacturerRepository.findById(1L)).thenReturn(Optional.empty());
        manufacturerService.delete(1L);
    }

    @Test
    public void updateManufacturerName_ok()
        throws ConstraintsViolationException, EntityNotFoundException {
        ManufacturerDO expectedManufacturer = new ManufacturerDO("Tesla");

        ManufacturerDO actualManufacturer = new ManufacturerDO("BWM");
        Mockito.when(manufacturerRepository.findById(1L))
            .thenReturn(Optional.of(actualManufacturer));
        Mockito.when(manufacturerRepository.save(any(ManufacturerDO.class)))
            .thenReturn(any(ManufacturerDO.class));
        manufacturerService.updateManufacturerName(1L, "Tesla");
        assertThat(actualManufacturer).isEqualTo(expectedManufacturer);
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateManufacturerName_notFound()
        throws ConstraintsViolationException, EntityNotFoundException {
        Mockito.when(manufacturerRepository.findById(1L)).thenReturn(Optional.empty());
        manufacturerService.updateManufacturerName(1L, "Tesla");
    }

    @Test(expected = ConstraintsViolationException.class)
    public void updateManufacturerName_databaseIntegrityViolation()
        throws ConstraintsViolationException, EntityNotFoundException {
        ManufacturerDO actualManufacturer = new ManufacturerDO("BWM");
        Mockito.when(manufacturerRepository.findById(1L))
            .thenReturn(Optional.of(actualManufacturer));
        Mockito.when(manufacturerRepository.save(any(ManufacturerDO.class)))
            .thenThrow(DataIntegrityViolationException.class);
        manufacturerService.updateManufacturerName(1L, "Tesla");
    }
}