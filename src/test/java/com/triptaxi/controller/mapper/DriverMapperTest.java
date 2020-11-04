package com.triptaxi.controller.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.triptaxi.datatransferobject.DriverDTO;
import com.triptaxi.domainobject.DriverDO;
import com.triptaxi.domainvalue.GeoCoordinate;
import java.util.Arrays;
import org.junit.Test;

public class DriverMapperTest {

    @Test
    public void makeDriverDO() {

        DriverDTO driverDTO = DriverDTO.builder()
            .username("driver")
            .password("driverPwd")
            .build();

        DriverDO expectedDriverDO = new DriverDO("driver", "driverPwd");

        assertThat(DriverMapper.makeDriverDO(driverDTO)).isEqualTo(expectedDriverDO);
    }

    @Test
    public void makeDriverDTO() {
        DriverDO driverDO = new DriverDO("driver", "driverPwd");
        driverDO.setCoordinate(new GeoCoordinate(12.9, 89.1));

        DriverDTO expectedDriverDTO = DriverDTO.builder()
            .id(null)
            .username("driver")
            .password("driverPwd")
            .coordinate(new GeoCoordinate(12.9, 89.1))
            .build();

        assertThat(DriverMapper.makeDriverDTO(driverDO)).isEqualTo(expectedDriverDTO);
    }

    @Test
    public void makeDriverDTOList() {
        DriverDO driverDO = new DriverDO("driver", "driverPwd");
        driverDO.setCoordinate(new GeoCoordinate(12.9, 89.1));

        DriverDTO expectedDriverDTO = DriverDTO.builder()
            .id(null)
            .username("driver")
            .password("driverPwd")
            .coordinate(new GeoCoordinate(12.9, 89.1))
            .build();

        assertThat(DriverMapper.makeDriverDTOList(Arrays.asList(driverDO)))
            .isEqualTo(Arrays.asList(expectedDriverDTO));
    }
}