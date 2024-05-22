package ir.farzadafi.dto;

import java.time.LocalDateTime;

public record ErrorDetails(LocalDateTime localDateTime,
                           String message) {
}