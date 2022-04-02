package com.varachit.transistance.repository;

import com.varachit.transistance.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
    @Query("SELECT s FROM Station s")
    Optional<Station> findByCode(String code);

    @Query("SELECT s FROM Station s WHERE s.name = ?1")
    List<Station> searchStationsByName(String name);

    @Query("SELECT s FROM Station s WHERE s.type = ?1")
    List<Station> searchStationsByType(StationType type);

    @Query("SELECT s FROM Station s WHERE s.name = ?1 AND s.type = ?2")
    List<Station> searchStationsByNameAndType(String name, StationType type);
}
