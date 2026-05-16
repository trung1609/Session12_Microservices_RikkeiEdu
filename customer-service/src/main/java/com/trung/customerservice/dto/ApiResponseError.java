package com.trung.customerservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseError {
    private String message;
    private int status;
    private Object error;
    private LocalDateTime timestamp;
}
