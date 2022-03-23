package com.varachit.transistance.service;

import com.varachit.transistance.entity.*;
import com.varachit.transistance.repository.StationRepository;
import com.varachit.transistance.util.StationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.*;

@Service
public class StationService {
    private final StationRepository stationRepository;
    private final StationUtils stationUtils;

    @Autowired
    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
        this.stationUtils = new StationUtils();
    }

    // Get all stations
    public List<Station> getStations() {
        return stationRepository.findAll();
    }

    // Get a station with an ID
    public Station getStation(Long id) {
        Optional<Station> stationOptional = stationRepository.findById(id);
        if(stationOptional.isPresent()) {
            return stationOptional.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Station with the specified ID does not exist");
    }

    // Search for the station with required NAME and optional TYPE
    public List<Station> searchStation(String stationName, StationType stationType) {
        List<Station> stations = new ArrayList<>();

        if(Objects.isNull(stationType)) {
            stations = stationRepository.searchStationsByName(stationName);
        } else if(Objects.isNull(stationName) || stationName.length() <= 0) {
            stations = stationRepository.searchStationsByType(stationType);
        } else {
            if(Stream.of(StationType.values()).map(Enum::name)
                     .collect(Collectors.toList())
                     .contains(stationType.toString())) {
                stations = stationRepository.searchStationsByNameAndType(stationName, stationType);
            }
        }

        if(!stations.isEmpty()) {
            return stations;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Station with the specified name and/or type does not exist");
    }

    /*
     * Add station, Station Object is being passed in to the method and validate using StationUtils
     * If validation is completed and passed, the exception will not be thrown from validateStation() method
     * Finally, persist the station into the database through the repository
     */
    public Station addStation(Station station) {
        stationUtils.validateStation(station.getName(), station.getCode(), station.getLatitude(),
                                     station.getLongitude(), station.getType().name());

        Optional<Station> existingStationCode = stationRepository.findByCode(station.getCode());

        if(existingStationCode.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unable to add station due to the specified code already exists");
        }
        return stationRepository.save(station);
    }

    /*
     * Update station, seven parameters are passed into the method
     * Station ID and its object fields are required
     * Each parameter will be validated and re-assign individually
     * Eventually, return the updated station object
     */
    public Station updateStation(Long stationId, String newStationName, String newStationCode, Double newLatitude,
                                 Double newLongitude, StationType newType) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Unable to update station due to the specified station does not exist"));

        // Format validation
        stationUtils.validateStation(newStationName, newStationCode, newLatitude, newLongitude, newType.name());

        // Additional validation is required as the Station Code must be unique
        Optional<Station> stationOptional = stationRepository.findByCode(newStationCode);
        if(stationOptional.isEmpty()) {
            station.setName(newStationName);
            station.setCode(newStationCode);
            station.setLatitude(newLatitude);
            station.setLongitude(newLongitude);
            station.setType(StationType.valueOf(newType.toString()));
            stationRepository.save(station);
            return station;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Unable to update station code due to the specified code already exists");
    }


    // Update specific station status
    public Station updateStationStatus(Long stationId, StationStatus newStatus) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Unable to update station due to the specified station does not exist"));

        stationUtils.validateStatus(newStatus.name());
        if(!Objects.equals(station.getStatus(), newStatus)) {
            station.setStatus(StationStatus.valueOf(newStatus.toString()));
            stationRepository.save(station);
        }
        return station;
    }

    // Delete a station with an ID
    public Station deleteStation(Long stationId) {
        Optional<Station> stationOptional = stationRepository.findById(stationId);
        if(stationOptional.isPresent()) {
            Station stationToDelete = stationOptional.get();
            stationRepository.delete(stationToDelete);
            return stationToDelete; // Return DELETED Station
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                          "Unable to delete station due to the specified station does not exist");
    }
}
