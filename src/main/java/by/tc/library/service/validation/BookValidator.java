package by.tc.library.service.validation;

import by.tc.library.bean.Book;

import java.util.Calendar;

public class BookValidator {
    private final static String NAME_PATTERN = "^[A-ZА-Я][a-zа-я]+$";
    private final static String EMPTY_STRING = "";

    public static boolean isNotContainingEmptyFields(Book book){
        return (null != book.getBookName()) &&
                (null != book.getAuthorName()) &&
                (null != book.getAuthorSurname()) &&
                (null != book.getDescription()) &&
                (!EMPTY_STRING.equals(book.getBookName())) &&
                (!EMPTY_STRING.equals(book.getAuthorName())) &&
                (!EMPTY_STRING.equals(book.getAuthorSurname())) &&
                (!EMPTY_STRING.equals(book.getDescription()));
    }

    public static boolean isAuthorNameValid(String name){
        return name.matches(NAME_PATTERN);
    }
    public static boolean isAuthorSurnameValid(String surname){
        return surname.matches(NAME_PATTERN);
    }
    public static boolean isReleaseYearValid(int releaseYear){
        //Первая точно датированная печатная книга была издана 11 мая 868 года
        return (releaseYear > 867) &&
                (releaseYear <= Calendar.getInstance().get(Calendar.YEAR));
    }
}
