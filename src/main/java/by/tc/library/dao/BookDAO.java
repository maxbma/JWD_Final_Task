package by.tc.library.dao;

import java.util.List;

import by.tc.library.bean.Book;

public interface BookDAO {
	
	List<Book> all()  throws DAOException;
	List<Book> foundByName(String name, String genre) throws DAOException;
	List<Book> getPopular() throws DAOException;
	Book bookByID(int id) throws DAOException;
	List<String> allGenres() throws DAOException;
	boolean addBook(Book book) throws DAOException;
	boolean changeBook(Book oldBook, Book newBook) throws DAOException;
	boolean deleteBook(Book book) throws DAOException;
	int bookAmount() throws DAOException;
}
