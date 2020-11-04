package com.triptaxi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.triptaxi.datatransferobject.CarDTO;
import com.triptaxi.domainobject.CarDO;
import com.triptaxi.domainobject.ManufacturerDO;
import com.triptaxi.domainvalue.EngineType;
import com.triptaxi.exception.ConstraintsViolationException;
import com.triptaxi.exception.EntityNotFoundException;
import com.triptaxi.service.car.CarService;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CarController.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void getCars() throws Exception {

        CarDO carDO = new CarDO("1A-2B3C", 4, true);
        carDO.setRating(3.5F);
        carDO.setEngineType(EngineType.GAS);
        carDO.setManufacturer(new ManufacturerDO("Tesla"));

        CarDTO carDTO = CarDTO.builder()
            .licensePlate("1A-2B3C")
            .seatCount(4)
            .convertible(true)
            .rating(3.5F)
            .engineType(EngineType.GAS)
            .manufacturerId(null)
            .build();

        List<CarDTO> expectedCarDTOList = Arrays.asList(carDTO);

        Mockito.when(carService.findAll()).thenReturn(Arrays.asList(carDO));
        mockMvc.perform(get("/v1/cars"))
            .andExpect(status().isOk())
            .andExpect(content().string(objectMapper.writeValueAsString(expectedCarDTOList)));
    }

    @Test
    public void findCars() throws Exception {
        CarDO carDO = new CarDO("1A-2B3C", 4, true);
        carDO.setRating(3.5F);
        carDO.setEngineType(EngineType.GAS);
        carDO.setManufacturer(new ManufacturerDO("Tesla"));

        CarDTO carDTO = CarDTO.builder()
            .licensePlate("1A-2B3C")
            .seatCount(4)
            .convertible(true)
            .rating(3.5F)
            .engineType(EngineType.GAS)
            .manufacturerId(null)
            .build();

        List<CarDTO> expectedCarDTOList = Arrays.asList(carDTO);

        Mockito.when(carService.find(EngineType.GAS)).thenReturn(Arrays.asList(carDO));
        mockMvc.perform(get("/v1/cars/engine").param("engineType", EngineType.GAS.name()))
            .andExpect(status().isOk())
            .andExpect(content().string(objectMapper.writeValueAsString(expectedCarDTOList)));
    }

    @Test
    public void getCar_found() throws Exception {
        CarDO carDO = new CarDO("1A-2B3C", 4, true);
        carDO.setRating(3.5F);
        carDO.setEngineType(EngineType.GAS);
        carDO.setManufacturer(new ManufacturerDO("Tesla"));

        CarDTO carDTO = CarDTO.builder()
            .licensePlate("1A-2B3C")
            .seatCount(4)
            .convertible(true)
            .rating(3.5F)
            .engineType(EngineType.GAS)
            .manufacturerId(null)
            .build();

        Mockito.when(carService.find(1L)).thenReturn(carDO);
        mockMvc.perform(get("/v1/cars/1"))
            .andExpect(status().isOk())
            .andExpect(content().string(objectMapper.writeValueAsString(carDTO)));
    }

    @Test
    public void getCar_notFound() throws Exception {
        Mockito.when(carService.find(1L)).thenThrow(EntityNotFoundException.class);
        mockMvc.perform(get("/v1/cars/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void createCar_ok() throws Exception {
        CarDO carDO = new CarDO("1A-2B3C", 4, true);
        carDO.setRating(3.5F);
        carDO.setEngineType(EngineType.GAS);
        carDO.setManufacturer(new ManufacturerDO("Tesla"));

        CarDTO carDTO = CarDTO.builder()
            .licensePlate("1A-2B3C")
            .seatCount(4)
            .convertible(true)
            .rating(3.5F)
            .engineType(EngineType.GAS)
            .manufacturerId(1L)
            .build();

        CarDTO expectedCarDTO = CarDTO.builder()
            .licensePlate("1A-2B3C")
            .seatCount(4)
            .convertible(true)
            .rating(3.5F)
            .engineType(EngineType.GAS)
            .build();

        Mockito.when(carService.create(any(CarDO.class), any(Long.class))).thenReturn(carDO);
        mockMvc.perform(
            post("/v1/cars").contentType("application/json")
                .content(objectMapper.writeValueAsString(carDTO)))
            .andExpect(status().isCreated())
            .andExpect(content().string(objectMapper.writeValueAsString(expectedCarDTO)));
    }

    @Test
    public void createCar_manufacturer_notFound() throws Exception {
        CarDTO carDTO = CarDTO.builder()
            .licensePlate("1A-2B3C")
            .seatCount(4)
            .convertible(true)
            .rating(3.5F)
            .engineType(EngineType.GAS)
            .manufacturerId(1L)
            .build();

        Mockito.when(carService.create(any(CarDO.class), any(Long.class)))
            .thenThrow(EntityNotFoundException.class);
        mockMvc.perform(
            post("/v1/cars").contentType("application/json")
                .content(objectMapper.writeValueAsString(carDTO)))
            .andExpect(status().isNotFound());
    }

    @Test
    public void createCar_databaseIntegrityViolation() throws Exception {
        CarDTO carDTO = CarDTO.builder()
            .licensePlate("1A-2B3C")
            .seatCount(4)
            .convertible(true)
            .rating(3.5F)
            .engineType(EngineType.GAS)
            .manufacturerId(1L)
            .build();

        Mockito.when(carService.create(any(CarDO.class), any(Long.class)))
            .thenThrow(ConstraintsViolationException.class);
        mockMvc.perform(
            post("/v1/cars").contentType("application/json")
                .content(objectMapper.writeValueAsString(carDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void createCar_invalidCreateData() throws Exception {
        CarDO carDO = new CarDO("1A-2B3C", 4, true);
        carDO.setRating(3.5F);
        carDO.setEngineType(EngineType.GAS);
        carDO.setManufacturer(new ManufacturerDO("Tesla"));

        CarDTO carDTO = CarDTO.builder()
            .seatCount(4)
            .convertible(true)
            .rating(3.5F)
            .engineType(EngineType.GAS)
            .manufacturerId(1L)
            .build();

        Mockito.when(carService.create(any(CarDO.class), any(Long.class))).thenReturn(carDO);
        mockMvc.perform(
            post("/v1/cars").contentType("application/json")
                .content(objectMapper.writeValueAsString(carDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteCar_ok() throws Exception {
        Mockito.doNothing().when(carService).delete(1L);
        mockMvc.perform(delete("/v1/cars/1"))
            .andExpect(status().isOk());
    }

    @Test
    public void deleteCar_notFound() throws Exception {
        Mockito.doThrow(EntityNotFoundException.class).when(carService).delete(1L);
        mockMvc.perform(delete("/v1/cars/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateRating_ok() throws Exception {
        Mockito.doNothing().when(carService).updateRating(1L, 4.3F);
        mockMvc.perform(put("/v1/cars/1").param("rating", "4.3"))
            .andExpect(status().isOk());
    }

    @Test
    public void updateRating_notFound() throws Exception {
        Mockito.doThrow(EntityNotFoundException.class).when(carService).updateRating(1L, 4.3F);
        mockMvc.perform(put("/v1/cars/1").param("rating", "4.3"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateRating_constraintsViolation() throws Exception {
        Mockito.doThrow(ConstraintsViolationException.class).when(carService)
            .updateRating(1L, 9.3F);
        mockMvc.perform(put("/v1/cars/1").param("rating", "9.3"))
            .andExpect(status().isBadRequest());
    }
}
