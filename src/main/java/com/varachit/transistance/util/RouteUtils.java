package com.varachit.transistance.util;

import com.varachit.transistance.entity.Station;
import com.varachit.transistance.entity.StationType;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.regex.Pattern.compile;

public class RouteUtils {
    private final Pattern namePattern = compile("[a-zA-Z0-9 ]*");

    // The validation method for route using parameters
    public void validateRoute(String routeName, String description, String type, List<Station> stations) {

        if(routeName == null || routeName.length() <= 0 || !namePattern.matcher(routeName).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid name");
        }

        if(description == null || description.length() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid description");
        }

        if(type == null || !Stream.of(StationType.values()).map(Enum::name)
                .collect(Collectors.toList()).contains(type)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid station type");
        }

        if(stations == null || !stations.getClass().getSimpleName().contains("List")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid station list type");
        }
    }

    public void validateName(String routeName) {
        if(routeName == null || routeName.length() <= 0 || !namePattern.matcher(routeName).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid name");
        }
    }

    public void validateDescription(String description) {
        if(description == null || description.length() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid description");
        }
    }

    public void validateStationType(String type) {
        if(type == null || !Stream.of(StationType.values()).map(Enum::name)
                .collect(Collectors.toList()).contains(type)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid station type");
        }
    }

    public void validateStationListType(List<Station> stations) {
        if(stations == null || !stations.getClass().getTypeName().contains("List")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid station list type");
        }
    }

}
