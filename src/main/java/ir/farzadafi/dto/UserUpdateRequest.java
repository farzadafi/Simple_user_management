package ir.farzadafi.dto;

import java.util.Date;

public record UserUpdateRequest(String firstname,
                                String lastname,
                                Date birthdate,
                                String email) {
}
