package com.skycast.exception;

import java.rmi.ServerException;

public class ForecastNotFoundException extends ServerException {

    public ForecastNotFoundException(String message) {
        super(message);
    }

}
