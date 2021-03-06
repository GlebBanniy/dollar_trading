package com.dollartrading.trading.service;

public enum Messages {
    ADDED_MESSAGE("Successfully added"),
    UPDATED_MESSAGE("Successfully updated"),
    ADDED_EXCEPTION_MESSAGE("This entity is already exist"),
    INCORRECT_BID_VALUE_MESSAGE("Bid value is too much or too few"),
    NOT_FOUND_MESSAGE("This entity does not exist"),
    ERROR_SAVING_ENTITY("Error saving to the database");

    private final String message;

    Messages(String s) {
        this.message = s;
    }

    public String getMessage() {
        return message;
    }
}
