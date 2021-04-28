package by.tc.library.dao.impl;

import java.sql.*;
import java.util.*;

import by.tc.library.bean.Book;
import by.tc.library.dao.DAOException;
import by.tc.library.dao.BookDAO;
import by.tc.library.dao.pool.ConnectionPoolException;
import by.tc.library.dao.pool.ConnectionPool;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class SQLBookDAO implements BookDAO {

	private final static Logger logger = Logger.getLogger(SQLBookDAO.class.getName());
	private final static ConnectionPool connectionPool = ConnectionPool.getInstance();

	private final static String SELECT_ALL_BOOKS;
	private final static String SELECT_ALL_BOOKS_OF_GENRE;
	private final static String SELECT_BY_ID;
	private final static String SELECT_ALL_GENRES;
	private final static String SELECT_POPULAR_BOOKS;
	private final static String SELECT_SAME_BOOKS;
	private final static String UPDATE_SAME_BOOKS;
	private final static String DELETE_BOOK;
	private final static String AMOUNT_OF_BOOKS;
	private final static String GENRES_OF_BOOK;
	private final static String INSERT_BOOK_GENRE;
	private final static String DELETE_BOOK_GENRE;
	private final static String ADD_BOOK;

	private final static String BOOK_ID = "b_id";
	private final static String BOOK_NAME = "b_name";
	private final static String AUTHOR_SURNAME = "b_author_surname";
	private final static String AUTHOR_NAME = "b_author_name";
	private final static String RELEASE_YEAR = "b_release_year";
	private final static String PICTURE = "b_picture";
	private final static String DESCRIPTION = "b_description";
	private final static String NOT_SELECTED = "not selected";
	private final static String POPULARITY = "popularity";
	private final static String GENRE_NAME = "g_name";
	private final static String GENRE_ID = "g_id";
	private final static String AMOUNT = "amount";

	private final static String FATAL_ERROR_RESULT_SET = "Fatal error closing resultSet";
	private final static String FATAL_ERROR_PREPARED_STATEMENT = "Fatal error closing preparedStatement";


	static {
		SELECT_ALL_BOOKS = "SELECT * FROM `book_info`";
		SELECT_BY_ID = "SELECT * FROM `book_info` WHERE `b_id`= ? ";
		SELECT_ALL_GENRES = "SELECT * FROM `genres`";
		SELECT_ALL_BOOKS_OF_GENRE = "SELECT * FROM `book_info` INNER JOIN (`m2m_book_genres`" +
				"INNER JOIN `genres` ON `genres`.g_id=`m2m_book_genres`.genre_id) " +
				"ON `book_info`.b_id=`m2m_book_genres`.book_id " +
				"WHERE g_name = ? ";
		SELECT_POPULAR_BOOKS = "SELECT *, SUM(amount_of_orders) AS popularity FROM book_info " +
				"GROUP BY b_name, b_author_name, b_author_surname, b_description " +
				"ORDER BY amount_of_orders DESC, b_name  ";
		SELECT_SAME_BOOKS = "SELECT b_id FROM `book_info` " +
				"WHERE b_name = ? " +
				"AND b_author_surname = ? " +
				"AND b_author_name = ? " +
				"AND b_release_year = ? ";
		UPDATE_SAME_BOOKS = "UPDATE `book_info` " +
				"SET b_name = ?, b_author_surname = ?, b_author_name = ?, " +
				"b_release_year = ?, b_description = ? " +
				"WHERE b_id = ?";
		DELETE_BOOK = "DELETE FROM `book_info` " +
				"WHERE b_id = ?";
		AMOUNT_OF_BOOKS = "SELECT COUNT(b_id) AS amount FROM `book_info`";
		GENRES_OF_BOOK = "SELECT * FROM `m2m_book_genres` INNER JOIN genres " +
				"ON `m2m_book_genres`.genre_id = genres.g_id " +
				"WHERE book_id = ? ";
		INSERT_BOOK_GENRE = "INSERT INTO `m2m_book_genres`(book_id,genre_id) " +
				"VALUES(?,?)";
		DELETE_BOOK_GENRE = "DELETE FROM `m2m_book_genres` " +
				"WHERE book_id = ? AND genre_id = ? ";
		ADD_BOOK = "INSERT INTO `book_info`(b_id, b_name,b_author_surname,b_author_name,b_description,b_release_year,b_picture) " +
				"VALUES(?,?,?,?,?,?,?)";
	}

	@Override
	public List<Book> all() throws DAOException {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		List<Book> books = null;
		try {
			con = connectionPool.takeConnection();

			st = con.prepareStatement(SELECT_ALL_BOOKS);
			rs = st.executeQuery();
			
			books = new ArrayList<Book>();
			while(rs.next()) {
				int id = rs.getInt(BOOK_ID);
				String bookName = rs.getString(BOOK_NAME);
				String authorSurname = rs.getString(AUTHOR_SURNAME);
				String authorName = rs.getString(AUTHOR_NAME);
				int releaseYear = rs.getInt(RELEASE_YEAR);
				String bookPicture = rs.getString(PICTURE);
				String description = rs.getString(DESCRIPTION);

				Book n = new Book(id, bookName, authorSurname,authorName,releaseYear,description,bookPicture);

				if(!books.contains(n)){
					books.add(n);
				}
			}
		} catch (SQLException | ConnectionPoolException e) {
			logger.error(e.getMessage());
			throw new DAOException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				}catch (SQLException e){
					logger.log(Level.FATAL, FATAL_ERROR_RESULT_SET, e);
				}
			}
			if (st != null) {
				try {
					st.close();
				}catch (SQLException e){
					logger.log(Level.FATAL, FATAL_ERROR_PREPARED_STATEMENT, e);
				}
			}
			if(con != null){
				connectionPool.releaseConnection(con);
			}
		}

		return books;
	}

	public List<Book> foundByName(String name, String genre) throws DAOException {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		List<Book> foundBooks = null;
		try {
			con = connectionPool.takeConnection();

			if(null == genre || genre.equals(NOT_SELECTED)){
				st = con.prepareStatement(SELECT_ALL_BOOKS);
			} else {
				st = con.prepareStatement(SELECT_ALL_BOOKS_OF_GENRE);
				st.setString(1, genre);
			}

			rs = st.executeQuery();

			foundBooks = new ArrayList<Book>();

			while(rs.next()) {
				String bookName = rs.getString(BOOK_NAME);
				String authorName = rs.getString(AUTHOR_NAME);
				String authorSurname = rs.getString(AUTHOR_SURNAME);

				if(!(bookName.toLowerCase().contains(name.toLowerCase()) ||
				authorName.toLowerCase().contains(name.toLowerCase()) ||
				authorSurname.toLowerCase().contains(name.toLowerCase()) ||
						(name.toLowerCase().contains(authorName.toLowerCase()) &&
								name.toLowerCase().contains(authorSurname.toLowerCase())) ||
						(name.toLowerCase().contains(authorName.toLowerCase()) &&
								name.toLowerCase().contains(authorSurname.toLowerCase()) &&
								name.toLowerCase().contains(bookName.toLowerCase())) ||
						(name.toLowerCase().contains(authorSurname.toLowerCase()) &&
								name.toLowerCase().contains(bookName.toLowerCase())))){
					continue;
				}


				int id = rs.getInt(BOOK_ID);
				int releaseYear = rs.getInt(RELEASE_YEAR);
				String bookPicture = rs.getString(PICTURE);
				String description = rs.getString(DESCRIPTION);

				Book n = new Book(id, bookName, authorSurname,authorName,releaseYear,description,bookPicture);

				if(!foundBooks.contains(n)) {
					foundBooks.add(n);
				}

			}
		} catch (SQLException | ConnectionPoolException e) {
			logger.error(e.getMessage());
			throw new DAOException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				}catch (SQLException e){
					logger.log(Level.FATAL, FATAL_ERROR_RESULT_SET, e);
				}
			}
			if (st != null) {
				try {
					st.close();
				}catch (SQLException e){
					logger.log(Level.FATAL, FATAL_ERROR_PREPARED_STATEMENT, e);
				}
			}
			if(con != null){
				connectionPool.releaseConnection(con);
			}
		}

		return foundBooks;
	}

	@Override
	public List<Book> getPopular() throws DAOException {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		List<Book> popularBooks = new ArrayList<>();
		try{
			con = connectionPool.takeConnection();

			st = con.prepareStatement(SELECT_POPULAR_BOOKS);
			rs = st.executeQuery();
			int i = 0;
			while(rs.next() && i<100){
				int bookID = rs.getInt(BOOK_ID);
				int releaseYear = rs.getInt(RELEASE_YEAR);
				int ordersAmount = rs.getInt(POPULARITY);
				String bookName = rs.getString(BOOK_NAME);
				String authorName = rs.getString(AUTHOR_NAME);
				String authorSurname = rs.getString(AUTHOR_SURNAME);
				String description = rs.getString(DESCRIPTION);
				String picture = rs.getString(PICTURE);

				Book book = new Book(bookID, bookName, authorSurname, authorName,
						releaseYear, description, picture);
				book.setOrdersAmount(ordersAmount);

				popularBooks.add(book);
				i++;
			}
		} catch (SQLException | ConnectionPoolException e) {
			logger.error(e.getMessage());
			throw new DAOException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				}catch (SQLException e){
					logger.log(Level.FATAL, FATAL_ERROR_RESULT_SET, e);
				}
			}
			if (st != null) {
				try {
					st.close();
				}catch (SQLException e){
					logger.log(Level.FATAL, FATAL_ERROR_PREPARED_STATEMENT, e);
				}
			}
			if(con != null){
				connectionPool.releaseConnection(con);
			}
		}

		return popularBooks;
	}

	public Book bookByID(int id) throws DAOException{
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		Book book = null;

		try{
			con = connectionPool.takeConnection();

			st = con.prepareStatement(SELECT_BY_ID);
			st.setInt(1, id);
			rs = st.executeQuery();

			while(rs.next()) {
				String bookName = rs.getString(BOOK_NAME);
				String authorSurname = rs.getString(AUTHOR_SURNAME);
				String authorName = rs.getString(AUTHOR_NAME);
				int releaseYear = rs.getInt(RELEASE_YEAR);
				String bookPicture = rs.getString(PICTURE);
				String description = rs.getString(DESCRIPTION);
				book = new Book(id, bookName, authorSurname,authorName,releaseYear,description,bookPicture);
			}

			st = con.prepareStatement(GENRES_OF_BOOK);
			st.setInt(1, id);
			rs = st.executeQuery();
			List<String> genres = new ArrayList<>();
			while(rs.next()){
				String genre = rs.getString(GENRE_NAME);
				genres.add(genre);
			}
			book.setGenres(genres);
		} catch (SQLException | ConnectionPoolException e) {
			logger.error(e.getMessage());
			throw new DAOException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				}catch (SQLException e){
					logger.log(Level.FATAL, FATAL_ERROR_RESULT_SET, e);
				}
			}
			if (st != null) {
				try {
					st.close();
				}catch (SQLException e){
					logger.log(Level.FATAL, FATAL_ERROR_PREPARED_STATEMENT, e);
				}
			}
			if(con != null){
				connectionPool.releaseConnection(con);
			}
		}
		return book;
	}

	@Override
	public List<String> allGenres() throws DAOException {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		List<String> bookGenres = new ArrayList<>();
		try{
			con = connectionPool.takeConnection();

			st = con.prepareStatement(SELECT_ALL_GENRES);
			rs = st.executeQuery();

			while(rs.next()){
				String genre = rs.getString(GENRE_NAME);
				bookGenres.add(genre);
			}
		} catch (SQLException | ConnectionPoolException e) {
			logger.error(e.getMessage());
			throw new DAOException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				}catch (SQLException e){
					logger.log(Level.FATAL, FATAL_ERROR_RESULT_SET, e);
				}
			}
			if (st != null) {
				try {
					st.close();
				}catch (SQLException e){
					logger.log(Level.FATAL, FATAL_ERROR_PREPARED_STATEMENT, e);
				}
			}
			if(con != null){
				connectionPool.releaseConnection(con);
			}
		}

		return bookGenres;
	}

	@Override
	public boolean addBook(Book book) throws DAOException {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		boolean isBookAdded = false;
		try{
			con = connectionPool.takeConnection();

			st = con.prepareStatement(ADD_BOOK);
			st.setInt(1, book.getId());
			st.setString(2, book.getBookName());
			st.setString(3, book.getAuthorSurname());
			st.setString(4, book.getAuthorName());
			st.setString(5, book.getDescription());
			st.setInt(6, book.getReleaseYear());
			st.setString(7, book.getPicture());
			st.executeUpdate();

			st = con.prepareStatement(SELECT_ALL_GENRES);
			rs = st.executeQuery();
			List<Integer> genresID = new ArrayList<>();
			List<String> bookGenres = book.getGenres();
			if(bookGenres != null){
				while(rs.next()){
					if(bookGenres.contains(rs.getString(GENRE_NAME))){
						genresID.add(rs.getInt(GENRE_ID));
					}
				}
			}

			st = con.prepareStatement(INSERT_BOOK_GENRE);
			st.setInt(1, book.getId());
			for(int id : genresID){
				st.setInt(2, id);
				st.executeUpdate();
			}

			isBookAdded = true;
		} catch (SQLException | ConnectionPoolException e) {
			logger.error(e.getMessage());
			throw new DAOException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				}catch (SQLException e){
					logger.log(Level.FATAL, FATAL_ERROR_RESULT_SET, e);
				}
			}
			if (st != null) {
				try {
					st.close();
				}catch (SQLException e){
					logger.log(Level.FATAL, FATAL_ERROR_PREPARED_STATEMENT, e);
				}
			}
			if(con != null){
				connectionPool.releaseConnection(con);
			}
		}
		return isBookAdded;
	}

	@Override
	public boolean changeBook(Book oldBook, Book newBook) throws DAOException {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		boolean isBookChanged = false;
		try{
			con = connectionPool.takeConnection();

			st = con.prepareStatement(SELECT_SAME_BOOKS);
			String bookName = oldBook.getBookName();
			String authorSurname = oldBook.getAuthorSurname();
			String authorName = oldBook.getAuthorName();
			String description = oldBook.getDescription();
			int releaseYear = oldBook.getReleaseYear();

			st.setString(1, bookName);
			st.setString(2,authorSurname);
			st.setString(3, authorName);
			st.setInt(4, releaseYear);
			rs = st.executeQuery();

			List<Integer> booksToChangeID = new ArrayList<>();
			while(rs.next()){
				int id = rs.getInt(BOOK_ID);
				booksToChangeID.add(id);
			}

			String newBookName = newBook.getBookName();
			String newAuthorSurname = newBook.getAuthorSurname();
			String newAuthorName = newBook.getAuthorName();
			String newDescription = newBook.getDescription();
			int newReleaseYear = newBook.getReleaseYear();

			st =  con.prepareStatement(UPDATE_SAME_BOOKS);
			st.setString(1, newBookName);
			st.setString(2, newAuthorSurname);
			st.setString(3, newAuthorName);
			st.setInt(4, newReleaseYear);
			st.setString(5, newDescription);

			for(Integer id : booksToChangeID){
				st.setInt(6, id);
				st.executeUpdate();
			}

			st = con.prepareStatement(SELECT_ALL_GENRES);
			rs = st.executeQuery();
			Map<Integer, String> allGenres = new HashMap<>();
			while(rs.next()){
				allGenres.put(rs.getInt(GENRE_ID), rs.getString(GENRE_NAME));
			}

			List<String> bookGenres = newBook.getGenres();
			st = con.prepareStatement(GENRES_OF_BOOK);
			st.setInt(1, newBook.getId());
			rs = st.executeQuery();

			Map<Integer, String> bookGenresToDeleteWithID = new HashMap<>();

			List<Integer> toDeleteFromAllGenres = new ArrayList<>();
			for(Map.Entry<Integer, String> entry : allGenres.entrySet()){
				if(!bookGenres.contains(entry.getValue())){
					toDeleteFromAllGenres.add(entry.getKey());
				}
			}
			for(int id : toDeleteFromAllGenres){
				allGenres.remove(id);
			}

			while(rs.next()){
				String genre = rs.getString(GENRE_NAME);
				Integer genreID = rs.getInt(GENRE_ID);
				if(bookGenres.contains(genre)){
					allGenres.remove(genreID, genre);
				} else {
					bookGenresToDeleteWithID.put(genreID, genre);
				}
			}

			st = con.prepareStatement(DELETE_BOOK_GENRE);
			if(!bookGenresToDeleteWithID.isEmpty()){
				for(int bookID : booksToChangeID){
					for(Map.Entry<Integer, String> entry : bookGenresToDeleteWithID.entrySet()){
						st.setInt(1, bookID);
						st.setInt(2, entry.getKey());
						st.executeUpdate();
					}
				}
			}

			st = con.prepareStatement(INSERT_BOOK_GENRE);
			if(!allGenres.isEmpty()){
				for(int bookID : booksToChangeID){
					for(Map.Entry<Integer, String> entry : allGenres.entrySet()){
						st.setInt(1, bookID);
						st.setInt(2, entry.getKey());
						st.executeUpdate();
					}
				}
			}
			isBookChanged = true;
		} catch (SQLException | ConnectionPoolException e) {
			logger.error(e.getMessage());
			throw new DAOException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				}catch (SQLException e){
					logger.log(Level.FATAL, FATAL_ERROR_RESULT_SET, e);
				}
			}
			if (st != null) {
				try {
					st.close();
				}catch (SQLException e){
					logger.log(Level.FATAL, FATAL_ERROR_PREPARED_STATEMENT, e);
				}
			}
			if(con != null){
				connectionPool.releaseConnection(con);
			}
		}
		return isBookChanged;
	}

	@Override
	public boolean deleteBook(Book book) throws DAOException {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		boolean isBookDeleted = false;
		try {
			con = connectionPool.takeConnection();

			st = con.prepareStatement(SELECT_SAME_BOOKS);
			String bookName = book.getBookName();
			String authorSurname = book.getAuthorSurname();
			String authorName = book.getAuthorName();
			String description = book.getDescription();
			int releaseYear = book.getReleaseYear();

			st.setString(1, bookName);
			st.setString(2,authorSurname);
			st.setString(3, authorName);
			st.setInt(4, releaseYear);
			rs = st.executeQuery();

			List<Integer> booksToDeleteID = new ArrayList<>();
			while(rs.next()){
				int id = rs.getInt(BOOK_ID);
				booksToDeleteID.add(id);
			}

			st = con.prepareStatement(DELETE_BOOK);
			for(Integer id : booksToDeleteID){
				st.setInt(1, id);
				st.executeUpdate();
			}

			isBookDeleted = true;
		} catch (SQLException | ConnectionPoolException e) {
			logger.error(e.getMessage());
			throw new DAOException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				}catch (SQLException e){
					logger.log(Level.FATAL, FATAL_ERROR_RESULT_SET, e);
				}
			}
			if (st != null) {
				try {
					st.close();
				}catch (SQLException e){
					logger.log(Level.FATAL, FATAL_ERROR_PREPARED_STATEMENT, e);
				}
			}
			if(con != null){
				connectionPool.releaseConnection(con);
			}
		}
		return isBookDeleted;
	}

	@Override
	public int bookAmount() throws DAOException {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		int amountOfBooks = 0;
		try{
			con = connectionPool.takeConnection();

			st = con.prepareStatement(AMOUNT_OF_BOOKS);
			rs = st.executeQuery();
			if(rs.next()){
				amountOfBooks = rs.getInt(AMOUNT);
			}
		} catch (SQLException | ConnectionPoolException e) {
			logger.error(e.getMessage());
			throw new DAOException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				}catch (SQLException e){
					logger.log(Level.FATAL, FATAL_ERROR_RESULT_SET, e);
				}
			}
			if (st != null) {
				try {
					st.close();
				}catch (SQLException e){
					logger.log(Level.FATAL, FATAL_ERROR_PREPARED_STATEMENT, e);
				}
			}
			if(con != null){
				connectionPool.releaseConnection(con);
			}
		}
		return amountOfBooks;
	}

}
