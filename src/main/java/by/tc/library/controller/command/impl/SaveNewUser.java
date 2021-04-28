package by.tc.library.controller.command.impl;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.tc.library.bean.RegistrationInfo;
import by.tc.library.controller.command.Command;
import by.tc.library.service.ServiceException;
import by.tc.library.service.ServiceProvider;
import by.tc.library.service.UserService;
import by.tc.library.service.validation.exception.ValidatorException;

public class SaveNewUser implements Command {

	private final static String LOCAL_BASE_NAME = "localisation/local";
	private final static String ATTRIBUTE_LOCAL = "local";
	private final static String REGISTRATION_REDIRECT = "Controller?command=registration";
	private final static String INDEX_PAGE_REDIRECT = "Controller?command=gotoindexpage";
	private final static String PARAM_LOGIN = "login";
	private final static String PARAM_PASSWORD = "password";
	private final static String PARAM_NAME = "name";
	private final static String PARAM_SURNAME = "surname";
	private final static String PARAM_ADDRESS = "address";
	private final static String ATTRIBUTE_MESSAGE = "message";
	private final static String LOGIN_EXISTS = "local.loginExists";
	private final static String SUCCESSFUL_REG = "local.succRegistration";
	private final static String UNSUCC_REG = "local.unsuccReg";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);

		String readerLogin = request.getParameter(PARAM_LOGIN);
		String readerPassword = request.getParameter(PARAM_PASSWORD);
		String readerName = request.getParameter(PARAM_NAME);
		String readerSurname = request.getParameter(PARAM_SURNAME);
		String readerAddress = request.getParameter(PARAM_ADDRESS);
		
		RegistrationInfo regInfo = new RegistrationInfo(readerLogin, readerPassword,
				readerName,readerSurname,readerAddress);
		ServiceProvider provider = ServiceProvider.getInstance();
		UserService userService = provider.getUserService();

		Locale locale = new Locale((String) session.getAttribute(ATTRIBUTE_LOCAL));
		ResourceBundle resourceBundle = ResourceBundle.getBundle(LOCAL_BASE_NAME, locale);

		try{
			boolean isUserRegistrated = userService.registration(regInfo);

			if(!isUserRegistrated){
				session.setAttribute(ATTRIBUTE_MESSAGE, resourceBundle.getString(LOGIN_EXISTS));
				response.sendRedirect(REGISTRATION_REDIRECT);
				return;
			}

			session.setAttribute(ATTRIBUTE_MESSAGE, resourceBundle.getString(SUCCESSFUL_REG));
			response.sendRedirect(INDEX_PAGE_REDIRECT);
		} catch (ServiceException e) {
			session.setAttribute(ATTRIBUTE_MESSAGE, resourceBundle.getString(UNSUCC_REG));
			response.sendRedirect(REGISTRATION_REDIRECT);
			return;
		} catch (ValidatorException e){
			session.setAttribute(ATTRIBUTE_MESSAGE, e.getMessage());
			response.sendRedirect(REGISTRATION_REDIRECT);
		}
	}

}
