package ir.farzadafi.dto;

import jakarta.validation.constraints.Email;

public record GenerateNewVerificationCodeRequest(@Email
                                                 String email,
                                                 String password) {
}
