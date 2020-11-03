package com.triptaxi.controller.mapper;

import com.triptaxi.datatransferobject.ManufacturerDTO;
import com.triptaxi.domainobject.ManufacturerDO;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ManufacturerMapper {

    public static ManufacturerDO makeManufacturerDO(ManufacturerDTO manufacturerDTO) {
        return new ManufacturerDO(manufacturerDTO.getManufacturerName());
    }

    public static ManufacturerDTO makeManufacturerDTO(ManufacturerDO manufacturerDO) {
        return ManufacturerDTO.builder()
            .id(manufacturerDO.getId())
            .manufacturerName(manufacturerDO.getManufacturerName())
            .build();
    }

    public static List<ManufacturerDTO> makeDriverDTOList(
        Collection<ManufacturerDO> manufacturers) {
        return manufacturers.stream()
            .map(ManufacturerMapper::makeManufacturerDTO)
            .collect(Collectors.toList());
    }
}
