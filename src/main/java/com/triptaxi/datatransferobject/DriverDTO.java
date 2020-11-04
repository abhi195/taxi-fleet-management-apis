package com.triptaxi.datatransferobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.triptaxi.domainvalue.GeoCoordinate;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverDTO {

    @JsonIgnore
    private Long id;

    @NotNull(message = "Username can not be null!")
    private String username;

    @NotNull(message = "Password can not be null!")
    private String password;

    private GeoCoordinate coordinate;

    private DriverDTO() {
    }

    @Builder
    private DriverDTO(Long id, String username, String password, GeoCoordinate coordinate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.coordinate = coordinate;
    }

    @JsonProperty
    public Long getId() {
        return id;
    }
}