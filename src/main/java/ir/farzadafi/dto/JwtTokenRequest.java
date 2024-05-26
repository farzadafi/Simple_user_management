package ir.farzadafi.dto;

import jakarta.validation.constraints.NotNull;

public record JwtTokenRequest(
        @NotNull
        String nationalCode,
        @NotNull
        String password) {
}
