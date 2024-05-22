package ir.farzadafi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class NationalCodeValidator implements ConstraintValidator<NationalCode, String> {

    @Override
    public void initialize(NationalCode constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String melliCode, ConstraintValidatorContext constraintValidatorContext) {
        String[] identicalDigits = {"0000000000", "1111111111", "2222222222",
                "3333333333", "4444444444", "5555555555",
                "6666666666", "7777777777", "8888888888", "9999999999"};

        if (melliCode == null)
            return true;
        if (melliCode.trim().isEmpty())
            return true; // National Code is empty
        else if (melliCode.length() != 10)
            return false; // National Code is less or more than 10 digits
        else if (Arrays.asList(identicalDigits).contains(melliCode))
            return false; // Fake National Code
        else {
            int sum = 0;
            for (int i = 0; i < 9; i++)
                sum += Character.getNumericValue(melliCode.charAt(i)) * (10 - i);

            int lastDigit;
            int divideRemaining = sum % 11;

            if (divideRemaining < 2)
                lastDigit = divideRemaining;
            else
                lastDigit = 11 - (divideRemaining);

            return Character.getNumericValue(melliCode.charAt(9)) == lastDigit;
        }
    }
}
