package com.triptaxi.datatransferobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.triptaxi.domainvalue.EngineType;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarDTO {

    @JsonIgnore
    private Long id;

    @NotNull(message = "licensePlate can not be null!")
    private String licensePlate;

    @NotNull(message = "seatCount can not be null!")
    private Integer seatCount;

    @NotNull(message = "convertible can not be null!")
    private Boolean convertible;

    @DecimalMin(value = "0.0", message = "rating should be >= 0.0")
    @DecimalMax(value = "5.0", message = "rating should be <= 5.0")
    private Float rating;

    private EngineType engineType;

    @NotNull(message = "manufacturerId can not be null!")
    private Long manufacturerId;

    private CarDTO() {
    }

    @Builder
    private CarDTO(
        Long id, String licensePlate, Integer seatCount, Boolean convertible,
        Float rating, EngineType engineType, Long manufacturerId) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.convertible = convertible;
        this.rating = rating;
        this.engineType = engineType;
        this.manufacturerId = manufacturerId;
    }

    @JsonProperty
    public Long getId() {
        return id;
    }
}