package com.varachit.transistance.util;

import com.varachit.transistance.entity.Route;
import com.varachit.transistance.entity.Station;
import com.varachit.transistance.entity.StationType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RouteUtilsTest {
    RouteUtils routeUtils = new RouteUtils();
    final Logger logger = LoggerFactory.getLogger(RouteUtilsTest.class);

    /*
     * RouteUtilsTest - To test the validity of the properties value within the Route Object
     * Route Objects are created throughout the test for the better readability. However, in the case of enum,
     * raw value will be passed instead of calling getter method from the object class due to the problem
     * that the enumeration type must be correctly assigned in order to be successfully instantiate the object
     */

    @Test()
    void validateRoute_DoesNotThrowException_IfAllPropertyInRouteIsValid() {
        Route ARLLine = new Route("Airport Rail Link", "ARL Airport Rail Link", StationType.ARL, List.of());
        assertDoesNotThrow(() -> routeUtils.validateRoute(ARLLine.getName(), ARLLine.getDescription(),
                ARLLine.getType().name(), ARLLine.getStations()));
    }

    @Test
    void validateRoute_ThrowResponseStatusException400_ThenAssertionSucceeds_IfRouteNameIsInvalid() {
        Route SilomLine = new Route("Silom_Line", "BTS Silom Line", StationType.BTS, List.of());
        Exception exception = assertThrows(ResponseStatusException.class, () -> routeUtils.validateRoute(
                SilomLine.getName(), SilomLine.getDescription(), SilomLine.getType().name(), SilomLine.getStations()));

        String expectedMessage = "400 BAD_REQUEST \"Invalid name\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateRoute_ThrowResponseStatusException400_ThenAssertionSucceeds_IfRouteDescriptionIsInvalid() {
        Route SukhumvitLine = new Route("Sukhumvit Line", "", StationType.BTS, List.of());
        Exception exception = assertThrows(ResponseStatusException.class, () -> routeUtils.validateRoute(
                SukhumvitLine.getName(), SukhumvitLine.getDescription(),
                SukhumvitLine.getType().name(), SukhumvitLine.getStations()));

        String expectedMessage = "400 BAD_REQUEST \"Invalid description\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateRoute_ThrowResponseStatusException400_ThenAssertionSucceeds_IfRouteTypeIsInvalid() {
        // Avoid calling type getter from GoldLine (Route class)
        Route GoldLine = new Route("Gold Line", "BTS Gold Line", null, List.of());
        // Refer to raw value in routeType variable instead
        String routeType = "DLR";
        Exception exception = assertThrows(ResponseStatusException.class, () -> routeUtils.validateRoute(
                GoldLine.getName(), GoldLine.getDescription(), routeType, GoldLine.getStations()));

        String expectedMessage = "400 BAD_REQUEST \"Invalid station type\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateRoute_ThrowResponseStatusException400_ThenAssertionSucceeds_IfRouteStationListIsInvalid() {
        // Route stations list is ignored and passed in the raw value to the validator instead
        Route BlueLine = new Route("Blue Line", "MRT Blue Line", StationType.MRT, null);
        Exception exception = assertThrows(ResponseStatusException.class, () -> routeUtils.validateRoute(
                BlueLine.getName(), BlueLine.getDescription(), BlueLine.getType().name(), null));

        String expectedMessage = "400 BAD_REQUEST \"Invalid station list type\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateName_DoesNotThrowException_IfRouteNameIsValid() {
        String routeName = "Siam";
        assertDoesNotThrow(() -> routeUtils.validateName(routeName));
    }

    @Test
    void validateName_ThrowResponseStatusException400_ThenAssertionSucceeds_IfRouteNameIsInvalid() {
        String routeName = "Ari!";
        Exception exception = assertThrows(ResponseStatusException.class, () -> routeUtils.validateName(routeName));
        String expectedMessage = "400 BAD_REQUEST \"Invalid name\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateDescription_DoesNotThrowException_IfRouteDescriptionIsValid() {
        String routeDescription = "MRT Purple Line originated from Tao Poon to Khlong Bang Phai";
        assertDoesNotThrow(() -> routeUtils.validateDescription(routeDescription));
    }

    @Test
    void validateDescription_ThrowResponseStatusException400_ThenAssertionSucceeds_IfRouteDescriptionIsInvalid() {
        String routeDescription = "";
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> routeUtils.validateDescription(routeDescription));
        String expectedMessage = "400 BAD_REQUEST \"Invalid description\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateStationType_DoesNotThrowException_IfRouteStationTypeIsValid() {
        String routeStationType = "BTS";
        assertDoesNotThrow(() -> routeUtils.validateStationType(routeStationType));
    }

    @Test
    void validateStationType_ResponseStatusException400_ThenAssertionSucceeds_IfRouteStationTypeIsInvalid() {
        String routeStationType = "DLR";
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> routeUtils.validateStationType(routeStationType));
        String expectedMessage = "400 BAD_REQUEST \"Invalid station type\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateStationListType_DoesNotThrowException_IfRouteStationListTypeIsValid() {
        Station N24 = new Station("Khu Khot", "N24", 13.9549146,100.5992026, StationType.BTS);
        assertDoesNotThrow(() -> routeUtils.validateStationListType(List.of(N24)));
    }

    @Test
    void validateStationListType_ResponseStatusException400_ThenAssertionSucceeds_IfRouteStationListTypeIsInvalid() {
        Vector<Station> BusLine = new Vector<>();
        Exception exception = assertThrows(ResponseStatusException.class, () -> routeUtils.validateStationListType(BusLine));
        String expectedMessage = "400 BAD_REQUEST \"Invalid station list type\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}