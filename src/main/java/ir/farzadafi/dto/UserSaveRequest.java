package ir.farzadafi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import ir.farzadafi.validation.NationalCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import static ir.farzadafi.utility.Constant.*;

public record UserSaveRequest(
        @Schema(example = "farzad")
        String firstname,

        @Schema(example = "afshar")
        String lastname,

        @NationalCode
        @Schema(example = "3080252***")
        String nationalCode,

        @Email
        @Schema(example = "farzadafi40@gmail.com")
        String email,

        @Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_MESSAGE)
        @Schema(example = "aaaAAA111!!!")
        String password,

        @Pattern(regexp = BIRTHDATE_PATTERN, message = BIRTHDATE_MESSAGE)
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Schema(example = "1997-10-10")
        String birthdate) {
}
