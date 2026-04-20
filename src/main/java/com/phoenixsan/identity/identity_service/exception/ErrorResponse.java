package com.phoenixsan.identity.identity_service.exception;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    private int status;
    private String message;
    private String path;
    private LocalDateTime timestamp;
}