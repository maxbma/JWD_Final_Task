package by.tc.library.dao.impl;

import by.tc.library.bean.RegistrationInfo;
import by.tc.library.bean.User;
import by.tc.library.dao.DAOException;
import by.tc.library.dao.UserDAO;
import by.tc.library.dao.pool.ConnectionPool;
import by.tc.library.dao.pool.ConnectionPoolException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.sql.*;

public class SQLUserDAO implements UserDAO{

	private final static Logger logger = Logger.getLogger(SQLUserDAO.class.getName());
	private final static ConnectionPool connectionPool = ConnectionPool.getInstance();

	private final static String ROLE_READER;
	private final static String SELECT_USER_BY_ID;
	private final static String UPDATE_PASSWORD;
	private final static String SELECT_BY_LOGIN;
	private final static String COUNT_SUCH_LOGIN;

	private final static String USER_PASSWORD = "u_password";
	private final static String USER_ID = "u_id";
	private final static String USER_ROLE = "u_role";
	private final static String READER = "reader";
	private final static String LIBRARIAN = "librarian";
	private final static String READER_ID = "r_id";
	private final static String LOGIN_EQUALS = "loginEquals";
	private final static String READER_BY_USER_ID = "SELECT * FROM `reader` WHERE `r_u_id`=?";
	private final static String REGISTRSATE_USER = "INSERT INTO `user`(`u_login`, `u_password`) VALUES(?,?)";
	private final static String COUNT_USERS = "SELECT COUNT(u_id) AS u_id FROM `user`;";
	private final static String REGISTRSATE_READER = "INSERT INTO `reader`(`r_u_id`,`r_address`,`r_name`,`r_surname`) " +
			"VALUES(?, ?, ?, ?);";

	private final static String FATAL_ERROR_RESULT_SET = "Fatal error closing resultSet";
	private final static String FATAL_ERROR_PREPARED_STATEMENT = "Fatal error closing preparedStatement";


	static {
		ROLE_READER = "читатель";
		SELECT_USER_BY_ID = "SELECT * FROM `user` WHERE u_id = ? ";
		UPDATE_PASSWORD = "UPDATE `user`" +
				"SET `u_password` = ? " +
				"WHERE u_id = ? ";
		SELECT_BY_LOGIN = "SELECT * FROM `user` WHERE `u_login` = ? ";
		COUNT_SUCH_LOGIN = "SELECT COUNT(u_id) AS loginEquals FROM `user` WHERE `u_login` = ? ";
	}
	
	@Override
	public User authorization(String login, String password) throws DAOException {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		User user = null;

		try{
			con = connectionPool.takeConnection();

			st = con.prepareStatement(SELECT_BY_LOGIN);
			st.setString(1, login);
			rs = st.executeQuery();

			while(rs.next()){
				String userPassword = rs.getString(USER_PASSWORD);
				String userRole = null;
				int userID = rs.getInt(USER_ID);
				if(ROLE_READER.equals(rs.getString(USER_ROLE))){
					userRole = READER;
				} else{
					userRole = LIBRARIAN;
				}

				if(userPassword.equals(password)){
					user = new User(userRole.toUpperCase(), userID);
				}else{
					return null;
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

		return user;
	}

	@Override
	public boolean registration(RegistrationInfo regInfo) throws DAOException {

		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		boolean isUserRegistrated = false;
		int userID = 0;

		try {
			con = connectionPool.takeConnection();

			st = con.prepareStatement(COUNT_SUCH_LOGIN);
			st.setString(1, regInfo.getLogin());
			rs = st.executeQuery();
			while(rs.next()) {
				int usersWithSuchLogin = rs.getInt(LOGIN_EQUALS);
				if (usersWithSuchLogin != 0) {
					return false;
				}
				break;
			}

			st = con.prepareStatement(REGISTRSATE_USER);
			st.setString(1, regInfo.getLogin());
			st.setString(2, regInfo.getPassword());
			st.executeUpdate();

			st = con.prepareStatement(COUNT_USERS);
			rs = st.executeQuery();

			if(rs.next())
			{
				userID = rs.getInt(USER_ID);
			}

			isUserRegistrated = readerRegistration(regInfo, userID);
		}catch (SQLException | ConnectionPoolException e) {
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

		return isUserRegistrated;
	}

	@Override
	public int getReaderID(int userID) throws DAOException {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		int readerID = 0;
		try {
			con = connectionPool.takeConnection();

			st = con.prepareStatement(READER_BY_USER_ID);
			st.setInt(1, userID);
			rs = st.executeQuery();

			if(rs.next()){
				readerID = rs.getInt(READER_ID);
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

		return readerID;
	}

	@Override
	public boolean changePassword(String oldPassword, String newPassword, int userID) throws DAOException {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		boolean isPasswordChanged = false;
		try{
			con = connectionPool.takeConnection();
			st = con.prepareStatement(SELECT_USER_BY_ID);
			st.setInt(1, userID);
			rs = st.executeQuery();

			if(rs.next()){
				String currentPassword = rs.getString(USER_PASSWORD);
				if(!currentPassword.equals(oldPassword)){
					return false;
				}
			}

			st = con.prepareStatement(UPDATE_PASSWORD);
			st.setString(1, newPassword);
			st.setInt(2, userID);
			st.executeUpdate();

			isPasswordChanged = true;

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

		return isPasswordChanged;
	}

	public boolean readerRegistration(RegistrationInfo regInfo, int userID) throws DAOException {
		Connection con = null;
		PreparedStatement st = null;

		boolean isReaderRegistrated = false;

		try {
			con = connectionPool.takeConnection();

			st = con.prepareStatement(REGISTRSATE_READER);
			st.setInt(1, userID);
			st.setString(2, regInfo.getAddress());
			st.setString(3, regInfo.getName());
			st.setString(4, regInfo.getSurname());
			st.executeUpdate();
			isReaderRegistrated = true;
		} catch (SQLException | ConnectionPoolException e) {
			logger.error(e.getMessage());
			throw new DAOException(e);
		} finally {
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

		return isReaderRegistrated;
	}
}
