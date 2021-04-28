package by.tc.library.service.impl;

import java.util.List;

import by.tc.library.bean.Book;
import by.tc.library.dao.DAOException;
import by.tc.library.dao.DAOProvider;
import by.tc.library.dao.BookDAO;
import by.tc.library.service.BookService;
import by.tc.library.service.ServiceException;
import by.tc.library.service.validation.BookValidator;
import by.tc.library.service.validation.exception.ValidatorException;

public class BookServiceImpl implements BookService {

	private final static String INPUT_ALL_DATA = "To add new book you must input book name, author name and surname, description and release year";
	private final static String INVALID_NAME = "Invalid name. The name must start with a capital letter";
	private final static String INVALID_SURNAME = "Invalid surname. The surname must start with a capital letter";
	private final static String INVALID_YEAR = "Invalid release year. Release year must be between 867 and current year";

	@Override
	public List<Book> takeAll() throws ServiceException {

		DAOProvider provider = DAOProvider.getInstance();
		BookDAO bookDAO = provider.getBookDAO();
		
		List<Book> books;
		try {
			books = bookDAO.all();
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		
		return books;
	}

	@Override
	public List<Book> takeFound(String name, String genre) throws ServiceException {
		DAOProvider provider = DAOProvider.getInstance();
		BookDAO bookDAO = provider.getBookDAO();

		List<Book> foundBooks;
		try {
			foundBooks = bookDAO.foundByName(name, genre);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}

		return foundBooks;
	}

	@Override
	public List<Book> getPopular() throws ServiceException {
		DAOProvider provider = DAOProvider.getInstance();
		BookDAO bookDAO = provider.getBookDAO();

		List<Book> popularBooks;
		try {
			popularBooks = bookDAO.getPopular();
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return popularBooks;
	}

	public Book takeByID(int id) throws ServiceException{
		DAOProvider provider = DAOProvider.getInstance();
		BookDAO bookDAO = provider.getBookDAO();

		Book bookByID;
		try{
			bookByID = bookDAO.bookByID(id);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}

		return bookByID;
	}

	@Override
	public List<String> allGenres() throws ServiceException {
		DAOProvider provider = DAOProvider.getInstance();
		BookDAO bookDAO = provider.getBookDAO();

		List<String> bookGenres;
		try{
			bookGenres = bookDAO.allGenres();
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return bookGenres;
	}

	@Override
	public boolean addBook(Book book) throws ServiceException, ValidatorException {

		if(!BookValidator.isNotContainingEmptyFields(book)){
			throw new ValidatorException(INPUT_ALL_DATA);
		}
		if(!BookValidator.isAuthorNameValid(book.getAuthorName())){
			throw new ValidatorException(INVALID_NAME);
		}
		if(!BookValidator.isAuthorSurnameValid(book.getAuthorSurname())){
			throw new ValidatorException(INVALID_SURNAME);
		}
		if(!BookValidator.isReleaseYearValid(book.getReleaseYear())){
			throw new ValidatorException(INVALID_YEAR);
		}

		DAOProvider provider = DAOProvider.getInstance();
		BookDAO bookDAO = provider.getBookDAO();

		boolean isBookAdded = false;
		try{
			isBookAdded = bookDAO.addBook(book);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return  isBookAdded;
	}

	@Override
	public boolean changeBook(Book oldBook, Book newBook) throws ServiceException, ValidatorException {
		if(!BookValidator.isNotContainingEmptyFields(newBook)){
			throw new ValidatorException(INPUT_ALL_DATA);
		}
		if(!BookValidator.isAuthorNameValid(newBook.getAuthorName())){
			throw new ValidatorException(INVALID_NAME);
		}
		if(!BookValidator.isAuthorSurnameValid(newBook.getAuthorSurname())){
			throw new ValidatorException(INVALID_SURNAME);
		}
		if(!BookValidator.isReleaseYearValid(newBook.getReleaseYear())){
			throw new ValidatorException(INVALID_YEAR);
		}

		DAOProvider provider = DAOProvider.getInstance();
		BookDAO bookDAO = provider.getBookDAO();
		boolean isBookChanged = false;

		try{
			isBookChanged = bookDAO.changeBook(oldBook, newBook);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return  isBookChanged;
	}

	@Override
	public boolean deleteBook(Book book) throws ServiceException {
		DAOProvider provider = DAOProvider.getInstance();
		BookDAO bookDAO = provider.getBookDAO();
		boolean isBookDeleted = false;

		try{
			isBookDeleted = bookDAO.deleteBook(book);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return isBookDeleted;
	}

	@Override
	public int bookAmount() throws ServiceException{
		DAOProvider provider = DAOProvider.getInstance();
		BookDAO bookDAO = provider.getBookDAO();
		int bookAmount = 0;

		try{
			bookAmount = bookDAO.bookAmount();
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return bookAmount;
	}
}



