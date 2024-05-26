package ir.farzadafi.dto;

public record UserSearchResponse(
        Integer id,
        String firstname,
        String lastname,
        String nationalCode,
        String email,
        String birthdate) {
}