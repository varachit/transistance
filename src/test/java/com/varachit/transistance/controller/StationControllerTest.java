package com.varachit.transistance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.varachit.transistance.entity.Station;
import com.varachit.transistance.entity.StationStatus;
import com.varachit.transistance.entity.StationType;
import com.varachit.transistance.repository.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StationRepository stationRepository;
    final Logger logger = LoggerFactory.getLogger(StationControllerTest.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getStationsTest_ShouldReturnAllStations_ThenAssertionSucceeds() throws Exception {
        // GET : getStations - /api/v1/station
        String endpoint = "/api/v1/station";

        List<Station> stationList = new LinkedList<>();
        stationList.add(new Station("Tao Poon", "PP16", 13.806133,100.5285723, StationType.MRT));
        stationList.add(new Station("Bang Son", "PP15", 13.806133,100.5285723, StationType.MRT));

        Mockito.when(stationRepository.findAll()).thenReturn(stationList);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(endpoint))
                .andDo(print()).andExpect(status().isOk());
        MvcResult mvcResult = resultActions.andReturn();

        String expectedJsonResponse = objectMapper.writeValueAsString(stationList);
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
    }

    @Test
    void getStationTest_ShouldReturnAStation_ThenAssertionSucceeds_IfAStationIsExists() throws Exception {
        // GET : getStation - /api/v1/station/{stationId}
        String endpoint = "/api/v1/station/";

        Station N19 = Station.builder()
                .id(1L)
                .name("Sai Yud")
                .code("N19")
                .latitude(13.8885181).longitude(100.6019377)
                .type(StationType.BTS)
                .build();

        Mockito.when(stationRepository.findById(1L)).thenReturn(Optional.of(N19));
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(endpoint + 1))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Sai Yud"));
        MvcResult mvcResult = resultActions.andReturn();

        String expectedJsonResponse = objectMapper.writeValueAsString(N19);
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
    }

    @Test
    void searchStationTest_ShouldReturnAStationOrListOfStationThatMatchWithNameAndTypeIfExists() throws Exception {
        // GET : searchStation - /api/v1/station/search
        String endpoint = "/api/v1/station/search";

        Station CEN = Station.builder()
                .id(2L)
                .name("Siam")
                .code("CEN")
                .latitude(13.7455902).longitude(100.5331048)
                .type(StationType.BTS)
                .build();

        Mockito.when(stationRepository.searchStationsByNameAndType("Siam", StationType.BTS))
                .thenReturn(List.of(CEN));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(endpoint)
                .param("stationName", "Siam")
                .param("stationType", "BTS"))
                .andDo(print()).andExpect(status().isOk());
        MvcResult mvcResult = resultActions.andReturn();

        String expectedJsonResponse = objectMapper.writeValueAsString(List.of(CEN));
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
    }

    @Test
    void searchStationTest_ShouldReturnAStationOrListOfStationThatMatchWithNameIfExists() throws Exception {
        // GET : searchStation - /api/v1/station/search
        String endpoint = "/api/v1/station/search";

        Station N22 = Station.builder()
                .name("Royal Thai Air Force Museum")
                .code("N22")
                .latitude(13.917945).longitude(100.6195263)
                .type(StationType.BTS)
                .build();

        Mockito.when(stationRepository.searchStationsByName("Royal Thai Air Force Museum")).thenReturn(List.of(N22));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(endpoint)
                .param("stationName", "Royal Thai Air Force Museum"))
                .andDo(print()).andExpect(status().isOk());
        MvcResult mvcResult = resultActions.andReturn();

        String expectedJsonResponse = objectMapper.writeValueAsString(List.of(N22));
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
    }

    @Test
    void searchStationTest_ShouldReturnAStationOrListOfStationThatMatchWithTypeIfExists() throws Exception {
        // GET : searchStation - /api/v1/station/search
        String endpoint = "/api/v1/station/search";

        Station BL20 = Station.builder()
                .name("Phra Ram 9")
                .code("BL20")
                .latitude(13.7578458).longitude(100.5631891)
                .type(StationType.MRT)
                .build();

        Mockito.when(stationRepository.searchStationsByType(StationType.MRT)).thenReturn(List.of(BL20));
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(endpoint)
                .param("stationType", String.valueOf(StationType.MRT)))
                .andDo(print()).andExpect(status().isOk());
        MvcResult mvcResult = resultActions.andReturn();

        String expectedJsonResponse = objectMapper.writeValueAsString(List.of(BL20));
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
    }

    @Test
    void addStationTest_ShouldAddAStation_ThenAssertionSucceeds() throws Exception {
        // POST : addStation - /api/v1/station
        String endpoint = "/api/v1/station/";

        Station BL06 = Station.builder()
                .id(3L)
                .name("Sirindhorn")
                .code("BL06")
                .latitude(13.7840083).longitude(100.4934932)
                .type(StationType.MRT)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                .content(objectMapper.writeValueAsString(BL06))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated());

        Mockito.when(stationRepository.findById(3L)).thenReturn(Optional.of(BL06));
        mockMvc.perform(MockMvcRequestBuilders.get(endpoint + 3L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Sirindhorn"))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    void updateStationTest_ShouldUpdateAStation_IfAStationIsExists() throws Exception {
        // PUT : updateStation - /api/v1/station/{stationId}
        String endpoint = "/api/v1/station/";

        Station BL27 = Station.builder()
                .id(4L)
                .name("SamYan")
                .code("BL1") // Should be BL27
                .latitude(13.7299531).longitude(100.5194617)
                .type(StationType.MRT)
                .build();

        Mockito.when(stationRepository.findById(4L)).thenReturn(Optional.of(BL27));

        Station newBL27 = Station.builder()
                .id(4L)
                .name("Sam Yan")
                .code("BL27")
                .latitude(13.7299531).longitude(100.5194617)
                .type(StationType.MRT)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put(endpoint + 4L)
                .content(objectMapper.writeValueAsString(newBL27))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(endpoint + 4L))
                .andDo(print()).andExpect(status().isOk());
        MvcResult mvcResult = resultActions.andReturn();

        String expectedJsonResponse = objectMapper.writeValueAsString(newBL27);
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
    }

    @Test
    void updateStationTest_StatusShouldUpdateAStationStatus_IfAStationIsExistAndStatusIsValid() throws Exception {
        // PUT : updateStation - /api/v1/station/{stationId}
        String endpoint = "/api/v1/station/status/";

        Station N3 = Station.builder()
                .id(5L)
                .name("Victory Monument")
                .code("N3")
                .latitude(13.762793).longitude(100.537085)
                .type(StationType.BTS)
                .status(StationStatus.OPENED)
                .build();

        Station expectedStation = Station.builder()
                .id(5L)
                .name("Victory Monument")
                .code("N3")
                .latitude(13.762793).longitude(100.537085)
                .type(StationType.BTS)
                .status(StationStatus.CLOSED)
                .build();

        Mockito.when(stationRepository.findById(5L)).thenReturn(Optional.of(N3));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put(endpoint + 5L)
                .param("newStatus", "CLOSED"))
                .andDo(print()).andExpect(status().isOk());
        MvcResult mvcResult = resultActions.andReturn();

        String expectedJsonResponse = objectMapper.writeValueAsString(expectedStation);
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
    }

    @Test
    void deleteStationTest_ShouldDeleteAStation_ThenAssertionSucceeds_IfAStationIsExists() throws Exception {
        // DELETE : /api/v1/station/{stationId}
        String endpoint = "/api/v1/station/";

        Station N14 = Station.builder()
                .id(6L)
                .name("Royal Forest Department")
                .code("N14")
                .latitude(13.8503668).latitude(100.5796393)
                .type(StationType.BTS)
                .build();

        Mockito.when(stationRepository.findById(6L)).thenReturn(Optional.of(N14));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete(endpoint + 6L))
                .andDo(print()).andExpect(status().isOk());
        MvcResult mvcResult = resultActions.andReturn();

        String expectedJsonResponse = objectMapper.writeValueAsString(N14);
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
    }
}