package com.skycast.exception;

import java.rmi.ServerException;

public class WeatherNotFoundException extends ServerException {

    public WeatherNotFoundException(String message) {
        super(message);
    }

}
