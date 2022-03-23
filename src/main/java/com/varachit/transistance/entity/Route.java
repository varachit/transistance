package com.varachit.transistance.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "route")
public class Route implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private StationType type;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Station> stations;

    public Route(String name, String description, StationType type, List<Station> stations) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.stations = new LinkedList<>();

        if (stations != null && !stations.isEmpty()) {
            this.stations.addAll(stations);
        }
    }

    public void addStation(Station station) {
        this.stations.add(station);
    }

    public void removeStation(Long stationId) {
        this.stations.removeIf(station -> station.getId().equals(stationId));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) { return false; }
        return id != null && Objects.equals(id, ((Route) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}