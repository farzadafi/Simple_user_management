package ir.farzadafi.dto;

public record UserUpdateResponse(
        Integer id,
        String firstname,
        String lastname,
        String nationalCode,
        String email,
        String birthdate) {
}