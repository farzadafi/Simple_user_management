package ir.farzadafi.dto;

import java.time.LocalDate;

public record UserUpdateRequest(String firstname,
                                String lastname,
                                LocalDate birthdate,
                                String email) {
}
