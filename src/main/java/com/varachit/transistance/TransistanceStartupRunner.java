package com.varachit.transistance;

import com.varachit.transistance.entity.Route;
import com.varachit.transistance.entity.Station;
import com.varachit.transistance.entity.StationType;
import com.varachit.transistance.repository.RouteRepository;
import com.varachit.transistance.repository.StationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Configuration
public class TransistanceStartupRunner {

    @Bean
    CommandLineRunner commandLineRunner(StationRepository stationRepository, RouteRepository routeRepository) {
        return args -> {

            // Station
            // Station -> (String name, String code, Double latitude, Double longitude, StationType type)

            // Route
            // Route -> (String name, String description, StationType type, List<Station> stations)

            /*
             * BTS Sukhumvit Station and Route
             * Route: Sena Nikhom -> Ratchayothin -> Phahon Yothin -> Ha Yaek Lat Phrao -> Mo Chit -> Saphan Khwai
             *     -> Sena Ruam -> Ari -> Sanam Pao -> Victory Monument -> Phaya Thai -> Ratchathewi -> Siam (CEN)
             *     -> Chit Lom -> Phloen Chit -> Nana -> Asok -> Phrom Phong -> Thong Lo -> Ekkamai -> Phra Khanong
             *     -> On Nut -> Bang Chak -> Punnawithi -> Udom Suk
             */
            Station N12 = new Station("Sena Nikhom", "N12", 13.8363601, 100.5714222, StationType.BTS);
            Station N11 = new Station("Ratchayothin", "N11", 13.8308609, 100.5680845, StationType.BTS);
            Station N10 = new Station("Phahon Yothin", "N10", 13.8142104, 100.5451057, StationType.BTS);
            Station N9 = new Station("Ha Yaek Lat Phrao", "N9", 13.81644, 100.5597613, StationType.BTS);
            Station N8 = new Station("Mo Chit", "N8", 13.7990075, 100.5524655, StationType.BTS);
            Station N7 = new Station("Saphan Khwai", "N7", 13.7919983, 100.5491032, StationType.BTS);
            Station N6 = new Station("Sena Ruam", "N6", 13.7873954, 100.5470566, StationType.BTS);
            Station N5 = new Station("Ari", "N5", 13.7791812, 100.5458335, StationType.BTS);
            Station N4 = new Station("Sanam Pao", "N4", 13.7592238,100.5404675, StationType.BTS);
            Station N3 = new Station("Victory Monument", "N3", 13.7600783, 100.5372488, StationType.BTS);
            Station N2 = new Station("Phaya Thai", "N2", 13.7523875, 100.5342018, StationType.BTS);
            Station N1 = new Station("Ratchathewi", "N1", 13.7474478, 100.5331504, StationType.BTS);

            Station CEN = new Station("Siam", "CEN", 13.7455902, 100.5331048, StationType.BTS);

            Station E1 = new Station("Chit Lom", "E1", 13.7481608, 100.5409774, StationType.BTS);
            Station E2 = new Station("Phloen Chit", "E2", 13.743768,100.5446164, StationType.BTS);
            Station E3 = new Station("Nana", "E3", 13.7408864,100.5492513, StationType.BTS);
            Station E4 = new Station("Asok", "E4", 13.7361653,100.5588965, StationType.BTS);
            Station E5 = new Station("Phrom Phong", "E5", 13.733672,100.5627736, StationType.BTS);
            Station E6 = new Station("Thong Lo", "E6", 13.7274916,100.5653378, StationType.BTS);
            Station E7 = new Station("Ekkamai", "E7", 13.7195079,100.5745646, StationType.BTS);
            Station E8 = new Station("Phra Khanong", "E8",13.7147551,100.5829546, StationType.BTS);
            Station E9 = new Station("On Nut", "E9", 13.7055725,100.5987648, StationType.BTS);
            Station E10 = new Station("Bang Chak", "E10", 13.6973463,100.6041557, StationType.BTS);
            Station E11 = new Station("Punnawithi", "E11", 13.6923533,100.6025356, StationType.BTS);
            Station E12 = new Station("Udom Suk", "E12", 13.6777378,100.5982889, StationType.BTS);

            List<Station> SukhumvitStations = new LinkedList<>(
                    Arrays.asList(N12, N11, N10, N9, N8, N7, N6, N5, N4, N3, N2, N1, CEN,
                            E1, E2, E3, E4, E5, E6, E7, E8, E9, E10, E11, E12));
            // stationRepository.saveAll(SukhumvitStations);

            // BTS Sukhumvit Line Route
            Route Sukhumvit = new Route("Sukhumvit", "BTS Skytrain Sukhumvit Line", StationType.BTS, SukhumvitStations);
            routeRepository.save(Sukhumvit);


            /*
             * MRT Blue Station and Route
             * Route: Sukhumvit -> Queen Sirikit National Convention Centre -> Khlong Toei -> Lumphini -> Si Lom
             */
            Station BL22 = new Station("Sukhumvit", "BL22", 13.7380479,100.5603774, StationType.MRT);
            Station BL23 = new Station("Queen Sirikit National Convention Centre", "BL23", 13.7231519,100.5601019, StationType.MRT);
            Station BL24 = new Station("Khlong Toei", "BL24", 13.7223044,100.5517262, StationType.MRT);
            Station BL25 = new Station("Lumphini", "BL25", 13.7264811,100.5418382, StationType.MRT);
            Station BL26 = new Station("Si Lom", "BL26", 13.7293055,100.5350227, StationType.MRT);
            // stationRepository.saveAll(Arrays.asList(BL22, BL26));

            List<Station> BlueLineStations = new LinkedList<>(Arrays.asList(BL22, BL23, BL24, BL25, BL26));
            Route BlueLine = new Route("Blue Line", "MRT Underground Train Blue Line", StationType.MRT, BlueLineStations);
            routeRepository.save(BlueLine);


            /*
             * MRT Purple Station and Route
             * Route: Tao Poon -> Bang Son
             */
            Station PP16 = new Station("Tao Poon", "PP16", 13.806133,100.5285723, StationType.MRT);
            Station PP15 = new Station("Bang Son", "PP15", 13.806133,100.5285723, StationType.MRT);
            List<Station> PurpleLineStations = new LinkedList<>(Arrays.asList(PP16, PP15));

            Route PurpleLine = new Route("Purple Line", "MRT Skytrain Purple Line", StationType.MRT, PurpleLineStations);
            routeRepository.save(PurpleLine);
        };
    }
}
