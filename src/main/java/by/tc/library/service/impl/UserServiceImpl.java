package by.tc.library.service.impl;

import by.tc.library.bean.RegistrationInfo;
import by.tc.library.bean.User;
import by.tc.library.dao.DAOException;
import by.tc.library.dao.DAOProvider;
import by.tc.library.dao.UserDAO;
import by.tc.library.service.ServiceException;
import by.tc.library.service.UserService;
import by.tc.library.service.validation.PasswordChangeValidator;
import by.tc.library.service.validation.SignUpValidator;
import by.tc.library.service.validation.exception.ValidatorException;

public class UserServiceImpl implements UserService {

	private final static String EMPTY_STRING = "";
	private final static String INVALID_LOGIN = "Invalid login. Login may include letters from A to Z in any register, numbers from 0 to 9 " +
			" and underscore. Login length must be from 3 to 50 signs";
	private final static String INVALID_PASSWORD = "Invalid password. Password may include letters from A to Z in any register," +
			" numbers from 0 to 9 and underscore. Password length must be from 3 to 50 signs";
	private final static String INVALID_NAME = "Invalid name. The name must start with a capital letter";
	private final static String INVALID_SURNAME = "Invalid surname. The surname must start with a capital letter";
	private final static String INPUT_ALL_DATA = "Input all data";
	private final static String SAME_PASSWORDS = "Old and new passwords are the same";
	private final static String CONFIRM_PASSWORD = "New and confirmed passwords are not the same";
	
	@Override
	public User authorization(String login, String password) throws ServiceException, ValidatorException {

		if(login == null || EMPTY_STRING.equals(login) || password == null || EMPTY_STRING.equals(password)) {
			throw new ValidatorException(INPUT_ALL_DATA);
		}
		
		DAOProvider provider = DAOProvider.getInstance();
        UserDAO userDAO = provider.getUserdao();
        
		User user = null;
		try {
			user = userDAO.authorization(login, password);
		}catch(DAOException e) {
			throw new ServiceException(e);
		}
		return user;
	}

	@Override
	public boolean registration(RegistrationInfo regInfo) throws ServiceException, ValidatorException {
		String login = regInfo.getLogin();
		String password = regInfo.getPassword();
		String name = regInfo.getName();
		String surname = regInfo.getSurname();
		String address = regInfo.getAddress();

		if(login == null || EMPTY_STRING.equals(login) || password == null || EMPTY_STRING.equals(password)
		|| name == null || EMPTY_STRING.equals(name) || surname == null || EMPTY_STRING.equals(surname)
		|| address == null || EMPTY_STRING.equals(address)) {
			throw new ValidatorException(INPUT_ALL_DATA);
		}

		if(!SignUpValidator.isLoginValid(login)){
			throw new ValidatorException(INVALID_LOGIN);
		}
		if(!SignUpValidator.isPasswordValid(password)){
			throw new ValidatorException(INVALID_PASSWORD);
		}
		if(!SignUpValidator.isNameValid(name)){
			throw new ValidatorException(INVALID_NAME);
		}
		if(!SignUpValidator.isSurnameValid(surname)){
			throw new ValidatorException(INVALID_SURNAME);
		}

		DAOProvider provider = DAOProvider.getInstance();
		UserDAO userDAO = provider.getUserdao();

		boolean isUserRegistrated = false;
		try {
			isUserRegistrated = userDAO.registration(regInfo);
		}catch(DAOException e) {
			throw new ServiceException(e);
		}

		return isUserRegistrated;
	}

	@Override
	public int getReaderID(int userID) throws ServiceException {
		DAOProvider provider = DAOProvider.getInstance();
		UserDAO userDAO = provider.getUserdao();

		int readerID = 0;
		try{
			readerID = userDAO.getReaderID(userID);
		}catch(DAOException e) {
			throw new ServiceException(e);
		}

		return readerID;
	}

	@Override
	public boolean changePassword(String oldPassword, String newPassword, String confirmedPassword, int userID) throws ServiceException, ValidatorException {
		if(null == oldPassword || EMPTY_STRING.equals(oldPassword) ||
		null == newPassword || EMPTY_STRING.equals(newPassword) ||
		null == confirmedPassword || EMPTY_STRING.equals(confirmedPassword)){
			throw new ValidatorException(INPUT_ALL_DATA);
		}

		if(!PasswordChangeValidator.isPasswordSame(oldPassword, newPassword)){
			throw new ValidatorException(SAME_PASSWORDS);
		}
		if(!PasswordChangeValidator.isPasswordConfirmed(newPassword, confirmedPassword)){
			throw new ValidatorException(CONFIRM_PASSWORD);
		}
		if(!PasswordChangeValidator.isPasswordValid(newPassword)){
			throw new ValidatorException(INVALID_PASSWORD);
		}


		DAOProvider provider = DAOProvider.getInstance();
		UserDAO userDAO = provider.getUserdao();

		boolean isPasswordChanged = false;
		try{
			isPasswordChanged = userDAO.changePassword(oldPassword, newPassword, userID);
		} catch(DAOException e) {
			throw new ServiceException(e);
		}

		return isPasswordChanged;
	}

}
