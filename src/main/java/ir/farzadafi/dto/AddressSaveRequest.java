package ir.farzadafi.dto;

import jakarta.validation.constraints.NotNull;

public record AddressSaveRequest(@NotNull(message = "province can't be null") Integer provinceId,
                                 @NotNull(message = "county can't be null") Integer countyId,
                                 @NotNull(message = "city can't be null") Integer cityId,
                                 String street) {
}
