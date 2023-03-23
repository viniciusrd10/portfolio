package com.vinicius.bosh.utils;

public enum ResponseMessage {
    EVENT_NOT_FOUND("There is no event with this id!"),
    SUBSCRIPTION_NOT_FOUND("There is no subscription with this id!"),
    EVENT_NOT_ENABLED("The event with ID '%s' is not enabled."),
    INTERNAL_ERROR_SERVER("An unexpected error occurred."),
    SUBSCRIPTION_WITH_SAME_EVENTID("This subscription is already register with this eventId."),
    EVENT_MUST_TO_BE_INFORMED("Event not informed"), EVENT_ALREADY_EXIST("This event was already created with eventId: %s");

    private final String message;

    ResponseMessage(final String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
