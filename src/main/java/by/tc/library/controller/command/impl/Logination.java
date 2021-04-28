package by.tc.library.controller.command.impl;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.tc.library.bean.User;
import by.tc.library.controller.command.Command;
import by.tc.library.service.ServiceException;
import by.tc.library.service.ServiceProvider;
import by.tc.library.service.UserService;
import by.tc.library.service.validation.exception.ValidatorException;

public class Logination implements Command {

	private final static String USER = "user";
	private final static String AUTHORIZATION = "auth";
	private final static String ATTRIBUTE_LOCAL = "local";
	private final static String ATTRIBUTE_MESSAGE = "message";
	private final static String LOCAL_BASE_NAME = "localisation/local";
	private final static String ATTRIBUTE_URL = "url";
	private final static String PARAM_LOGIN = "login";
	private final static String PARAM_PASSWORD = "password";
	private final static String WRONG_LOGIN_OR_PASS = "local.wrongLoginOrPass";
	private final static String REDIRECT_SIGN_IN = "Controller?command=signin";
	private final static String MAIN_PAGE = "/WEB-INF/jsp/main.jsp";
	private final static String LIBRARIAN_PAGE = "/WEB-INF/jsp/librarian.jsp";
	private final static String INDEX_PAGE_REDIRECT = "Controller?command=gotoindexpage";
	private final static String ERROR_MESSAGE = "Something went wrong during logination";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(true);

		String login;
		String password;

		login = request.getParameter(PARAM_LOGIN);
		password = request.getParameter(PARAM_PASSWORD);

		ServiceProvider provider = ServiceProvider.getInstance();
		UserService userService = provider.getUserService();

		Locale locale = new Locale((String) session.getAttribute(ATTRIBUTE_LOCAL));
		ResourceBundle resourceBundle = ResourceBundle.getBundle(LOCAL_BASE_NAME, locale);

		User user = null;
		try {
			user = userService.authorization(login, password);
			
			if (user == null) {
				session.setAttribute(ATTRIBUTE_MESSAGE, resourceBundle.getString(WRONG_LOGIN_OR_PASS));
				response.sendRedirect(REDIRECT_SIGN_IN);
				return;
			}

			session.setAttribute(AUTHORIZATION, true);
			session.setAttribute(USER, user);

			session.setAttribute(ATTRIBUTE_URL, INDEX_PAGE_REDIRECT);

			response.sendRedirect(INDEX_PAGE_REDIRECT);
		} catch (ServiceException e) {
			session.setAttribute(ATTRIBUTE_MESSAGE, ERROR_MESSAGE);
			response.sendRedirect(INDEX_PAGE_REDIRECT);
		} catch (ValidatorException e){
			session.setAttribute(ATTRIBUTE_MESSAGE, e.getMessage());
			response.sendRedirect(INDEX_PAGE_REDIRECT);
		}

	}

}
