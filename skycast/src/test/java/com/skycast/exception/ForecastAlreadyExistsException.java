package com.skycast.exception;

import java.rmi.ServerException;

public class ForecastAlreadyExistsException extends ServerException {

    public ForecastAlreadyExistsException(String message) {
        super(message);
    }

}
