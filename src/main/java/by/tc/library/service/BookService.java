package by.tc.library.service;

import java.util.List;

import by.tc.library.bean.Book;
import by.tc.library.service.validation.exception.ValidatorException;

public interface BookService {
	List<Book> takeAll() throws ServiceException;
	List<Book> takeFound(String name, String genre) throws ServiceException;
	List<Book> getPopular() throws ServiceException;
	Book takeByID(int id) throws ServiceException;
	List<String> allGenres() throws  ServiceException;
	boolean addBook(Book book) throws ServiceException, ValidatorException;
	boolean changeBook(Book oldBook, Book newBook) throws ServiceException, ValidatorException;
	boolean deleteBook(Book book) throws ServiceException;
	int bookAmount() throws ServiceException;
}
