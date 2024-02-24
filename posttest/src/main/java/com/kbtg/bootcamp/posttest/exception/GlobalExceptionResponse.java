package com.kbtg.bootcamp.posttest.exception;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class GlobalExceptionResponse {

    private LocalDateTime timestamp;

    private int status;

    private String error;

    private String message;
}
