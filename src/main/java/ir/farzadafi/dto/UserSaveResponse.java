package ir.farzadafi.dto;

public record UserSaveResponse(
        Integer id,
        String firstname,
        String lastname,
        String nationalCode,
        String email,
        String birthdate) {
}