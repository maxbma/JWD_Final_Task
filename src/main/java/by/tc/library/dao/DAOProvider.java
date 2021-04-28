package by.tc.library.dao;

import by.tc.library.dao.impl.*;

public final class DAOProvider {

	private static final DAOProvider instance = new DAOProvider();	
	
	private final UserDAO userdao = new SQLUserDAO();
	private final BookDAO bookDAO = new SQLBookDAO();
	private final OrderDAO orderDAO = new SQLOrderDAO();
	private final GivingDAO givingDAO = new SQLGivingDAO();
	private final ReturnDAO returnDAO = new SQLReturnDAO();
	
	private DAOProvider() {}
	
	public static DAOProvider getInstance() {
		return instance;
	}

	public UserDAO getUserdao() {
		return userdao;
	}

	public BookDAO getBookDAO() {
		return bookDAO;
	}

	public GivingDAO getGivingDAO() {
		return givingDAO;
	}

	public OrderDAO getOrderDAO() {
		return orderDAO;
	}

	public ReturnDAO getReturnDAO() {
		return returnDAO;
	}
}
