package com.kbtg.bootcamp.posttest.exception;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BadRequestExceptionResponse {

    private LocalDateTime timestamp;

    private int status;

    private String error;

    private List<String> messages;

}
