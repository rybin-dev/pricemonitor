package com.senla.pricemonitor.dto;

import java.time.LocalDate;

public record PriceAtDate(
        Long min,
        Long max,
        Double avg,
        LocalDate date
) {
}