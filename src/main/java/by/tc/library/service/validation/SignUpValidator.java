package by.tc.library.service.validation;

public class SignUpValidator {
    private final static String LOGIN_PASSWORD_PATTERN = "^[a-zA-Z_0-9]{3,50}$";
    private final static String FIO_PATTERN = "^[A-ZА-Я][a-zа-я]+$";

    public static boolean isLoginValid(String login){
        return login.matches(LOGIN_PASSWORD_PATTERN);
    }

    public static boolean isPasswordValid(String password){
        return password.matches(LOGIN_PASSWORD_PATTERN);
    }

    public static boolean isNameValid(String name){
        return name.matches(FIO_PATTERN);
    }

    public static boolean isSurnameValid(String surname){
        return surname.matches(FIO_PATTERN);
    }

}
