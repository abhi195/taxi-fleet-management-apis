package com.triptaxi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.triptaxi.datatransferobject.DriverDTO;
import com.triptaxi.domainobject.DriverDO;
import com.triptaxi.domainvalue.OnlineStatus;
import com.triptaxi.exception.CarAlreadyInUseException;
import com.triptaxi.exception.ConstraintsViolationException;
import com.triptaxi.exception.EntityNotFoundException;
import com.triptaxi.service.driver.DriverService;
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

@WebMvcTest(controllers = DriverController.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DriverService driverService;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void getDriver() throws Exception {
        DriverDO driverDO = new DriverDO("driver", "driverPwd");

        DriverDTO expectedDriverDTO = DriverDTO.builder()
            .id(null)
            .username("driver")
            .password("driverPwd")
            .build();

        Mockito.when(driverService.find(1L)).thenReturn(driverDO);
        mockMvc.perform(get("/v1/drivers/1"))
            .andExpect(status().isOk())
            .andExpect(content().string(objectMapper.writeValueAsString(expectedDriverDTO)));
    }

    @Test
    public void getDriver_notFound() throws Exception {
        Mockito.when(driverService.find(1L)).thenThrow(EntityNotFoundException.class);
        mockMvc.perform(get("/v1/drivers/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void createDriver_ok() throws Exception {
        DriverDO driverDO = new DriverDO("driver", "driverPwd");

        DriverDTO expectedDriverDTO = DriverDTO.builder()
            .id(null)
            .username("driver")
            .password("driverPwd")
            .build();

        Mockito.when(driverService.create(any(DriverDO.class))).thenReturn(driverDO);
        mockMvc.perform(
            post("/v1/drivers").contentType("application/json")
                .content(objectMapper.writeValueAsString(expectedDriverDTO)))
            .andExpect(status().isCreated())
            .andExpect(content().string(objectMapper.writeValueAsString(expectedDriverDTO)));
    }

    @Test
    public void createDriver_invalidCreateData() throws Exception {
        DriverDO driverDO = new DriverDO("driver", "driverPwd");

        DriverDTO expectedDriverDTO = DriverDTO.builder()
            .id(null)
            .password("driverPwd")
            .build();

        Mockito.when(driverService.create(any(DriverDO.class))).thenReturn(driverDO);
        mockMvc.perform(
            post("/v1/drivers").contentType("application/json")
                .content(objectMapper.writeValueAsString(expectedDriverDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void createDriver_constraintsViolation() throws Exception {
        DriverDTO expectedDriverDTO = DriverDTO.builder()
            .id(null)
            .password("driverPwd")
            .build();

        Mockito.when(driverService.create(any(DriverDO.class)))
            .thenThrow(ConstraintsViolationException.class);
        mockMvc.perform(
            post("/v1/drivers").contentType("application/json")
                .content(objectMapper.writeValueAsString(expectedDriverDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteDriver_ok() throws Exception {
        Mockito.doNothing().when(driverService).delete(1L);
        mockMvc.perform(delete("/v1/drivers/1"))
            .andExpect(status().isOk());
    }

    @Test
    public void deleteDriver_notFound() throws Exception {
        Mockito.doThrow(EntityNotFoundException.class).when(driverService).delete(1L);
        mockMvc.perform(delete("/v1/drivers/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateLocation_ok() throws Exception {
        Mockito.doNothing().when(driverService).updateLocation(1L, 10.5, 12.4);
        mockMvc.perform(put("/v1/drivers/1").param("longitude", "10.5").param("latitude", "12.4"))
            .andExpect(status().isOk());
    }

    @Test
    public void updateLocation_notFound() throws Exception {
        Mockito.doThrow(EntityNotFoundException.class).when(driverService)
            .updateLocation(1L, 10.5, 12.4);
        mockMvc.perform(put("/v1/drivers/1").param("longitude", "10.5").param("latitude", "12.4"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateLocation_constraintsViolation() throws Exception {
        Mockito.doThrow(ConstraintsViolationException.class).when(driverService)
            .updateLocation(1L, 10.5, 12.4);
        mockMvc.perform(put("/v1/drivers/1").param("longitude", "10.5").param("latitude", "12.4"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void findDrivers() throws Exception {
        DriverDO driverDO = new DriverDO("driver", "driverPwd");

        DriverDTO driverDTO = DriverDTO.builder()
            .id(null)
            .username("driver")
            .password("driverPwd")
            .build();

        List<DriverDTO> expectedDriverDTOList = Arrays.asList(driverDTO);

        Mockito.when(driverService.find(OnlineStatus.ONLINE)).thenReturn(Arrays.asList(driverDO));
        mockMvc.perform(get("/v1/drivers").param("onlineStatus", OnlineStatus.ONLINE.name()))
            .andExpect(status().isOk())
            .andExpect(content().string(objectMapper.writeValueAsString(expectedDriverDTOList)));
    }

    @Test
    public void selectCar_ok() throws Exception {
        Mockito.doNothing().when(driverService).selectCar(1L, 1L);
        mockMvc.perform(put("/v1/drivers/1/selectCar").param("carId", "1"))
            .andExpect(status().isOk());
    }

    @Test
    public void selectCar_notFound() throws Exception {
        Mockito.doThrow(EntityNotFoundException.class).when(driverService).selectCar(1L, 1L);
        mockMvc.perform(put("/v1/drivers/1/selectCar").param("carId", "1"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void selectCar_constraintsViolation() throws Exception {
        Mockito.doThrow(ConstraintsViolationException.class).when(driverService).selectCar(1L, 1L);
        mockMvc.perform(put("/v1/drivers/1/selectCar").param("carId", "1"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void selectCar_carAlreadyInUse() throws Exception {
        Mockito.doThrow(CarAlreadyInUseException.class).when(driverService).selectCar(1L, 1L);
        mockMvc.perform(put("/v1/drivers/1/selectCar").param("carId", "1"))
            .andExpect(status().isConflict());
    }

    @Test
    public void deselectCar_ok() throws Exception {
        Mockito.doNothing().when(driverService).deselectCar(1L);
        mockMvc.perform(put("/v1/drivers/1/deselectCar"))
            .andExpect(status().isOk());
    }

    @Test
    public void deselectCar_notFound() throws Exception {
        Mockito.doThrow(EntityNotFoundException.class).when(driverService).deselectCar(1L);
        mockMvc.perform(put("/v1/drivers/1/deselectCar"))
            .andExpect(status().isNotFound());
    }
}
