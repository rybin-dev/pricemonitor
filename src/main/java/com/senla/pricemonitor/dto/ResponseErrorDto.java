package com.senla.pricemonitor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseErrorDto {
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
