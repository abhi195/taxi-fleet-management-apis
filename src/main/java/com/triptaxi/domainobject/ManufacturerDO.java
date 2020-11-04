package com.triptaxi.domainobject;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(
    name = "manufacturer",
    uniqueConstraints = @UniqueConstraint(name = "uc_manufacturerName",
        columnNames = {"manufacturerName"})
)
@Getter
@ToString
@EqualsAndHashCode
public class ManufacturerDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateCreated;

    @Setter
    @Column(nullable = false)
    @NotNull(message = "manufacturerName can not be null!")
    private String manufacturerName;

    @Setter
    @UpdateTimestamp
    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateManufacturerUpdated;

    @Setter
    @Column(nullable = false)
    private Boolean deleted = false;

    @JsonBackReference
    @OneToMany(mappedBy = "manufacturer", fetch = FetchType.LAZY)
    private List<CarDO> cars;

    private ManufacturerDO() {
    }

    public ManufacturerDO(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }
}