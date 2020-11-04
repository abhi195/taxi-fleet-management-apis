package com.triptaxi.service.driver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.triptaxi.dataaccessobject.DriverRepository;
import com.triptaxi.domainobject.CarDO;
import com.triptaxi.domainobject.DriverDO;
import com.triptaxi.domainvalue.GeoCoordinate;
import com.triptaxi.domainvalue.OnlineStatus;
import com.triptaxi.exception.CarAlreadyInUseException;
import com.triptaxi.exception.ConstraintsViolationException;
import com.triptaxi.exception.EntityNotFoundException;
import com.triptaxi.service.car.CarService;
import java.util.Arrays;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDriverServiceTest {

    private DriverService driverService;
    @Mock
    private DriverRepository driverRepository;
    @Mock
    private CarService carService;

    @Before
    public void setUp() {
        this.driverService = new DefaultDriverService(driverRepository, carService);
    }

    @Test
    public void find_byDriverId_ok() throws EntityNotFoundException {
        DriverDO expectedDriver = new DriverDO("driver", "driverPwd");

        DriverDO actualDriver = new DriverDO("driver", "driverPwd");

        Mockito.when(driverRepository.findById(1L)).thenReturn(Optional.of(actualDriver));
        assertThat(driverService.find(1L)).isEqualTo(expectedDriver);
    }

    @Test(expected = EntityNotFoundException.class)
    public void find_byDriverId_notFound() throws EntityNotFoundException {
        Mockito.when(driverRepository.findById(1L)).thenReturn(Optional.empty());
        driverService.find(1L);
    }

    @Test
    public void create_ok() throws ConstraintsViolationException {
        DriverDO expectedDriver = new DriverDO("driver", "driverPwd");

        DriverDO actualDriver = new DriverDO("driver", "driverPwd");

        Mockito.when(driverRepository.save(any(DriverDO.class))).thenReturn(actualDriver);
        assertThat(driverService.create(actualDriver)).isEqualTo(expectedDriver);
    }

    @Test(expected = ConstraintsViolationException.class)
    public void create_databaseIntegrityViolation() throws ConstraintsViolationException {
        DriverDO actualDriver = new DriverDO("driver", "driverPwd");

        Mockito.when(driverRepository.save(any(DriverDO.class))).thenThrow(
            DataIntegrityViolationException.class);
        driverService.create(actualDriver);
    }

    @Test
    public void delete_ok() throws EntityNotFoundException {
        DriverDO expectedDriver = new DriverDO("driver", "driverPwd");
        expectedDriver.setDeleted(true);

        DriverDO actualDriver = new DriverDO("driver", "driverPwd");
        Mockito.when(driverRepository.findById(1L)).thenReturn(Optional.of(actualDriver));
        driverService.delete(1L);
        assertThat(actualDriver).isEqualTo(expectedDriver);
    }

    @Test(expected = EntityNotFoundException.class)
    public void delete_notFound() throws EntityNotFoundException {
        Mockito.when(driverRepository.findById(1L)).thenReturn(Optional.empty());
        driverService.delete(1L);
    }

    @Test
    public void updateLocation_ok() throws ConstraintsViolationException, EntityNotFoundException {
        DriverDO expectedDriver = new DriverDO("driver", "driverPwd");
        expectedDriver.setCoordinate(new GeoCoordinate(12.72, 101.1));

        DriverDO actualDriver = new DriverDO("driver", "driverPwd");
        Mockito.when(driverRepository.findById(1L)).thenReturn(Optional.of(actualDriver));
        driverService.updateLocation(1L, 101.1, 12.72);
        assertThat(actualDriver)
            .isEqualToIgnoringGivenFields(expectedDriver, "dateCoordinateUpdated");
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateLocation_notFound()
        throws ConstraintsViolationException, EntityNotFoundException {
        Mockito.when(driverRepository.findById(1L)).thenReturn(Optional.empty());
        driverService.updateLocation(1L, 101.1, 12.72);
    }

    @Test(expected = ConstraintsViolationException.class)
    public void updateLocation_invalidCoordinates_1()
        throws ConstraintsViolationException, EntityNotFoundException {
        DriverDO actualDriver = new DriverDO("driver", "driverPwd");
        Mockito.when(driverRepository.findById(1L)).thenReturn(Optional.of(actualDriver));
        driverService.updateLocation(1L, 181.1, 12.72);
    }

    @Test(expected = ConstraintsViolationException.class)
    public void updateLocation_invalidCoordinates_2()
        throws ConstraintsViolationException, EntityNotFoundException {
        DriverDO actualDriver = new DriverDO("driver", "driverPwd");
        Mockito.when(driverRepository.findById(1L)).thenReturn(Optional.of(actualDriver));
        driverService.updateLocation(1L, 101.1, 91.72);
    }

    @Test(expected = ConstraintsViolationException.class)
    public void updateLocation_invalidCoordinates_3()
        throws ConstraintsViolationException, EntityNotFoundException {
        DriverDO actualDriver = new DriverDO("driver", "driverPwd");
        Mockito.when(driverRepository.findById(1L)).thenReturn(Optional.of(actualDriver));
        driverService.updateLocation(1L, -181.1, 12.72);
    }

    @Test(expected = ConstraintsViolationException.class)
    public void updateLocation_invalidCoordinates_4()
        throws ConstraintsViolationException, EntityNotFoundException {
        DriverDO actualDriver = new DriverDO("driver", "driverPwd");
        Mockito.when(driverRepository.findById(1L)).thenReturn(Optional.of(actualDriver));
        driverService.updateLocation(1L, 101.1, -91.72);
    }

    @Test
    public void find_byOnlineStatus() {
        DriverDO expectedDriver = new DriverDO("driver", "driverPwd");
        expectedDriver.setOnlineStatus(OnlineStatus.ONLINE);

        DriverDO actualDriver = new DriverDO("driver", "driverPwd");
        actualDriver.setOnlineStatus(OnlineStatus.ONLINE);

        Mockito.when(driverRepository.findByOnlineStatus(OnlineStatus.ONLINE))
            .thenReturn(Arrays.asList(actualDriver));
        assertThat(driverService.find(OnlineStatus.ONLINE))
            .isEqualTo(Arrays.asList(expectedDriver));
    }

    @Test
    public void selectCar_ok()
        throws EntityNotFoundException, CarAlreadyInUseException, ConstraintsViolationException {
        DriverDO expectedDriver = new DriverDO("driver", "driverPwd");
        expectedDriver.setOnlineStatus(OnlineStatus.ONLINE);
        CarDO expectedDriverCar = new CarDO("1A-2B3C", 4, true);
        expectedDriver.setCar(expectedDriverCar);

        DriverDO actualDriver = new DriverDO("driver", "driverPwd");
        actualDriver.setOnlineStatus(OnlineStatus.ONLINE);
        CarDO actualDriverCar = new CarDO("1A-2B3C", 4, true);

        Mockito.when(driverRepository.findById(1L)).thenReturn(Optional.of(actualDriver));
        Mockito.when(carService.find(1L)).thenReturn(actualDriverCar);
        Mockito.when(driverRepository.save(any(DriverDO.class))).thenReturn(actualDriver);
        driverService.selectCar(1L, 1L);
        assertThat(actualDriver).isEqualTo(expectedDriver);
    }

    @Test(expected = EntityNotFoundException.class)
    public void selectCar_driver_notFound()
        throws EntityNotFoundException, CarAlreadyInUseException, ConstraintsViolationException {
        Mockito.when(driverRepository.findById(1L)).thenReturn(Optional.empty());
        driverService.selectCar(1L, 1L);
    }

    @Test(expected = EntityNotFoundException.class)
    public void selectCar_car_notFound()
        throws EntityNotFoundException, CarAlreadyInUseException, ConstraintsViolationException {
        DriverDO actualDriver = new DriverDO("driver", "driverPwd");

        Mockito.when(driverRepository.findById(1L)).thenReturn(Optional.of(actualDriver));
        Mockito.when(carService.find(1L)).thenThrow(EntityNotFoundException.class);
        driverService.selectCar(1L, 1L);
    }

    @Test(expected = ConstraintsViolationException.class)
    public void selectCar_driver_offline()
        throws EntityNotFoundException, CarAlreadyInUseException, ConstraintsViolationException {
        DriverDO actualDriver = new DriverDO("driver", "driverPwd");
        actualDriver.setOnlineStatus(OnlineStatus.OFFLINE);
        CarDO actualDriverCar = new CarDO("1A-2B3C", 4, true);

        Mockito.when(driverRepository.findById(1L)).thenReturn(Optional.of(actualDriver));
        Mockito.when(carService.find(1L)).thenReturn(actualDriverCar);
        driverService.selectCar(1L, 1L);
    }

    @Test(expected = ConstraintsViolationException.class)
    public void selectCar_car_deleted()
        throws EntityNotFoundException, CarAlreadyInUseException, ConstraintsViolationException {
        DriverDO actualDriver = new DriverDO("driver", "driverPwd");
        actualDriver.setOnlineStatus(OnlineStatus.ONLINE);
        CarDO actualDriverCar = new CarDO("1A-2B3C", 4, true);
        actualDriverCar.setDeleted(true);

        Mockito.when(driverRepository.findById(1L)).thenReturn(Optional.of(actualDriver));
        Mockito.when(carService.find(1L)).thenReturn(actualDriverCar);
        driverService.selectCar(1L, 1L);
    }

    @Test(expected = CarAlreadyInUseException.class)
    public void selectCar_databaseIntegrityViolation_alreadyAssignedCar()
        throws EntityNotFoundException, CarAlreadyInUseException, ConstraintsViolationException {
        DriverDO actualDriver = new DriverDO("driver", "driverPwd");
        actualDriver.setOnlineStatus(OnlineStatus.ONLINE);
        CarDO actualDriverCar = new CarDO("1A-2B3C", 4, true);

        Mockito.when(driverRepository.findById(1L)).thenReturn(Optional.of(actualDriver));
        Mockito.when(carService.find(1L)).thenReturn(actualDriverCar);
        Mockito.when(driverRepository.save(any(DriverDO.class)))
            .thenThrow(DataIntegrityViolationException.class);
        driverService.selectCar(1L, 1L);
    }

    @Test
    public void deselectCar_ok() throws EntityNotFoundException {
        DriverDO expectedDriver = new DriverDO("driver", "driverPwd");

        DriverDO actualDriver = new DriverDO("driver", "driverPwd");
        CarDO actualDriverCar = new CarDO("1A-2B3C", 4, true);
        actualDriver.setCar(actualDriverCar);

        Mockito.when(driverRepository.findById(1L)).thenReturn(Optional.of(actualDriver));
        driverService.deselectCar(1L);
        assertThat(actualDriver).isEqualTo(expectedDriver);
    }

    @Test(expected = EntityNotFoundException.class)
    public void deselectCar_driver_notFound() throws EntityNotFoundException {
        Mockito.when(driverRepository.findById(1L)).thenReturn(Optional.empty());
        driverService.deselectCar(1L);
    }

    @Test
    public void search() {
        DriverDO expectedDriver = new DriverDO("driver", "driverPwd");

        DriverDO actualDriver = new DriverDO("driver", "driverPwd");

        Specification spec = Mockito.mock(Specification.class);
        Sort sort = Mockito.mock(Sort.class);
        Mockito.when(driverRepository.findAll(any(Specification.class), any(Sort.class)))
            .thenReturn(Arrays.asList(actualDriver));
        assertThat(driverService.search(spec, sort)).isEqualTo(Arrays.asList(expectedDriver));
    }
}
