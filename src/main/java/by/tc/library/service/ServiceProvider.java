package by.tc.library.service;

import by.tc.library.service.impl.*;

public final class ServiceProvider {
	
	private static final ServiceProvider instance = new ServiceProvider(); 

	private ServiceProvider() {}
	
	private final UserService userService = new UserServiceImpl();
	private final BookService bookService = new BookServiceImpl();
	private final OrderService orderService = new OrderServiceImpl();
	private final GivingService givingService = new GivingServiceImpl();
	private final ReturnService returnService = new ReturnServiceImpl();
	
	public static ServiceProvider getInstance() {
		return instance;
	}

	public UserService getUserService() {
		return userService;
	}

	public BookService getBookService() {
		return bookService;
	}

	public GivingService getGivingService() {
		return givingService;
	}

	public OrderService getOrderService() {
		return orderService;
	}

	public ReturnService getReturnService() {
		return returnService;
	}
}
