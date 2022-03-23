package com.varachit.transistance.repository;

import com.varachit.transistance.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Station> findByCode(String code);

    List<Station> searchStationsByName(String name);
    List<Station> searchStationsByType(StationType type);
    List<Station> searchStationsByNameAndType(String name, StationType type);
}
