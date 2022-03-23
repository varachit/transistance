package com.varachit.transistance.service;

import com.varachit.transistance.entity.Route;
import com.varachit.transistance.entity.Station;
import com.varachit.transistance.entity.StationType;
import com.varachit.transistance.repository.RouteRepository;
import com.varachit.transistance.repository.StationRepository;
import com.varachit.transistance.util.RouteUtils;
import com.varachit.transistance.util.StationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RouteService {
    private final RouteRepository routeRepository;
    private final RouteUtils routeUtils;
    private final StationUtils stationUtils;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
        this.routeUtils = new RouteUtils();
        this.stationUtils = new StationUtils();
    }

    // Get all stations
    public List<Route> getRoutes() {
        return routeRepository.findAll();
    }

    // Get a station with an ID
    public Route getRoute(Long routeId) {
        Optional<Route> routeOptional = routeRepository.findById(routeId);
        if(routeOptional.isPresent()) {
            return routeOptional.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route with the specified ID does not exist");
    }

    public Route addRoute(Route route) {
        routeUtils.validateRoute(route.getName(), route.getDescription(), route.getType().name(), route.getStations());

        Optional<Route> existingRouteName = routeRepository.findByName(route.getName());
        if(existingRouteName.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unable to add route due to the specified name already exists");
        }
        return routeRepository.save(route);
    }

    public Route updateRoute(Long routeId, Route newRoute) {
        routeUtils.validateRoute(newRoute.getName(), newRoute.getDescription(),
                                 newRoute.getType().name(), newRoute.getStations());

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Unable to update route due to the specified route does not exists"));

        if(!Objects.equals(route.getName(), newRoute.getName())) {
            route.setName(newRoute.getName());
        }

        if(!Objects.equals(route.getDescription(), newRoute.getDescription())) {
            route.setDescription(newRoute.getDescription());
        }

        if(!Objects.equals(route.getType(), newRoute.getType())) {
            route.setType(StationType.valueOf(newRoute.getType().toString()));
        }

        if(!Objects.equals(route.getStations(), newRoute.getStations())) {
            route.setStations(newRoute.getStations());
        }
        routeRepository.save(route);
        return route;
    }

    public Route deleteRoute(Long routeId) {
        Optional<Route> routeOptional = routeRepository.findById(routeId);
        if(routeOptional.isPresent()) {
            Route routeToDelete = routeOptional.get();
            routeRepository.delete(routeToDelete);
            return routeToDelete;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Unable to delete route due to the specified route does not exist");
    }

    public Route addStationToRoute(Long routeId, Station station) {
        stationUtils.validateStation(station.getName(), station.getCode(), station.getLatitude(),
                station.getLongitude(), station.getType().name());

        Route existingRoute = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Unable to add station to route due to the specified route does not exists"));

        if(existingRoute.getStations().stream().map(Station::getCode).anyMatch(station.getCode()::equals)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unable to add station to route due to the specified route already exists");
        }
        existingRoute.addStation(station);
        routeRepository.save(existingRoute);
        return existingRoute;
    }

    public Route deleteStationFromRoute(Long routeId, Long stationId) {
        Route existingRoute = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Unable to delete station from route due to the specified route does not exists"));

        if(stationId != null && existingRoute.getStations().stream().map(Station::getId).anyMatch(stationId::equals)) {
            existingRoute.removeStation(stationId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Unable to delete station from route due to the specified station does not exists");
        }
        routeRepository.save(existingRoute);
        return existingRoute;
    }
}
