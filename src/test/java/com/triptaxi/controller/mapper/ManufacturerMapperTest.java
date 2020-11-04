package com.triptaxi.controller.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.triptaxi.datatransferobject.ManufacturerDTO;
import com.triptaxi.domainobject.ManufacturerDO;
import java.util.Arrays;
import org.junit.Test;

public class ManufacturerMapperTest {

    @Test
    public void makeManufacturerDO() {
        ManufacturerDTO manufacturerDTO = ManufacturerDTO.builder()
            .manufacturerName("Tesla")
            .build();

        ManufacturerDO expectedManufacturerDO = new ManufacturerDO("Tesla");

        assertThat(ManufacturerMapper.makeManufacturerDO(manufacturerDTO))
            .isEqualTo(expectedManufacturerDO);
    }

    @Test
    public void makeManufacturerDTO() {
        ManufacturerDO manufacturerDO = new ManufacturerDO("Tesla");

        ManufacturerDTO expectedManufacturerDTO = ManufacturerDTO.builder()
            .id(null)
            .manufacturerName("Tesla")
            .build();

        assertThat(ManufacturerMapper.makeManufacturerDTO(manufacturerDO))
            .isEqualTo(expectedManufacturerDTO);
    }

    @Test
    public void makeDriverDTOList() {
        ManufacturerDO manufacturerDO = new ManufacturerDO("Tesla");

        ManufacturerDTO expectedManufacturerDTO = ManufacturerDTO.builder()
            .id(null)
            .manufacturerName("Tesla")
            .build();

        assertThat(ManufacturerMapper.makeDriverDTOList(Arrays.asList(manufacturerDO)))
            .isEqualTo(Arrays.asList(expectedManufacturerDTO));
    }
}