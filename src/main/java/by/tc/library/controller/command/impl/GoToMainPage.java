package by.tc.library.controller.command.impl;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.tc.library.controller.command.Command;
import by.tc.library.service.BookService;
import by.tc.library.service.ServiceException;
import by.tc.library.service.ServiceProvider;

public class GoToMainPage implements Command {

	private final static String ERROR_MESSAGE = "ERROR";
	private final static String INDEX_PAGE_REDIRECT = "Controller?command=gotoindexpage";
	private final static String ATTRIBUTE_MESSAGE = "message";
	private final static String ATTRIBUTE_GENRES = "genres";
	private final static String ATTRIBUTE_URL = "url";
	private final static String MAIN_PAGE_REDIRECT = "Controller?command=gotomainpage";
	private final static String MAIN_PAGE ="/WEB-INF/jsp/main.jsp";


	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();

		if(session == null) {
			session.setAttribute(ATTRIBUTE_MESSAGE, ERROR_MESSAGE);
			response.sendRedirect(INDEX_PAGE_REDIRECT);
			return;			
		}

		ServiceProvider serviceProvider = ServiceProvider.getInstance();
		BookService bookService = serviceProvider.getBookService();

		try{
			List<String> bookGenres = bookService.allGenres();

			request.setAttribute(ATTRIBUTE_GENRES, bookGenres);

			session.setAttribute(ATTRIBUTE_URL,MAIN_PAGE_REDIRECT);

			RequestDispatcher requestDispatcher = request.getRequestDispatcher(MAIN_PAGE);
			requestDispatcher.forward(request, response);
		} catch(ServiceException e){
			session.setAttribute(ATTRIBUTE_MESSAGE, ERROR_MESSAGE);
			response.sendRedirect(INDEX_PAGE_REDIRECT);
		}


	}

}
