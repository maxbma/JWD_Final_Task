package by.tc.library.controller.command.impl;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.tc.library.bean.User;
import by.tc.library.bean.UserRole;
import by.tc.library.controller.command.Command;
import by.tc.library.service.BookService;
import by.tc.library.service.ServiceException;
import by.tc.library.service.ServiceProvider;

public class GoToIndexPage implements Command {

	private final static String INDEX_PAGE_REDIRECT = "Controller?command=gotoindexpage";
	private final static String ATTRIBUTE_USER = "user";
	private final static String MAIN_PAGE = "/WEB-INF/jsp/main.jsp";
	private final static String ATTRIBUTE_GENRES = "genres";
	private final static String ATTRIBUTE_MESSAGE = "message";
	private final static String ERROR_MESSAGE = "ERROR";
	private final static String LIBRARIAN_PAGE = "/WEB-INF/jsp/librarian.jsp";
	private final static String ATTRIBUTE_LOCAL = "local";
	private final static String LOCAL_EN = "en";
	private final static String ATTRIBUTE_URL = "url";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();

		String redirectPath = null;


		if(null == session.getAttribute(ATTRIBUTE_USER) ||
				((User)session.getAttribute(ATTRIBUTE_USER)).getUserRole() == UserRole.READER){
			redirectPath = MAIN_PAGE;
		} else {
			redirectPath = LIBRARIAN_PAGE;
		}

		ServiceProvider provider = ServiceProvider.getInstance();
		BookService bookService = provider.getBookService();
		try{
			List<String> bookGenres = bookService.allGenres();

			request.setAttribute(ATTRIBUTE_GENRES, bookGenres);
		} catch(ServiceException e){
			session.setAttribute(ATTRIBUTE_MESSAGE, ERROR_MESSAGE);
			response.sendRedirect(INDEX_PAGE_REDIRECT);
			return;
		}

		if(null == session.getAttribute(ATTRIBUTE_LOCAL)){
			session.setAttribute(ATTRIBUTE_LOCAL, LOCAL_EN);
		}

		session.setAttribute(ATTRIBUTE_URL, INDEX_PAGE_REDIRECT);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(redirectPath);
		requestDispatcher.forward(request, response);
	}

}
