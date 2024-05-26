package ir.farzadafi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class ValidSearchColumnValidator implements ConstraintValidator<ValidSearchColumn, String> {

    private static final List<String> VALID_COLUMN_SEARCH = Arrays.asList("age", "province", "county", "city");

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return name == null || VALID_COLUMN_SEARCH.contains(name.toLowerCase());
    }
}