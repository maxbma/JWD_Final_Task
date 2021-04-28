package by.tc.library.dao.impl;

import by.tc.library.bean.Book;
import by.tc.library.controller.listener.ContextListener;
import by.tc.library.dao.BookDAO;
import by.tc.library.dao.DAOException;
import by.tc.library.dao.DAOProvider;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBookDAO {
    private final static DAOProvider provider = DAOProvider.getInstance();
    private final static BookDAO bookDAO = provider.getBookDAO();
    private final static ContextListener listener = new ContextListener();

    @Test
    public void test01FindBookById() throws DAOException {
        listener.contextInitialized(null);

        final int bookID = 1;
        Book expected = new Book(bookID, "Название", "Фамилия",
                "Имя", 2015, "Описание", "Фотография");
        Book actual = bookDAO.bookByID(bookID);
        assertEquals(expected, actual);
    }

    @Test
    public void test02FindByName() throws DAOException{
        String bookName = "Название";
        Book expectedBook = new Book(1, "Название", "Фамилия",
                "Имя", 2015, "Описание", "Фотография");
        List<Book> expected = new ArrayList<>();
        expected.add(expectedBook);
        List<Book> actual = bookDAO.foundByName(bookName, null);
        assertEquals(expected,actual);
    }

    @Test
    public void test03FindByAuthor() throws DAOException{
        String author = "Имя Фамилия";
        Book expectedBook = new Book(1, "Название", "Фамилия",
                "Имя", 2015, "Описание", "Фотография");
        List<Book> expected = new ArrayList<>();
        expected.add(expectedBook);
        List<Book> actual = bookDAO.foundByName(author, null);
        assertEquals(expected,actual);
    }

    @Test
    public void test04FindByNameAndGenre() throws DAOException{
        String genre = "Жанр1";
        String bookName = "Название";
        Book expectedBook = new Book(1, "Название", "Фамилия",
                "Имя", 2015, "Описание", "Фотография");
        List<Book> expected = new ArrayList<>();
        expected.add(expectedBook);
        List<Book> actual = bookDAO.foundByName(bookName, genre);
        assertEquals(expected,actual);
    }

    @Test
    public void test05NegativeFindByGenre() throws DAOException{
        String genre = "Жанр2";
        String bookName = "Название";
        Book unexpectedBook = new Book(1, "Название", "Фамилия",
                "Имя", 2015, "Описание", "Фотография");
        List<Book> unexpected = new ArrayList<>();
        unexpected.add(unexpectedBook);
        List<Book> actual = bookDAO.foundByName(bookName, genre);
        assertNotEquals(unexpected, actual);
    }

    @Test
    public void test06AllGenres() throws DAOException{
        String genre1 = "Жанр1";
        String genre2 = "Жанр2";
        List<String> expected = new ArrayList<>();
        expected.add(genre1);
        expected.add(genre2);
        List<String> actual = bookDAO.allGenres();
        assertEquals(expected,actual);
    }

    @Test
    public void test07AddBook() throws DAOException{
        boolean expected = true;
        Book newBook = new Book(2, "Название2", "Фамилия2",
                "Имя2", 2020,"Описание2","Фото2");
        boolean actual = bookDAO.addBook(newBook);
        assertEquals(expected,actual);
    }

    @Test
    public void test08CheckAddedBook() throws DAOException{
        Book expected = new Book(2, "Название2", "Фамилия2",
                "Имя2", 2020,"Описание2","Фото2");
        Book actual = bookDAO.bookByID(2);
        assertEquals(expected,actual);
    }

    @Test
    public void test09DeleteAddedBook() throws DAOException{
        Book bookToDelete = new Book(2, "Название2", "Фамилия2",
                "Имя2", 2020,"Описание2","Фото2");
        boolean expected = true;
        boolean actual = bookDAO.deleteBook(bookToDelete);
        assertEquals(expected,actual);
    }

    @Test
    public void test10NegativeFindAddedBook()throws DAOException{
        Book expectedBook = new Book(2, "Название2", "Фамилия2",
                "Имя2", 2020,"Описание2","Фото2");
        List<Book> expected = new ArrayList<>();
        expected.add(expectedBook);
        List<Book> actual = bookDAO.foundByName(expectedBook.getBookName(), null);
        assertNotEquals(expected,actual);
    }

}
