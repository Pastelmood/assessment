package com.kbtg.bootcamp.posttest.exception;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class BadRequestExceptionResponse {

    private LocalDateTime timestamp;

    private int status;

    private String error;

    private List<String> messages;
}
