package com.varachit.transistance.controller;

import com.varachit.transistance.entity.*;
import com.varachit.transistance.service.StationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @Slf4j
@RequestMapping("api/v1/station")
public class StationController {
    private final StationService stationService;

    @Autowired
    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping
    public ResponseEntity<List<Station>> getStations() {
        List<Station> stations = stationService.getStations();
        return new ResponseEntity<>(stations, HttpStatus.OK);
    }

    @GetMapping(path = "/{stationId}")
    public ResponseEntity<Station> getStation(@PathVariable("stationId") Long id) {
        Station station = stationService.getStation(id);
        return new ResponseEntity<>(station, HttpStatus.OK);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<Iterable<Station>> searchStation(@RequestParam(required = false) String stationName,
                                                           @RequestParam(required = false) StationType stationType) {
        Iterable<Station> stations = stationService.searchStation(stationName, stationType);
        return new ResponseEntity<>(stations, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Station> addStation(@RequestBody Station station) {
        Station addedStation = stationService.addStation(station);
        return new ResponseEntity<>(addedStation, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{stationId}")
    public ResponseEntity<Station> updateStation(@PathVariable("stationId") Long stationId,
                                                 @RequestBody Station station) {
        Station updatedStation = stationService.updateStation(stationId, station.getName(), station.getCode(),
                station.getLatitude(), station.getLongitude(), station.getType());
        return new ResponseEntity<>(updatedStation, HttpStatus.OK);
    }

    @PutMapping(path = "/status/{stationId}")
    public ResponseEntity<Station> updateStationStatus(@PathVariable("stationId") Long stationId,
                                                       @RequestParam StationStatus newStatus) {
        Station updatedStatusStation = stationService.updateStationStatus(stationId, newStatus);
        return new ResponseEntity<>(updatedStatusStation, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{stationId}")
    public ResponseEntity<Station> deleteStation(@PathVariable("stationId") Long stationId) {
        Station deletedStatus = stationService.deleteStation(stationId);
        return new ResponseEntity<>(deletedStatus, HttpStatus.OK);
    }

}
