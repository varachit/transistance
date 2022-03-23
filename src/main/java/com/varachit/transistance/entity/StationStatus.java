package com.varachit.transistance.entity;

import java.util.stream.Stream;

public enum StationStatus {
    UNDER_CONSTRUCTION,
    OPENED,
    CLOSED;

    public String toString() {
        switch(this) {
            case UNDER_CONSTRUCTION:
                return "UNDER_CONSTRUCTION";
            case OPENED:
                return "OPENED";
            case CLOSED:
                return "CLOSED";
            default:
                return "UND";
        }
    }

    public static Stream<StationStatus> stream() {
        return Stream.of(StationStatus.values());
    }
}
