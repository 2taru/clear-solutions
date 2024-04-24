package com.clearsolutions.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorObject {

    private Integer statusCode;
    private String message;
    private LocalDateTime timestamp;
}