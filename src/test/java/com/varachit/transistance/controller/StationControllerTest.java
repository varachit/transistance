package com.varachit.transistance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.varachit.transistance.entity.Station;
import com.varachit.transistance.entity.StationType;
import com.varachit.transistance.repository.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
    void getStationsShouldReturnAllStationsFromService() throws Exception {
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
    void getStation() throws Exception {
        // GET : getStation - /api/v1/station/{stationId}
        String endpoint = "/api/v1/station/1";

        Station N19 = Station.builder()
                .id(1L)
                .name("Sai Yud")
                .code("N19")
                .latitude(13.8885181).longitude(100.6019377)
                .type(StationType.BTS)
                .build();

        Mockito.when(stationRepository.findById(1L)).thenReturn(Optional.of(N19));
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(endpoint))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Sai Yud"));
    }

    @Test
    void searchStation() {
    }

    @Test
    void addStation() {
    }

    @Test
    void updateStation() {
    }

    @Test
    void updateStationStatus() {
    }

    @Test
    void deleteStation() {
    }
}