package ir.farzadafi.dto;

import java.util.Date;

public record UserUpdateRequestDto(String firstname,
                                   String lastname,
                                   Date birthdate,
                                   String email) {
}
