package com.skycast.exception;

import java.rmi.ServerException;

public class WeatherAlreadyExistsException extends ServerException {

    public WeatherAlreadyExistsException(String message) {
        super(message);
    }

}
