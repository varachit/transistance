package com.varachit.transistance.service;

import com.varachit.transistance.entity.Route;
import com.varachit.transistance.entity.Station;
import com.varachit.transistance.entity.StationType;
import com.varachit.transistance.repository.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class RouteServiceTest {
    @Mock
    private RouteRepository routeRepository;
    private RouteService routeService;
    final Logger logger = LoggerFactory.getLogger(RouteServiceTest.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        routeService = new RouteService(routeRepository);
    }

    @Test
    void getRoutes_AssertionSucceeds_IfAllRoutesAreReturnedAndEqualToExpectedRouteList() {
        List<Route> expectedRoutes = getAllRoutesTestcase();
        given(routeRepository.findAll()).willReturn(expectedRoutes);
        List<Route> actualRoutes = routeService.getRoutes();
        assertEquals(expectedRoutes.size(), actualRoutes.size());
        logStatus("getRoutes", expectedRoutes.hashCode(), actualRoutes.hashCode());
    }

    @Test
    void getRoute_AssertionSucceeds_IfARouteWithAnIdIsExistsAndEqualToAnExpectedRoute() {
        Route expectedRoute = getAllRoutesTestcase().get(1);
        given(routeRepository.findById(1L)).willReturn(Optional.of(expectedRoute));
        Route actualRoute = routeService.getRoute(1L);
        assertEquals(expectedRoute.getName(), actualRoute.getName());
        logStatus("getRoute", expectedRoute.hashCode(), actualRoute.hashCode());
    }

    @Test
    void getRoute_ThrowResponseStatusException404_ThenAssertionSucceeds_IfARouteWithAnIdDoesNotExist() {
        given(routeRepository.findById(2L)).willReturn(Optional.empty());
        Exception exception = assertThrows(ResponseStatusException.class, () -> routeService.getRoute(2L));
        String expectedMessage = "404 NOT_FOUND \"Route with the specified ID does not exist\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        logStatus("getRoute", expectedMessage.hashCode(), actualMessage.hashCode());
    }

    @Test
    void addRoute_AssertionSucceeds_IfARouteAddedSuccessfullyAndIsEqualToAnExpectedRoute() {
        Route expectedRoute = Route.builder()
                .name("Gold Line")
                .description("Gold Line from Krung Thon Buri to Khlong San")
                .type(StationType.BTS)
                .stations(new LinkedList<>())
                .build();

        given(routeRepository.save(expectedRoute)).willReturn(expectedRoute);
        Route actualRoute = routeService.addRoute(expectedRoute);
        assertEquals(expectedRoute, actualRoute);
        logStatus("addRoute", expectedRoute.hashCode(), actualRoute.hashCode());
    }

    @Test
    void addRoute_ThrowResponseStatusException400_ThenAssertionSucceeds_IfARouteWithAnIdIsExists() {
        Route SilomLine = Route.builder()
                .name("Silom Line")
                .description("Silom Line from National Stadium to Siam")
                .type(StationType.BTS)
                .stations(new LinkedList<>())
                .build();

        given(routeRepository.findByName(SilomLine.getName())).willReturn(Optional.of(SilomLine));
        Exception exception = assertThrows(ResponseStatusException.class, () -> routeService.addRoute(SilomLine));
        String expectedMessage = "400 BAD_REQUEST \"Unable to add route due to the specified name already exists\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        logStatus("addRoute", expectedMessage.hashCode(), actualMessage.hashCode());
    }

    @Test
    void updateRoute_AssertionSucceeds_IfARouteUpdatedSuccessfullyAndIsEqualToAnExpectedRoute() {
        Route originalRoute = Route.builder()
                .name("Airport Link") //
                .description("The line provides an airport rail link from or to Suvarnabhumi Airport")
                .type(StationType.SRL) // should be ARL (Airport Rail Link) instead of SRL (State Railway Line)
                .stations(new LinkedList<>())  // is empty, require updating
                .build();

        Station A1 = Station.builder()
                .name("Suvarnabhumi")
                .code("A1")
                .latitude(13.6980426)
                .longitude(100.7410555)
                .type(StationType.ARL)
                .build();

        Route expectedRoute = Route.builder()
                .name("Airport Rail Link")
                .description("Railways from or to Suvarnabhumi Airport")
                .type(StationType.ARL)
                .stations(new LinkedList<>(List.of(A1)))
                .build();

        given(routeRepository.findById(3L)).willReturn(Optional.of(originalRoute));
        Route existingRouteId = routeService.getRoute(3L);
        assertEquals(originalRoute, existingRouteId);

        Route updatedRoute = routeService.updateRoute(
                3L, new Route("Airport Rail Link", "Railways from or to Suvarnabhumi Airport",
                        StationType.ARL, new LinkedList<>(List.of(A1))));
        assertEquals(expectedRoute.getName(), updatedRoute.getName());
        logStatus("updateRoute", expectedRoute.hashCode(), updatedRoute.hashCode());
    }

    @Test
    void deleteRoute_AssertionSucceeds_IfARouteDeletedSuccessfully() {
        Route BlueLine = Route.builder()
                .name("Blue Line")
                .description("MRT Railways from Lak Song from or to Tha Phra")
                .type(StationType.MRT)
                .stations(new LinkedList<>())
                .build();

        given(routeRepository.findById(4L)).willReturn(Optional.of(BlueLine));
        Route existingRouteId = routeService.getRoute(4L);
        assertEquals(BlueLine, existingRouteId);

        Route deletedRoute = routeService.deleteRoute(4L);
        assertEquals(BlueLine.getName(), deletedRoute.getName());
        logStatus("deleteRoute", BlueLine.hashCode(), deletedRoute.hashCode());
    }

    @Test
    void deleteRoute_ThrowResponseStatusException404_ThenAssertionSucceeds_IfARouteToDeleteWithIdDoesNotExist() {
        given(routeRepository.findById(5L)).willReturn(Optional.empty());
        Exception exception = assertThrows(ResponseStatusException.class, () -> routeService.deleteRoute(5L));
        String expectedMessage = "404 NOT_FOUND \"Unable to delete route due to the specified route does not exist\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        logStatus("deleteRoute", actualMessage.hashCode(), expectedMessage.hashCode());
    }

    @Test
    void addStationToRoute_AssertionSucceeds_IfAStationAddedToRouteSuccessfullyAndRouteIsEqualToAnExpectedRoute() {
        Station PP01 = Station.builder()
                .name("Khlong Bang Phai")
                .code("PP01")
                .latitude(13.8934687)
                .longitude(100.4070681)
                .type(StationType.MRT)
                .build();

        Route expectedRoute = Route.builder()
                .name("Purple Line")
                .description("MRT Railways from Tao Poon to Khlong Bang Phai")
                .type(StationType.MRT)
                .stations(new LinkedList<>(List.of(PP01)))
                .build();

        Route PurpleLine = Route.builder()
                .name("Purple Line")
                .description("MRT Railways from Tao Poon to Khlong Bang Phai")
                .type(StationType.MRT)
                .stations(new LinkedList<>())
                .build();

        given(routeRepository.findById(6L)).willReturn(Optional.of(PurpleLine));
        Route updatedStation = routeService.addStationToRoute(6L, PP01);
        assertEquals(1, updatedStation.getStations().size());
        assertEquals(PP01.getName(), updatedStation.getStations().get(0).getName());
        logStatus("addStationToRoute", expectedRoute.hashCode(), updatedStation.hashCode());
    }

    @Test
    void addStationToRoute_ThrowResponseStatusException404_ThenAssertionSucceeds_IfARouteWithIdDoesNotExist() {
        Station PP02 = Station.builder()
                .name("Talad Bang Yai")
                .code("PP02")
                .latitude(13.8807592)
                .longitude(100.4019657)
                .type(StationType.MRT)
                .build();

        given(routeRepository.findById(7L)).willReturn(Optional.empty());
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> routeService.addStationToRoute(7L, PP02));
        String expectedMessage = "404 NOT_FOUND \"Unable to add station to route due to the specified route does not exists\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        logStatus("addStationToRoute", expectedMessage.hashCode(), actualMessage.hashCode());
    }

    @Test
    void addStationToRoute_ThrowResponseStatusException400_ThenAssertionSucceeds_IfAStationWithCodeIsExistsInARoute() {
        Station G2 = Station.builder()
                .name("Charoen Nakhon")
                .code("G2")
                .latitude(13.7264832)
                .longitude(100.5068433)
                .type(StationType.BTS)
                .build();

        Route GoldLine = Route.builder()
                .name("Gold Line")
                .description("BTS Railways from Krung Thon Buri to Klong San")
                .type(StationType.BTS)
                .stations(new LinkedList<>(List.of(G2)))
                .build();

        given(routeRepository.findById(8L)).willReturn(Optional.of(GoldLine));
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> routeService.addStationToRoute(8L, G2));
        String expectedMessage = "400 BAD_REQUEST \"Unable to add station to route due to the specified route already exists\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        logStatus("addStationToRoute", expectedMessage.hashCode(), actualMessage.hashCode());
    }

    @Test
    void deleteStationFromRoute_AssertionSucceeds_IfAStationDeletedFromARouteSuccessfully() {
        Route expectedRoute = Route.builder()
                .name("Silom Line")
                .description("BTS Railways from Bangwa to Pho Nimit")
                .type(StationType.BTS)
                .stations(new LinkedList<>(List.of()))
                .build();

        Station S9 = Station.builder()
                .id(1L)
                .name("Pho Nimit")
                .code("S9")
                .latitude(13.7166273)
                .longitude(100.48849)
                .type(StationType.BTS)
                .build();

        Route SilomLine = Route.builder()
                .name("Silom Line")
                .description("BTS Railways from Bangwa to Pho Nimit")
                .type(StationType.BTS)
                .stations(new LinkedList<>(List.of(S9)))
                .build();

        given(routeRepository.findById(9L)).willReturn(Optional.of(SilomLine));
        Route updatedRoute = routeService.deleteStationFromRoute(9L, S9.getId());
        assertEquals(0, updatedRoute.getStations().size());
        assertEquals(expectedRoute.getStations().size(), updatedRoute.getStations().size());
        logStatus("deleteStationFromRoute", expectedRoute.hashCode(), updatedRoute.hashCode());
    }

    @Test
    void deleteStationFromRoute_ThrowResponseStatusException404_ThenAssertionSucceeds_IfARouteWithIdDoesNotExist() {
        given(routeRepository.findById(10L)).willReturn(Optional.empty());
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> routeService.deleteStationFromRoute(10L, 1L));
        String expectedMessage = "404 NOT_FOUND \"Unable to delete station from route due to the specified route does not exists\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        logStatus("deleteStationFromRoute", expectedMessage.hashCode(), actualMessage.hashCode());
    }

    @Test
    void deleteStationFromRoute_ThrowResponseStatusException404_ThenAssertionSucceeds_IfAStationWithIdInARouteDoesNotExist() {
        Route SukhumvitLine = Route.builder()
                .name("Sukhumvit Line")
                .description("BTS Railways from Siam to Khu Khot")
                .type(StationType.BTS)
                .stations(new LinkedList<>())
                .build();

        given(routeRepository.findById(11L)).willReturn(Optional.of(SukhumvitLine));
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> routeService.deleteStationFromRoute(11L, 1L));
        String expectedMessage = "404 NOT_FOUND \"Unable to delete station from route due to the specified station does not exists\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        logStatus("deleteStationFromRoute", expectedMessage.hashCode(), actualMessage.hashCode());
    }

    void logStatus(String methodName, int expectedHashCode, int actualHashCode) {
        if(expectedHashCode == actualHashCode) {
            logger.info(methodName + " : PASSED");
        } else {
            logger.error(methodName + " : FAILED");
        }
    }

    List<Route> getAllRoutesTestcase() {
        return Arrays.asList(
                new Route("Sukhumvit", "BTS Skytrain Sukhumvit Line", StationType.BTS, new LinkedList<>(
                        Arrays.asList(
                            new Station("Sena Nikhom", "N12", 13.8363601, 100.5714222, StationType.BTS),
                            new Station("Ratchayothin", "N11", 13.8308609, 100.5680845, StationType.BTS),
                            new Station("Phahon Yothin", "N10", 13.8142104, 100.5451057, StationType.BTS),
                            new Station("Ha Yaek Lat Phrao", "N9", 13.81644, 100.5597613, StationType.BTS),
                            new Station("Mo Chit", "N8", 13.7990075, 100.5524655, StationType.BTS),
                            new Station("Saphan Khwai", "N7", 13.7919983, 100.5491032, StationType.BTS),
                            new Station("Sena Ruam", "N6", 13.7873954, 100.5470566, StationType.BTS),
                            new Station("Ari", "N5", 13.7791812, 100.5458335, StationType.BTS),
                            new Station("Sanam Pao", "N4", 13.7592238,100.5404675, StationType.BTS),
                            new Station("Victory Monument", "N3", 13.7600783, 100.5372488, StationType.BTS),
                            new Station("Phaya Thai", "N2", 13.7523875, 100.5342018, StationType.BTS),
                            new Station("Ratchathewi", "N1", 13.7474478, 100.5331504, StationType.BTS),
                            new Station("Siam", "CEN", 13.7455902, 100.5331048, StationType.BTS),
                            new Station("Chit Lom", "E1", 13.7481608, 100.5409774, StationType.BTS),
                            new Station("Phloen Chit", "E2", 13.743768,100.5446164, StationType.BTS),
                            new Station("Nana", "E3", 13.7408864,100.5492513, StationType.BTS),
                            new Station("Asok", "E4", 13.7361653,100.5588965, StationType.BTS),
                            new Station("Phrom Phong", "E5", 13.733672,100.5627736, StationType.BTS),
                            new Station("Thong Lo", "E6", 13.7274916,100.5653378, StationType.BTS),
                            new Station("Ekkamai", "E7", 13.7195079,100.5745646, StationType.BTS),
                            new Station("Phra Khanong", "E8",13.7147551,100.5829546, StationType.BTS),
                            new Station("On Nut", "E9", 13.7055725,100.5987648, StationType.BTS),
                            new Station("Bang Chak", "E10", 13.6973463,100.6041557, StationType.BTS),
                            new Station("Punnawithi", "E11", 13.6923533,100.6025356, StationType.BTS),
                            new Station("Udom Suk", "E12", 13.6777378,100.5982889, StationType.BTS)
                        )
                )),

                new Route("Blue Line", "MRT Underground Train Blue Line", StationType.MRT, new LinkedList<>(
                        Arrays.asList(
                            new Station("Sukhumvit", "BL22", 13.7380479,100.5603774, StationType.MRT),
                            new Station("Queen Sirikit National Convention Centre", "BL23", 13.7231519,100.5601019, StationType.MRT),
                            new Station("Khlong Toei", "BL24", 13.7223044,100.5517262, StationType.MRT),
                            new Station("Lumphini", "BL25", 13.7264811,100.5418382, StationType.MRT),
                            new Station("Si Lom", "BL26", 13.7293055,100.5350227, StationType.MRT)
                        )
                )),

                new Route("Purple Line", "MRT Skytrain Purple Line", StationType.MRT, new LinkedList<>(
                        Arrays.asList(
                            new Station("Tao Poon", "PP16", 13.806133,100.5285723, StationType.MRT),
                            new Station("Bang Son", "PP15", 13.806133,100.5285723, StationType.MRT)
                        )
                ))
        );
    }


}