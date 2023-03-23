package com.vinicius.bosh.utils;

public enum Status {

    ENABLED("ENABLED"),
    DISABLED("DISABLED");

    private final String eventStatus;

    Status(final String eventStatus){
        this.eventStatus = eventStatus;
    }

    public String getEventStatus() {
        return eventStatus;
    }
}
