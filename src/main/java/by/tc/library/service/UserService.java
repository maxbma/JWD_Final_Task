package by.tc.library.service;

import by.tc.library.bean.RegistrationInfo;
import by.tc.library.bean.User;
import by.tc.library.service.validation.exception.ValidatorException;

public interface UserService {
	User authorization(String login, String passport) throws ServiceException, ValidatorException;
	boolean registration(RegistrationInfo regInfo) throws ServiceException, ValidatorException;
	int getReaderID(int userID) throws ServiceException;
	boolean changePassword(String oldPassword, String newPassword,
						   String confirmedPassword, int userID) throws ServiceException, ValidatorException;
}
