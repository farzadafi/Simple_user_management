package ir.farzadafi.utility;

public class Constant {

    public static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&#^])[A-Za-z0-9@$!%*?&]{8,}$";

    public static final String PASSWORD_MESSAGE =
            "Minimum eight char, at least one uppercase, one lowercase letter, one number and one special character";

    public static final String BIRTHDATE_PATTERN =
            "^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$";

    public static final String BIRTHDATE_MESSAGE =
            "please send a valid birth date -> 1999/10/10";
}
