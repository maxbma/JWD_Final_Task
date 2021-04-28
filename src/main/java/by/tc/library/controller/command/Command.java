package by.tc.library.controller.command;

import by.tc.library.service.ServiceException;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Command {
	
	void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException;

}
