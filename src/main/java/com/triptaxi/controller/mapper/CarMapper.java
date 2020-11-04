package com.triptaxi.controller.mapper;

import com.triptaxi.datatransferobject.CarDTO;
import com.triptaxi.domainobject.CarDO;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CarMapper {

    public static CarDO makeCarDO(CarDTO carDTO) {
        CarDO carDO = new CarDO(carDTO.getLicensePlate(), carDTO.getSeatCount(),
            carDTO.getConvertible());
        if (carDTO.getRating() != null) {
            carDO.setRating(carDTO.getRating());
        }
        if (carDTO.getEngineType() != null) {
            carDO.setEngineType(carDTO.getEngineType());
        }

        return carDO;
    }

    public static CarDTO makeCarDTO(CarDO carDO) {

        CarDTO.CarDTOBuilder carDTOBuilder = CarDTO.builder()
            .id(carDO.getId())
            .licensePlate(carDO.getLicensePlate())
            .seatCount(carDO.getSeatCount())
            .convertible(carDO.getConvertible())
            .manufacturerId(carDO.getManufacturer().getId())
            .engineType(carDO.getEngineType());

        if (carDO.getRating() != null) {
            carDTOBuilder.rating(carDO.getRating());
        }

        return carDTOBuilder.build();
    }

    public static List<CarDTO> makeCarDTOList(Collection<CarDO> cars) {
        return cars.stream()
            .map(CarMapper::makeCarDTO)
            .collect(Collectors.toList());
    }
}
