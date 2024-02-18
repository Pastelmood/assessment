package com.kbtg.bootcamp.posttest.exception;

public class StatusInternalServerErrorException extends RuntimeException {

    public StatusInternalServerErrorException() {
        super();
    }

    public StatusInternalServerErrorException(String message) {
        super(message);
    }

}
