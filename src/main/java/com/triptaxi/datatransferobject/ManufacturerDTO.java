package com.triptaxi.datatransferobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManufacturerDTO {

    @JsonIgnore
    private Long id;

    @NotNull(message = "manufacturerName can not be null!")
    private String manufacturerName;

    private ManufacturerDTO() {
    }

    @Builder
    private ManufacturerDTO(Long id, String manufacturerName) {
        this.id = id;
        this.manufacturerName = manufacturerName;
    }

    @JsonProperty
    public Long getId() {
        return id;
    }
}