package com.triptaxi.domainobject;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.triptaxi.domainvalue.EngineType;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(
    name = "car",
    uniqueConstraints = @UniqueConstraint(name = "uc_licensePlate", columnNames = {"licensePlate"})
)
@Getter
@ToString
public class CarDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateCreated;

    @Column(nullable = false)
    @NotNull(message = "licensePlate can not be null!")
    private String licensePlate;

    @Setter
    @Min(value = 2, message = "seatCount should be >= 2")
    @Max(value = 7, message = "seatCount should be <= 7")
    @Column(nullable = false)
    @NotNull(message = "seatCount can not be null!")
    private Integer seatCount;

    @Setter
    @Column(nullable = false)
    @NotNull(message = "convertible can not be null!")
    private Boolean convertible;

    @Setter
    @DecimalMin(value = "0.0", message = "rating should be >= 0.0")
    @DecimalMax(value = "5.0", message = "rating should be <= 5.0")
    private Float rating;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EngineType engineType;

    @Setter
    @ToString.Exclude
    @JsonManagedReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private ManufacturerDO manufacturer;

    @JsonBackReference
    @OneToOne(mappedBy = "car", fetch = FetchType.LAZY)
    private DriverDO driver;

    @Setter
    @UpdateTimestamp
    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateCarUpdated = ZonedDateTime.now();

    @Setter
    @Column(nullable = false)
    private Boolean deleted;

    private CarDO() {
    }

    public CarDO(String licensePlate, Integer seatCount, Boolean convertible) {
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.convertible = convertible;
        this.rating = null;
        this.engineType = engineType.UNKNOWN;
        this.deleted = false;
    }
}