package com.varachit.transistance.service;

import com.varachit.transistance.entity.Station;
import com.varachit.transistance.entity.StationStatus;
import com.varachit.transistance.entity.StationType;
import com.varachit.transistance.repository.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class StationServiceTest {
    @Mock
    private StationRepository stationRepository;
    private StationService stationService;
    final Logger logger = LoggerFactory.getLogger(StationServiceTest.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stationService = new StationService(stationRepository);
    }

    @Test
    @Order(1)
    void getStations_AssertionSucceeds_IfAllStationsAreReturnedAndEqualToExpectedStationList() {
        List<Station> expectedStations = getAllStationsTestcase(); // 30 Stations
        given(stationRepository.findAll()).willReturn(expectedStations);
        List<Station> actualStations = stationService.getStations();
        assertEquals(expectedStations.size(), actualStations.size());
        logStatus("getStations", expectedStations.hashCode(), actualStations.hashCode());
    }

    @Test
    @Order(2)
    void getStation_AssertionSucceeds_IfAStationWithAnIdIsExistsAndEqualToAnExpectedStation() {
        Station expectedStation = getAllStationsTestcase().get(0); // Get 13th index of station from testcase
        given(stationRepository.findById(1L)).willReturn(Optional.ofNullable(expectedStation));
        Station actualStation = stationService.getStation(1L);
        assert expectedStation != null;
        assertEquals(expectedStation.getName(), actualStation.getName());
        logStatus("getStation", expectedStation.hashCode(), actualStation.hashCode());
    }

    @Test
    @Order(3)
    void getStation_ThrowResponseStatusException404_ThenAssertionSucceeds_IfAStationWithAnIdDoesNotExist() {
        given(stationRepository.findById(2L)).willReturn(Optional.ofNullable(getAllStationsTestcase().get(1)));
        Exception exception = assertThrows(ResponseStatusException.class, () -> stationService.getStation(3L));
        String expectedMessage = "404 NOT_FOUND \"Station with the specified ID does not exist\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        logStatus("getStation", expectedMessage.hashCode(), actualMessage.hashCode());
    }

    @Test
    @Order(4)
    void searchStation_AssertionSucceeds_IfAStationWithNameAndTypeIsExistsAndEqualToAnExpectedStation() {
        Station expectedStation = Station.builder()
                .name("Siam")
                .code("CEN")
                .latitude(13.7455902).longitude(100.5331048)
                .type(StationType.BTS)
                .build();

        List<Station> expectedStations = new ArrayList<>();
        expectedStations.add(expectedStation);

        given(stationRepository.searchStationsByNameAndType("Siam", StationType.BTS)).willReturn(expectedStations);
        List<Station> actualStations = stationService.searchStation("Siam", StationType.BTS);
        assertEquals(expectedStations.get(0).getName(), actualStations.get(0).getName());
        logStatus("searchStation", expectedStations.hashCode(), actualStations.hashCode());
    }

    @Test
    @Order(5)
    void searchStation_AssertionSucceeds_IfAStationWithNameIsExistsAndEqualToAnExpectedStation() {
        Station expectedStation = Station.builder()
                .name("Ari")
                .code("N5")
                .latitude(13.7791812)
                .longitude(100.5458335)
                .type(StationType.BTS)
                .build();

        List<Station> expectedStations = new ArrayList<>();
        expectedStations.add(expectedStation);

        given(stationRepository.searchStationsByName("Ari")).willReturn(expectedStations);
        List<Station> actualStations = stationService.searchStation("Ari", null);
        assertEquals(expectedStations.get(0).getName(), actualStations.get(0).getName());
        logStatus("searchStation", expectedStations.hashCode(), actualStations.hashCode());
    }

    @Test
    @Order(6)
    void searchStation_AssertionSucceeds_IfAStationWithTypeIsExistsAndEqualToAnExpectedStation() {
        Station expectedStation = Station.builder()
                .name("Sukhumvit")
                .code("BL22")
                .latitude(13.7380479)
                .longitude(100.5603774)
                .type(StationType.MRT)
                .build();

        List<Station> expectedStations = new ArrayList<>();
        expectedStations.add(expectedStation);

        given(stationRepository.searchStationsByType(StationType.MRT)).willReturn(expectedStations);
        List<Station> actualStations = stationService.searchStation("", StationType.MRT);
        assertEquals(expectedStations.get(0).getName(), actualStations.get(0).getName());
        logStatus("searchStation", expectedStations.hashCode(), actualStations.hashCode());
    }

    @Test
    @Order(7)
    void searchStation_ThrowResponseStatusException404_ThenAssertionSucceeds_IfStationWithNameAndTypeDoesNotExist() {
        given(stationRepository.searchStationsByNameAndType("Salaya", StationType.BTS))
                .willReturn(new ArrayList<>());

        Exception exception = assertThrows(ResponseStatusException.class,
                () -> stationService.searchStation("Salaya", StationType.BTS));
        String expectedMessage = "404 NOT_FOUND \"Station with the specified name and/or type does not exist\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        logStatus("searchStation", expectedMessage.hashCode(), actualMessage.hashCode());
    }

    @Test
    @Order(8)
    void addStation_AssertionSucceeds_IfAStationAddedSuccessfullyAndIsEqualToAnExpectedStation() {
        Station expectedStation = Station.builder()
                .name("Sena Nikhom")
                .code("N12")
                .latitude(13.8363601).longitude(100.5714222)
                .type(StationType.BTS)
                .build();

        given(stationRepository.save(expectedStation)).willReturn(expectedStation);
        Station actualStation = stationService.addStation(expectedStation);
        assertEquals(expectedStation.getName(), actualStation.getName());
        logStatus("addStation", expectedStation.hashCode(), actualStation.hashCode());
    }

    @Test
    @Order(9)
    void addStation_ThrowResponseStatusException400_ThenAssertionSucceeds_IfAStationWithCodeIsExists() {
        Station HuaLamphong = Station.builder()
                .name("Hua Lamphong")
                .code("BL28")
                .latitude(13.737532).longitude(100.5174358)
                .type(StationType.MRT)
                .build();

        given(stationRepository.findByCode(HuaLamphong.getCode())).willReturn(Optional.of(HuaLamphong));
        Exception exception = assertThrows(ResponseStatusException.class, () -> stationService.addStation(HuaLamphong));
        String expectedMessage = "400 BAD_REQUEST \"Unable to add station due to the specified code already exists\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        logStatus("addStation", expectedMessage.hashCode(), actualMessage.hashCode());
    }

    @Test
    @Order(10)
    void updateStation_AssertionSucceeds_IfAStationUpdatedSuccessfullyAndIsEqualToAnExpectedStation() {
        Station originalStation = Station.builder()
                .name("TaoPoon") // Bad format, require updating
                .code("BL11") // Wrong station code must be BL10, require updating
                .latitude(13.8061278).longitude(100.530761)
                .type(StationType.BTS) // Wrong station type must be MRT instead, require updating
                .build();

        Station expectedStation = Station.builder()
                .name("Tao Poon")
                .code("BL10")
                .latitude(13.8061278).longitude(100.530761)
                .type(StationType.MRT)
                .build();

        given(stationRepository.findById(3L)).willReturn(Optional.ofNullable(originalStation));
        Station existingStationId = stationService.getStation(3L);
        assertEquals(originalStation, existingStationId);

        given(stationRepository.findById(3L)).willReturn(Optional.ofNullable(originalStation));
        Station updatedStation = stationService.updateStation(
                3L, "Tao Poon", "BL10",
                existingStationId.getLatitude(), existingStationId.getLongitude(), StationType.MRT);
        assertEquals(expectedStation.getName(), updatedStation.getName());
        logStatus("updateStation", expectedStation.hashCode(), updatedStation.hashCode());
    }

    @Test
    @Order(11)
    void updateStation_ThrowResponseStatusException400_ThenAssertionSucceeds_IfStationWithCodeIsExists() {
        Station S2 = Station.builder()
                .name("Sala Daeng")
                .code("S2")
                .latitude(13.7285329).longitude(100.532207)
                .type(StationType.BTS).build();

        given(stationRepository.findById(3L)).willReturn(Optional.of(S2));
        Station existingStationId = stationService.getStation(3L);
        assertEquals(S2, existingStationId);

        Station stationWithCodeCEN = Station.builder()
                .name("Siam")
                .code("CEN")
                .latitude(13.7455902).longitude(100.5331048)
                .type(StationType.BTS)
                .build();

        given(stationRepository.findByCode("CEN")).willReturn(Optional.of(stationWithCodeCEN));
        Exception exception = assertThrows(ResponseStatusException.class, () -> stationService.updateStation(
                3L, S2.getName(), "CEN", S2.getLatitude(), S2.getLongitude(), S2.getType()));
        String expectedMessage = "400 BAD_REQUEST \"Unable to update station code due to the specified code already exists\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        logStatus("updateStation", expectedMessage.hashCode(), actualMessage.hashCode());
    }

    @Test
    @Order(12)
    void updateStationStatus_AssertionSucceeds_IfAStationStatusUpdatedSuccessfullyAndIsEqualToAnExpectedStationStatus() {
        Station expectedStation = Station.builder()
                .name("Sutthisan")
                .code("BL17")
                .latitude(13.7523363).longitude(100.5427035)
                .type(StationType.MRT)
                .build();

        expectedStation.setStatus(StationStatus.CLOSED); // Status value before update
        given(stationRepository.findById(4L)).willReturn(Optional.of(expectedStation));

        Station updatedStation = stationService.updateStationStatus(4L, StationStatus.UNDER_CONSTRUCTION);
        assertEquals(StationStatus.UNDER_CONSTRUCTION, updatedStation.getStatus());
        logStatus("updateStationStatus",
                StationStatus.UNDER_CONSTRUCTION.hashCode(), updatedStation.getStatus().hashCode());
    }

    @Test
    @Order(13)
    void deleteStation_AssertionSucceeds_IfAStationDeletedSuccessfully() {
        Station expectedStation = Station.builder()
                .name("Lat Krabang")
                .code("A2")
                .latitude(13.7278286).longitude(100.7451841)
                .type(StationType.ARL)
                .build();

        given(stationRepository.findById(5L)).willReturn(Optional.ofNullable(expectedStation));

        // If the deletion is completed, the deleted object would be returned
        // Otherwise, ResponseStatusException 404 will be thrown
        Station deletedStation = stationService.deleteStation(5L);

        assert expectedStation != null;
        assertEquals(expectedStation.getName(), deletedStation.getName());
        logStatus("deleteStation", expectedStation.hashCode(), deletedStation.hashCode());
    }

    @Test
    @Order(14)
    void deleteStation_ThrowResponseStatusException_ThenAssertionSucceeds_IfAStationToDeleteWithIdDoesNotExist() {
        given(stationRepository.findById(6L)).willReturn(Optional.empty());
        Exception exception = assertThrows(ResponseStatusException.class, () -> stationService.deleteStation(6L));
        String expectedMessage = "404 NOT_FOUND \"Unable to delete station due to the specified station does not exist\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        logStatus("deleteStation", expectedMessage.hashCode(), actualMessage.hashCode());
    }

    void logStatus(String methodName, int expectedHashCode, int actualHashCode) {
        if(expectedHashCode == actualHashCode) {
            logger.info(methodName + " : PASSED");
        } else {
            logger.error(methodName + " : FAILED");
        }
    }

    List<Station> getAllStationsTestcase() {
        return Arrays.asList(
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
            new Station("Udom Suk", "E12", 13.6777378,100.5982889, StationType.BTS),
            new Station("Sukhumvit", "BL22", 13.7380479,100.5603774, StationType.MRT),
            new Station("Queen Sirikit National Convention Centre", "BL23", 13.7231519,100.5601019, StationType.MRT),
            new Station("Khlong Toei", "BL24", 13.7223044,100.5517262, StationType.MRT),
            new Station("Lumphini", "BL25", 13.7264811,100.5418382, StationType.MRT),
            new Station("Si Lom", "BL26", 13.7293055,100.5350227, StationType.MRT)
        );
    }
}