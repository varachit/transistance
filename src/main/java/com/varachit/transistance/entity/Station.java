package com.varachit.transistance.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "station")
public class Station implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    private Double latitude;
    private Double longitude;
    private StationType type;
    private StationStatus status;
    @Transient
    private boolean isInterchangeable;

    public Station(String name, String code, Double latitude, Double longitude, StationType type) {
        this.name = name;
        this.code = code;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.status = StationStatus.valueOf("OPENED");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) { return false; }
        return id != null && Objects.equals(id, ((Station) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}