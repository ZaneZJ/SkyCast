package com.skycast.exception;

import java.rmi.ServerException;

public class CityAlreadyExistsException extends ServerException {

    public CityAlreadyExistsException(String message) {
        super(message);
    }

}
