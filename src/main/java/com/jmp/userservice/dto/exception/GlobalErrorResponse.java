package com.jmp.userservice.dto.exception;

import java.sql.Timestamp;

public record GlobalErrorResponse(
        Timestamp timestamp, Integer status, String error, String message
) {
}
