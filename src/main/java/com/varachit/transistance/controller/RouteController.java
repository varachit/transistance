package com.varachit.transistance.controller;

import com.varachit.transistance.entity.Route;
import com.varachit.transistance.entity.Station;
import com.varachit.transistance.service.RouteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/route")
public class RouteController {
    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<List<Route>> getRoutes() {
        List<Route> routes = routeService.getRoutes();
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }

    @GetMapping(path = "/{routeId}")
    public ResponseEntity<Route> getRoute(@PathVariable("routeId") Long id) {
        Route route = routeService.getRoute(id);
        return new ResponseEntity<>(route, HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Route> addRoute(@RequestBody Route route) {
        Route addedRoute = routeService.addRoute(route);
        return new ResponseEntity<>(addedRoute, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{routeId}")
    public ResponseEntity<Route> updateRoute(@PathVariable("routeId") Long id, @RequestBody Route newRoute) {
        Route updatedRoute = routeService.updateRoute(id, newRoute);
        return new ResponseEntity<>(updatedRoute, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{routeId}")
    public ResponseEntity<Route> deleteRoute(@PathVariable("routeId") Long id) {
        Route deletedRoute = routeService.deleteRoute(id);
        return new ResponseEntity<>(deletedRoute, HttpStatus.OK);
    }

    @PostMapping(path = "{routeId}/addStation")
    public ResponseEntity<Route> addStationToRoute(@PathVariable("routeId") Long id, @RequestBody Station station) {
        Route updatedRoute = routeService.addStationToRoute(id, station);
        return new ResponseEntity<>(updatedRoute, HttpStatus.OK);
    }

    @DeleteMapping(path = "{routeId}/removeStation")
    public ResponseEntity<Route> removeStationFromRoute(@PathVariable("routeId") Long routeId,
                                                        @RequestParam Long stationId) {
        Route updatedRoute = routeService.deleteStationFromRoute(routeId, stationId);
        return new ResponseEntity<>(updatedRoute, HttpStatus.OK);
    }
}
