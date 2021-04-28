package by.tc.library.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.tc.library.controller.command.Command;
import by.tc.library.controller.command.CommandProvider;
import by.tc.library.service.ServiceException;

@MultipartConfig
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String PARAM_COMMAND = "command";
	private final static String ERROR_PAGE = "/WEB-INF/jsp/error.jsp";
	
	private final CommandProvider provider = new CommandProvider();

	public Controller() {
		super();
	}
		
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);
	}
	
	private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String name;
		Command command;
		
		name = request.getParameter(PARAM_COMMAND);
		command = provider.takeCommand(name);

		try {
			command.execute(request,response);
		} catch (ServiceException e) {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(ERROR_PAGE);
			requestDispatcher.forward(request, response);
		}
	}

}
