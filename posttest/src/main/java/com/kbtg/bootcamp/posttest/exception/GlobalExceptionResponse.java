package com.kbtg.bootcamp.posttest.exception;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GlobalExceptionResponse {

    private LocalDateTime timestamp;

    private int status;

    private String error;

    private String message;

}
