package com.triptaxi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.triptaxi.datatransferobject.ManufacturerDTO;
import com.triptaxi.domainobject.ManufacturerDO;
import com.triptaxi.exception.ConstraintsViolationException;
import com.triptaxi.exception.EntityNotFoundException;
import com.triptaxi.service.manufacturer.ManufacturerService;
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

@WebMvcTest(controllers = ManufacturerController.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ManufacturerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManufacturerService manufacturerService;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void getManufacturers() throws Exception {
        ManufacturerDO manufacturerDO = new ManufacturerDO("Tesla");

        ManufacturerDTO manufacturerDTO = ManufacturerDTO.builder()
            .manufacturerName("Tesla")
            .build();

        List<ManufacturerDTO> expectedManufacturerDTOList = Arrays.asList(manufacturerDTO);

        Mockito.when(manufacturerService.findAll()).thenReturn(Arrays.asList(manufacturerDO));
        mockMvc.perform(get("/v1/manufacturers"))
            .andExpect(status().isOk())
            .andExpect(
                content().string(objectMapper.writeValueAsString(expectedManufacturerDTOList)));
    }


    @Test
    public void getManufacturer_ok() throws Exception {
        ManufacturerDO manufacturerDO = new ManufacturerDO("Tesla");

        ManufacturerDTO manufacturerDTO = ManufacturerDTO.builder()
            .manufacturerName("Tesla")
            .build();

        Mockito.when(manufacturerService.find(1L)).thenReturn(manufacturerDO);
        mockMvc.perform(get("/v1/manufacturers/1"))
            .andExpect(status().isOk())
            .andExpect(content().string(objectMapper.writeValueAsString(manufacturerDTO)));
    }


    @Test
    public void getManufacturer_notFound() throws Exception {
        Mockito.when(manufacturerService.find(1L)).thenThrow(EntityNotFoundException.class);
        mockMvc.perform(get("/v1/manufacturers/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void createManufacturer_ok() throws Exception {
        ManufacturerDO manufacturerDO = new ManufacturerDO("Tesla");

        ManufacturerDTO manufacturerDTO = ManufacturerDTO.builder()
            .manufacturerName("Tesla")
            .build();

        Mockito.when(manufacturerService.create(any(ManufacturerDO.class)))
            .thenReturn(manufacturerDO);
        mockMvc.perform(
            post("/v1/manufacturers").contentType("application/json")
                .content(objectMapper.writeValueAsString(manufacturerDTO)))
            .andExpect(status().isCreated())
            .andExpect(content().string(objectMapper.writeValueAsString(manufacturerDTO)));
    }

    @Test
    public void createManufacturer_invalidCreateData() throws Exception {
        ManufacturerDO manufacturerDO = new ManufacturerDO("Tesla");

        ManufacturerDTO manufacturerDTO = ManufacturerDTO.builder()
            .build();

        Mockito.when(manufacturerService.create(any(ManufacturerDO.class)))
            .thenReturn(manufacturerDO);
        mockMvc.perform(
            post("/v1/manufacturers").contentType("application/json")
                .content(objectMapper.writeValueAsString(manufacturerDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void createManufacturer_constraintsViolation() throws Exception {
        ManufacturerDTO manufacturerDTO = ManufacturerDTO.builder()
            .manufacturerName("Tesla")
            .build();

        Mockito.when(manufacturerService.create(any(ManufacturerDO.class)))
            .thenThrow(ConstraintsViolationException.class);
        mockMvc.perform(
            post("/v1/manufacturers").contentType("application/json")
                .content(objectMapper.writeValueAsString(manufacturerDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteManufacturer_ok() throws Exception {
        Mockito.doNothing().when(manufacturerService).delete(1L);
        mockMvc.perform(delete("/v1/manufacturers/1"))
            .andExpect(status().isOk());
    }

    @Test
    public void deleteManufacturer_notFound() throws Exception {
        Mockito.doThrow(EntityNotFoundException.class).when(manufacturerService).delete(1L);
        mockMvc.perform(delete("/v1/manufacturers/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateName_ok() throws Exception {
        Mockito.doNothing().when(manufacturerService).updateManufacturerName(1L, "BMW");
        mockMvc.perform(put("/v1/manufacturers/1").param("manufacturerName", "BMW"))
            .andExpect(status().isOk());
    }

    @Test
    public void updateName_invalidPutData() throws Exception {
        Mockito.doNothing().when(manufacturerService).updateManufacturerName(1L, "BMW");
        mockMvc.perform(put("/v1/manufacturers/1"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void updateName_notFound() throws Exception {
        Mockito.doThrow(EntityNotFoundException.class).when(manufacturerService)
            .updateManufacturerName(1L, "BMW");
        mockMvc.perform(put("/v1/manufacturers/1").param("manufacturerName", "BMW"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateName_constraintsViolation() throws Exception {
        Mockito.doThrow(ConstraintsViolationException.class).when(manufacturerService)
            .updateManufacturerName(1L, "BMW");
        mockMvc.perform(put("/v1/manufacturers/1").param("manufacturerName", "BMW"))
            .andExpect(status().isBadRequest());
    }
}
