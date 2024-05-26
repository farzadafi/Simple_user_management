package ir.farzadafi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AddressSaveRequest(
        @NotNull(message = "province can't be null")
        @Schema(example = "1")
        Integer provinceId,

        @NotNull(message = "county can't be null")
        @Schema(example = "2")
        Integer countyId,

        @NotNull(message = "city can't be null")
        @Schema(example = "3")
        Integer cityId,

        String street) {
}
