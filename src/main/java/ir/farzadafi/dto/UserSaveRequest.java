package ir.farzadafi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ir.farzadafi.validation.NationalCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

import static ir.farzadafi.utility.Constant.*;

public record UserSaveRequest(String firstname,
                              String lastname,
                              @NationalCode
                              String nationalCode,
                              @Email
                              String email,
                              @Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_MESSAGE)
                              String password,
                              @Pattern(regexp = BIRTHDATE_PATTERN, message = BIRTHDATE_MESSAGE)
                              @JsonFormat(pattern = "yyyy-MM-dd")
                              LocalDate birthdate) {
}
