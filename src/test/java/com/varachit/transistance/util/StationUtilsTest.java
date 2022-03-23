package com.varachit.transistance.util;

import com.varachit.transistance.entity.Station;
import com.varachit.transistance.entity.StationType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class StationUtilsTest {
    StationUtils stationUtils = new StationUtils();
    final Logger logger = LoggerFactory.getLogger(StationUtilsTest.class);

    /*
     * StationUtilsTest - To test the validity of the properties value within the Station Object
     * Station Objects are created throughout the test for the better readability. However, in the case of enum,
     * raw value will be passed instead of calling getter method from the object class due to the problem
     * that the enumeration type must be correctly assigned in order to be successfully instantiate the object
     */

    @Test
    void validateStation_DoesNotThrowException_IfAllPropertyInStationIsValid() {
        Station E14 = new Station("Bearing", "E14", 13.6612072,100.6019066, StationType.BTS);
        assertDoesNotThrow(() -> stationUtils.validateStation(
                E14.getName(), E14.getCode(), E14.getLatitude(), E14.getLongitude(), E14.getType().name()));
    }

    @Test
    void validateStation_ThrowResponseStatusException400_ThenAssertionSucceeds_IfStationNameIsInvalid() {
        Station W1 = new Station("National_Stadium", "W1", 13.746473,100.529073, StationType.BTS);
        Exception exception = assertThrows(ResponseStatusException.class, () -> stationUtils.validateStation(
                W1.getName(), W1.getCode(), W1.getLatitude(), W1.getLongitude(), W1.getType().name()));
        String expectedMessage = "400 BAD_REQUEST \"Invalid station name\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateStation_ThrowResponseStatusException400_ThenAssertionSucceeds_IfStationCodeIsInvalid() {
        Station S6 = new Station("Saphan Taksin", "S6+", 13.7183636,100.5140366, StationType.BTS);
        Exception exception = assertThrows(ResponseStatusException.class, () -> stationUtils.validateStation(
                S6.getName(), S6.getCode(), S6.getLatitude(), S6.getLongitude(), S6.getType().name()));
        String expectedMessage = "400 BAD_REQUEST \"Invalid station code\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateStation_ThrowResponseStatusException400_ThenAssertionSucceeds_IfStationLatitudeIsInvalid() {
        Station G3 = new Station("Khlong San", "G3", 113.7751273,100.5430585, StationType.BTS);
        Exception exception = assertThrows(ResponseStatusException.class, () -> stationUtils.validateStation(
                G3.getName(), G3.getCode(), G3.getLatitude(), G3.getLongitude(), G3.getType().name()));
        String expectedMessage = "400 BAD_REQUEST \"Invalid station latitude coordinate\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateStation_ThrowResponseStatusException400_ThenAssertionSucceeds_IfStationLongitudeIsInvalid() {
        Station PP11 = new Station("Nonthaburi Civic Center", "PP11", 13.8601476,1100.5132409, StationType.MRT);
        Exception exception = assertThrows(ResponseStatusException.class, () -> stationUtils.validateStation(
                PP11.getName(), PP11.getCode(), PP11.getLatitude(), PP11.getLongitude(), PP11.getType().name()));
        String expectedMessage = "400 BAD_REQUEST \"Invalid station longitude coordinate\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateStation_ThrowResponseStatusException400_ThenAssertionSucceeds_IfStationTypeIsInvalid() {
        // type property within this BL13 will be avoided due to impossibility of inputting invalid value
        Station BL13 = new Station("Mo Chit", "BL13", 13.80257,100.55378, null);
        // To test enum type, it's mandatory to pass in raw value instead of calling getter from the BL13 object
        String stationType = "DLR";
        Exception exception = assertThrows(ResponseStatusException.class, () -> stationUtils.validateStation(
                BL13.getName(), BL13.getCode(), BL13.getLatitude(), BL13.getLongitude(), stationType));
        String expectedMessage = "400 BAD_REQUEST \"Invalid station type\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateName_DoesNotThrowException_IfStationNameIsValid() {
        String stationName = "Bang Sue Grand Station";
        assertDoesNotThrow(() -> stationUtils.validateName(stationName));
    }

    @Test
    void validateName_ThrowResponseStatusException400_IfStationNameIsInvalid() {
        String stationName = "Bang Son Railway Station!";
        Exception exception = assertThrows(ResponseStatusException.class, () -> stationUtils.validateName(stationName));
        String expectedMessage = "400 BAD_REQUEST \"Invalid station name\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateCode_DoesNotThrowException_IfStationCodeIsValid() {
        String stationCode = "CEN"; // CEN is Station Code for BTS Station Siam
        assertDoesNotThrow(() -> stationUtils.validateCode(stationCode));
    }

    @Test
    void validateCode_ThrowResponseStatusException400_IfStationCodeIsInvalid() {
        String stationCode = "@BL38"; // BL38 is Station Code for MRT Lak Song
        Exception exception = assertThrows(ResponseStatusException.class, () -> stationUtils.validateCode(stationCode));
        String expectedMessage = "400 BAD_REQUEST \"Invalid station code\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateLatitude_DoesNotThrowException_IfStationLatitudeIsValid() {
        Double stationLatitude = 13.7563;
        assertDoesNotThrow(() -> stationUtils.validateLatitude(stationLatitude));
    }

    @Test
    void validateLatitude_ThrowResponseStatusException400_IfStationLatitudeIsInvalid() {
        Double stationLatitude = 91.2697;
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> stationUtils.validateLatitude(stationLatitude));
        String expectedMessage = "400 BAD_REQUEST \"Invalid station latitude coordinate\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateLongitude_DoesNotThrowException_IfStationLongitudeIsValid() {
        Double stationLongitude = 100.5018;
        assertDoesNotThrow(() -> stationUtils.validateLongitude(stationLongitude));
    }

    @Test
    void validateLongitude_ThrowResponseStatusException400_IfStationLongitudeIsInvalid() {
        Double stationLongitude = -228.9203;
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> stationUtils.validateLongitude(stationLongitude));
        String expectedMessage = "400 BAD_REQUEST \"Invalid station longitude coordinate\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateType_DoesNotThrowException_IfStationTypeIsValid() {
        String stationType = "BTS";
        assertDoesNotThrow(() -> stationUtils.validateType(stationType));
    }

    @Test
    void validateType_ThrowResponseStatusException400_IfStationTypeIsInvalid() {
        String stationType = "DLR";
        Exception exception = assertThrows(ResponseStatusException.class, () -> stationUtils.validateType(stationType));
        String expectedMessage = "400 BAD_REQUEST \"Invalid station type\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateStatus_DoesNotThrowException_IfStationStatusIsValid() {
        String stationStatus = "OPENED";
        assertDoesNotThrow(() -> stationUtils.validateStatus(stationStatus));
    }

    @Test
    void validateStatus_ThrowResponseStatusException400_IfStationStatusIsInvalid() {
        String stationStatus = "DESTROYED";
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> stationUtils.validateStatus(stationStatus));
        String expectedMessage = "400 BAD_REQUEST \"Invalid station status\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}