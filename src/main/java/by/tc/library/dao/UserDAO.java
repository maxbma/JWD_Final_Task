package by.tc.library.dao;

import by.tc.library.bean.RegistrationInfo;
import by.tc.library.bean.User;

public interface UserDAO {
	
	User authorization (String login, String password) throws DAOException;
	boolean	registration(RegistrationInfo regInfo) throws DAOException;
	int getReaderID(int userID) throws DAOException;
	boolean changePassword(String oldPassword, String newPassword,
						    int userID) throws DAOException;
}
