package com.varachit.transistance.util;

import com.varachit.transistance.entity.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.regex.Pattern.compile;

public class StationUtils {
    private final Pattern namePattern = compile("[a-zA-Z0-9 ]*");
    private final Pattern codePattern = compile("[A-Z0-9]*");

    // The validation method for station using parameters
    public void validateStation(String stationName, String stationCode, Double latitude,
                                Double longitude, String type) {

        if(stationName == null || stationName.length() <= 0 || !namePattern.matcher(stationName).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid station name");
        }

        if(stationCode == null || stationCode.length() <= 0 || !codePattern.matcher(stationCode).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid station code");
        }

        if(latitude == null || (latitude < -90 || latitude > 90)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid station latitude coordinate");
        }

        if(longitude == null || (longitude < -180 || longitude > 180)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid station longitude coordinate");
        }

        if(type == null || !Stream.of(StationType.values()).map(Enum::name)
                .collect(Collectors.toList()).contains(type)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid station type");
        }
    }

    public void validateName(String name) {
        if(name == null || name.length() <= 0 || !namePattern.matcher(name).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid station name");
        }
    }

    public void validateCode(String code) {
        if(code == null || code.length() <= 0 || !codePattern.matcher(code).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid station code");
        }
    }

    // The validation method for latitude (range between -90 to 90)
    public void validateLatitude(Double latitude) {
        if (latitude < -90 || latitude > 90) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid station latitude coordinate");
        }
    }

    // The validation method for longitude (range between -180 to 180)
    public void validateLongitude(Double longitude) {
        if (longitude < -180 || longitude > 180) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid station longitude coordinate");
        }
    }

    public void validateType(String type) {
        if(type == null || !Stream.of(StationType.values()).map(Enum::name)
                .collect(Collectors.toList()).contains(type)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid station type");
        }
    }

    public void validateStatus(String status) {
        if(status == null || !Stream.of(StationStatus.values()).map(Enum::name)
                .collect(Collectors.toList()).contains(status.toString())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid station status");
        }
    }
}
