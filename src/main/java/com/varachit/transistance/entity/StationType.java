package com.varachit.transistance.entity;

import java.util.stream.Stream;

public enum StationType {
    /*
        BTS : Bangkok Sky Train
        MRT : Metropolitan Rapid Transit
        ARL : Airport Rail Link
        SRL : State Railway Line
    */
    BTS,
    MRT,
    ARL,
    SRL;

    public String toString() {
        switch(this) {
            case BTS:
                return "BTS";
            case MRT:
                return "MRT";
            case ARL:
                return "ARL";
            case SRL:
                return "SRL";
            default:
                return "UND";
        }
    }

    public static Stream<StationType> stream() {
        return Stream.of(StationType.values());
    }
}
