package ir.farzadafi.utility;

public class Constant {

    public static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&#^])[A-Za-z0-9@$!%*?&]{8,}$";

    public static final String PASSWORD_MESSAGE =
            "Minimum eight char, at least one uppercase, one lowercase letter, one number and one special character";
}
