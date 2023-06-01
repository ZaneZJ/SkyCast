package com.skycast.exception;

import java.rmi.ServerException;

public class CityNotFoundException extends ServerException {

    public CityNotFoundException(String message) {
        super(message);
    }
    public CityNotFoundException() {
        super("City not found!");
    }

}
