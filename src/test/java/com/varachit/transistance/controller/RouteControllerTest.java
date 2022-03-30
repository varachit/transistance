package com.varachit.transistance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.varachit.transistance.entity.Route;
import com.varachit.transistance.entity.Station;
import com.varachit.transistance.entity.StationStatus;
import com.varachit.transistance.entity.StationType;
import com.varachit.transistance.repository.RouteRepository;
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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RouteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RouteRepository routeRepository;
    final Logger logger = LoggerFactory.getLogger(RouteControllerTest.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRoutesTest_ShouldReturnAllRoutes_ThenAssertionSucceeds() throws Exception {
        // GET : getRoutes - /api/v1/route
        String URI = "/api/v1/route";

        Route SukhumvitLineNorth = Route.builder()
                .id(1L)
                .name("North Sukhumvit Line")
                .description("BTS Sukhumvit Line from Siam to Khu Khot")
                .type(StationType.BTS)
                .stations(new LinkedList<>())
                .build();

        Route SukhumvitLineSouth = Route.builder()
                .id(2L)
                .name("South Sukhumvit Line")
                .description("BTS Sukhumvit Line from Siam to Kheha")
                .type(StationType.BTS)
                .stations(new LinkedList<>())
                .build();

        Mockito.when(routeRepository.findAll()).thenReturn(Arrays.asList(SukhumvitLineNorth, SukhumvitLineSouth));
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URI))
                .andDo(print()).andExpect(status().isOk());
        MvcResult mvcResult = resultActions.andReturn();

        String expectedJsonResponse = objectMapper.writeValueAsString(Arrays.asList(SukhumvitLineNorth, SukhumvitLineSouth));
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
    }

    @Test
    void getRouteTest_ShouldReturnARoute_ThenAssertionSucceeds_IfARouteWithIdIsExists() throws Exception {
        // GET : getRoute - /api/v1/route/{routeId}
        long routeId = 3L;
        String URI = "/api/v1/route/" + routeId;

        Route SilomLine = Route.builder()
                .id(3L)
                .name("Silom Line")
                .description("BTS Silom Line from Siam to Bang Wa")
                .type(StationType.BTS)
                .stations(new LinkedList<>())
                .build();

        Mockito.when(routeRepository.findById(3L)).thenReturn(Optional.of(SilomLine));
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URI))
                .andDo(print()).andExpect(status().isOk());
        MvcResult mvcResult = resultActions.andReturn();

        String expectedJsonResponse = objectMapper.writeValueAsString(SilomLine);
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
    }

    @Test
    void addRouteTest_AssertionSucceeds_IfARouteDoesNotAlreadyExist() throws Exception {
        // POST : addRoute - /api/v1/route
        String URI = "/api/v1/route";

        Route GoldLine = Route.builder()
                .id(4L)
                .name("Gold Line")
                .description("BTS Gold Line from Krung Thon Buri to Khlong San")
                .type(StationType.BTS)
                .stations(new LinkedList<>())
                .build();

        Mockito.when(routeRepository.save(GoldLine)).thenReturn(GoldLine);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(URI)
                .content(objectMapper.writeValueAsString(GoldLine))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated());
        MvcResult mvcResult = resultActions.andReturn();

        String expectedJsonResponse = objectMapper.writeValueAsString(GoldLine);
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
    }

    @Test
    void updateRouteTest_AssertionSucceeds_IfARouteIsExists() throws Exception {
        // PUT : updateRoute - /api/v1/route/{routeId}
        long routeId = 5L;
        String URI = "/api/v1/route/" + routeId;

        Station PP09 = Station.builder()
                .id(1L)
                .name("Yaek Nonthaburi 1")
                .code("PP09")
                .latitude(13.8659187).longitude(100.4920588)
                .type(StationType.MRT)
                .status(StationStatus.OPENED)
                .build();

        Station PP10 = Station.builder()
                .id(2L)
                .name("Bang Krasor")
                .code("PP10")
                .latitude(13.861694).longitude(100.5022396)
                .type(StationType.MRT)
                .status(StationStatus.OPENED)
                .build();

        Route PurpleLine = Route.builder()
                .id(5L)
                .name("MRT Purple Line")
                .description("MRT Purple Line from Tao Poon to Khlong Bang Phai")
                .type(StationType.BTS)
                .stations(new LinkedList<>())
                .build();

        Route expectedRoute = Route.builder()
                .id(5L)
                .name("MRT Purple Line")
                .description("MRT Purple Line from Tao Poon to Khlong Bang Phai")
                .type(StationType.MRT)
                .stations(new LinkedList<>(Arrays.asList(PP09, PP10)))
                .build();

        Mockito.when(routeRepository.findById(5L)).thenReturn(Optional.of(PurpleLine));
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put(URI)
                .content(objectMapper.writeValueAsString(expectedRoute))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
        MvcResult mvcResult = resultActions.andReturn();

        String expectedJsonResponse = objectMapper.writeValueAsString(expectedRoute);
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
    }

    @Test
    void deleteRouteTest_AssertionSucceeds_IfARouteIsExists() throws Exception {
        // DELETE : deleteRoute - /api/v1/route/{routeId}
        long routeId = 6L;
        String URI = "/api/v1/route/" + routeId;

        Route OrangeLine = Route.builder()
                .id(6L)
                .name("MRT Future Orange Line")
                .description("MRT Future Orange Line from Bang Khun Non to Yaek Rom Klao")
                .type(StationType.MRT)
                .stations(new LinkedList<>())
                .build();

        Mockito.when(routeRepository.findById(6L)).thenReturn(Optional.of(OrangeLine));
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete(URI))
                .andDo(print()).andExpect(status().isOk());
        MvcResult mvcResult = resultActions.andReturn();

        String expectedJsonResponse = objectMapper.writeValueAsString(OrangeLine);
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
    }

    @Test
    void addStationToRouteTest_AssertionSucceeds_IfStationInRouteDoesNotAlreadyExist() throws Exception {
        // POST : addStationToRoute - /api/v1/route/{routeId}/addStation
        long routeId = 7L;
        String URI = "/api/v1/route/" + routeId + "/addStation";

        Route OrangeLine = Route.builder()
                .id(7L)
                .name("MRT Future Orange Line")
                .description("MRT Future Orange Line from Bang Khun Non to Yaek Rom Klao")
                .type(StationType.MRT)
                .stations(new LinkedList<>())
                .build();

        Station stationToAdd = Station.builder()
                .id(1L)
                .name("Democracy Monument")
                .code("OR04")
                .latitude(13.7567046).longitude(100.5018897)
                .type(StationType.MRT)
                .status(StationStatus.UNDER_CONSTRUCTION)
                .build();

        Route expectedLine = Route.builder()
                .id(7L)
                .name("MRT Future Orange Line")
                .description("MRT Future Orange Line from Bang Khun Non to Yaek Rom Klao")
                .type(StationType.MRT)
                .stations(new LinkedList<>(List.of(stationToAdd)))
                .build();


        Mockito.when(routeRepository.findById(7L)).thenReturn(Optional.of(OrangeLine));
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(URI)
                .content(objectMapper.writeValueAsString(stationToAdd))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
        MvcResult mvcResult = resultActions.andReturn();

        String expectedJsonResponse = objectMapper.writeValueAsString(expectedLine);
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
    }

    @Test
    void removeStationFromRouteTest_AssertionSucceeds_IfAStationIsExistsInARoute() throws Exception {
        // DELETE : removeStationFromRoute - /api/v1/route/{routeId}/removeStation
        long routeId = 7L;
        String URI = "/api/v1/route/" + routeId + "/removeStation";

        Station OR08 = Station.builder()
                .id(1L)
                .name("Pratunam")
                .code("OR08")
                .latitude(13.752485).longitude(100.5375262)
                .type(StationType.MRT)
                .status(StationStatus.UNDER_CONSTRUCTION)
                .build();

        Route OrangeLine = Route.builder()
                .id(7L)
                .name("MRT Future Orange Line")
                .description("MRT Future Orange Line from Bang Khun Non to Yaek Rom Klao")
                .type(StationType.MRT)
                .stations(new LinkedList<>(List.of(OR08)))
                .build();

        Route expectedLine = Route.builder()
                .id(7L)
                .name("MRT Future Orange Line")
                .description("MRT Future Orange Line from Bang Khun Non to Yaek Rom Klao")
                .type(StationType.MRT)
                .stations(new LinkedList<>())
                .build();

        Mockito.when(routeRepository.findById(7L)).thenReturn(Optional.of(OrangeLine));
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete(URI)
                .param("stationId", "1"))
                .andDo(print()).andExpect(status().isOk());
        MvcResult mvcResult = resultActions.andReturn();

        String expectedJsonResponse = objectMapper.writeValueAsString(expectedLine);
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
    }
}