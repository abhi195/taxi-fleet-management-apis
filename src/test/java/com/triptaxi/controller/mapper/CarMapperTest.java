package com.triptaxi.controller.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.triptaxi.datatransferobject.CarDTO;
import com.triptaxi.domainobject.CarDO;
import com.triptaxi.domainobject.ManufacturerDO;
import com.triptaxi.domainvalue.EngineType;
import java.util.Arrays;
import org.junit.Test;

public class CarMapperTest {

    @Test
    public void makeCarDO() {
        CarDTO carDTO = CarDTO.builder()
            .licensePlate("1A-2B3C")
            .seatCount(4)
            .convertible(true)
            .rating(3.5F)
            .engineType(EngineType.GAS)
            .build();

        CarDO expectedCarDO = new CarDO("1A-2B3C", 4, true);
        expectedCarDO.setRating(3.5F);
        expectedCarDO.setEngineType(EngineType.GAS);

        assertThat(CarMapper.makeCarDO(carDTO)).isEqualTo(expectedCarDO);
    }

    @Test
    public void makeCarDTO() {
        CarDO carDO = new CarDO("1A-2B3C", 4, true);
        carDO.setRating(3.5F);
        carDO.setEngineType(EngineType.GAS);
        carDO.setManufacturer(new ManufacturerDO("Tesla"));

        CarDTO expectedCarDTO = CarDTO.builder()
            .licensePlate("1A-2B3C")
            .seatCount(4)
            .convertible(true)
            .rating(3.5F)
            .engineType(EngineType.GAS)
            .manufacturerId(null)
            .build();

        assertThat(CarMapper.makeCarDTO(carDO)).isEqualTo(expectedCarDTO);
    }

    @Test
    public void makeCarDTOList() {
        CarDO carDO = new CarDO("1A-2B3C", 4, true);
        carDO.setRating(3.5F);
        carDO.setEngineType(EngineType.GAS);
        carDO.setManufacturer(new ManufacturerDO("Tesla"));

        CarDTO expectedCarDTO = CarDTO.builder()
            .licensePlate("1A-2B3C")
            .seatCount(4)
            .convertible(true)
            .rating(3.5F)
            .engineType(EngineType.GAS)
            .manufacturerId(null)
            .build();

        assertThat(CarMapper.makeCarDTOList(Arrays.asList(carDO)))
            .isEqualTo(Arrays.asList(expectedCarDTO));
    }
}