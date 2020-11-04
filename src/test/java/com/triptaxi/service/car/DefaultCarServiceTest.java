package com.triptaxi.service.car;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.triptaxi.dataaccessobject.CarRepository;
import com.triptaxi.domainobject.CarDO;
import com.triptaxi.domainobject.ManufacturerDO;
import com.triptaxi.domainvalue.EngineType;
import com.triptaxi.exception.ConstraintsViolationException;
import com.triptaxi.exception.EntityNotFoundException;
import com.triptaxi.service.manufacturer.ManufacturerService;
import java.util.Arrays;
import java.util.Optional;
import javax.validation.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCarServiceTest {

    private CarService carService;
    @Mock
    private CarRepository carRepository;
    @Mock
    private ManufacturerService manufacturerService;

    @Before
    public void setUp() {
        this.carService = new DefaultCarService(carRepository, manufacturerService);
    }

    @Test
    public void findAll() {
        CarDO expectedCar1 = new CarDO("1A-2B3C", 4, true);
        CarDO expectedCar2 = new CarDO("1X-2Y3Z", 7, false);

        CarDO actualCar1 = new CarDO("1A-2B3C", 4, true);
        CarDO actualCar2 = new CarDO("1X-2Y3Z", 7, false);

        Mockito.when(carRepository.findByDeletedFalse()).thenReturn(
            Arrays.asList(actualCar1, actualCar2));
        assertThat(carService.findAll()).isEqualTo(Arrays.asList(expectedCar1, expectedCar2));
    }

    @Test
    public void find_byEngineType() {
        CarDO expectedCar = new CarDO("1A-2B3C", 4, true);
        expectedCar.setEngineType(EngineType.GAS);

        CarDO actualCar = new CarDO("1A-2B3C", 4, true);
        actualCar.setEngineType(EngineType.GAS);

        Mockito.when(carRepository.findByEngineType(EngineType.GAS))
            .thenReturn(Arrays.asList(actualCar));
        assertThat(carService.find(EngineType.GAS)).isEqualTo(Arrays.asList(expectedCar));
    }

    @Test
    public void find_byCarId_ok() throws EntityNotFoundException {
        CarDO expectedCar = new CarDO("1A-2B3C", 4, true);

        CarDO actualCar = new CarDO("1A-2B3C", 4, true);

        Mockito.when(carRepository.findById(1L)).thenReturn(Optional.of(actualCar));
        assertThat(carService.find(1L)).isEqualTo(expectedCar);
    }

    @Test(expected = EntityNotFoundException.class)
    public void find_byCarId_notFound() throws EntityNotFoundException {
        Mockito.when(carRepository.findById(1L)).thenReturn(Optional.empty());
        carService.find(1L);
    }

    @Test
    public void create_ok() throws EntityNotFoundException, ConstraintsViolationException {
        CarDO expectedCar = new CarDO("1A-2B3C", 4, true);
        ManufacturerDO expectedManufacturer = new ManufacturerDO("Tesla");
        expectedCar.setManufacturer(expectedManufacturer);

        CarDO actualCar = new CarDO("1A-2B3C", 4, true);
        ManufacturerDO actualManufacturer = new ManufacturerDO("Tesla");
        actualCar.setManufacturer(actualManufacturer);

        Mockito.when(manufacturerService.find(1L)).thenReturn(actualManufacturer);
        Mockito.when(carRepository.save(any(CarDO.class))).thenReturn(actualCar);
        assertThat(carService.create(actualCar, 1L)).isEqualTo(expectedCar);
    }

    @Test(expected = EntityNotFoundException.class)
    public void create_manufacturer_notFound()
        throws EntityNotFoundException, ConstraintsViolationException {
        CarDO actualCar = new CarDO("1A-2B3C", 4, true);
        ManufacturerDO actualManufacturer = new ManufacturerDO("Tesla");
        actualCar.setManufacturer(actualManufacturer);

        Mockito.when(manufacturerService.find(1L)).thenThrow(EntityNotFoundException.class);
        carService.create(actualCar, 1L);
    }

    @Test(expected = ConstraintsViolationException.class)
    public void create_databaseIntegrityViolation()
        throws EntityNotFoundException, ConstraintsViolationException {
        CarDO actualCar = new CarDO("1A-2B3C", 4, true);
        ManufacturerDO actualManufacturer = new ManufacturerDO("Tesla");
        actualCar.setManufacturer(actualManufacturer);

        Mockito.when(manufacturerService.find(1L)).thenReturn(actualManufacturer);
        Mockito.when(carRepository.save(any(CarDO.class))).thenThrow(
            DataIntegrityViolationException.class);
        carService.create(actualCar, 1L);
    }

    @Test(expected = ConstraintsViolationException.class)
    public void create_fieldValidationFailure()
        throws EntityNotFoundException, ConstraintsViolationException {
        CarDO expectedCar = new CarDO("1A-2B3C", 4, true);
        ManufacturerDO expectedManufacturer = new ManufacturerDO("Tesla");
        expectedCar.setManufacturer(expectedManufacturer);

        CarDO actualCar = new CarDO("1A-2B3C", 4, true);
        ManufacturerDO actualManufacturer = new ManufacturerDO("Tesla");
        actualCar.setManufacturer(actualManufacturer);

        Mockito.when(manufacturerService.find(1L)).thenReturn(actualManufacturer);
        Mockito.when(carRepository.save(any(CarDO.class)))
            .thenThrow(ConstraintViolationException.class);
        assertThat(carService.create(actualCar, 1L)).isEqualTo(expectedCar);
    }

    @Test
    public void delete_ok() throws EntityNotFoundException {
        CarDO expectedCar = new CarDO("1A-2B3C", 4, true);
        expectedCar.setDeleted(true);

        CarDO actualCar = new CarDO("1A-2B3C", 4, true);

        Mockito.when(carRepository.findById(1L)).thenReturn(Optional.of(actualCar));
        carService.delete(1L);
        assertThat(actualCar).isEqualTo(expectedCar);
    }

    @Test(expected = EntityNotFoundException.class)
    public void delete_notFound() throws EntityNotFoundException {
        Mockito.when(carRepository.findById(1L)).thenReturn(Optional.empty());
        carService.delete(1L);
    }

    @Test
    public void updateRating_ok() throws ConstraintsViolationException, EntityNotFoundException {
        CarDO expectedCar = new CarDO("1A-2B3C", 4, true);
        expectedCar.setRating(4.7F);

        CarDO actualCar = new CarDO("1A-2B3C", 4, true);
        actualCar.setRating(3.5F);

        Mockito.when(carRepository.findById(1L)).thenReturn(Optional.of(actualCar));
        carService.updateRating(1L, 4.7F);
        assertThat(actualCar).isEqualTo(expectedCar);
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateRating_notFound()
        throws ConstraintsViolationException, EntityNotFoundException {
        Mockito.when(carRepository.findById(1L)).thenReturn(Optional.empty());
        carService.updateRating(1L, 4.7F);
    }

    @Test(expected = ConstraintsViolationException.class)
    public void updateRating_invalidRatingUnderflow()
        throws ConstraintsViolationException, EntityNotFoundException {
        carService.updateRating(1L, -1.7F);
    }

    @Test(expected = ConstraintsViolationException.class)
    public void updateRating_invalidRatingOverflow()
        throws ConstraintsViolationException, EntityNotFoundException {
        carService.updateRating(1L, 8.7F);
    }
}
