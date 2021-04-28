package by.tc.library.controller.command.impl;

import by.tc.library.bean.User;
import by.tc.library.bean.UserRole;
import by.tc.library.controller.command.Command;
import by.tc.library.service.ReturnService;
import by.tc.library.service.ServiceException;
import by.tc.library.service.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class ReturnBook implements Command {

    private static final String PARAM_GIVING_ID = "givingid";
    private final static String ERROR_PAGE = "/WEB-INF/jsp/error.jsp";
    private final static String USER = "user";
    private final static String AUTHORIZATION = "auth";
    private final static String BOOKS_TO_RETURN_REDIRECT = "Controller?command=showbookstoreturn";
    private final static String ERROR_MESSAGE = "Something went wrong";
    private final static String ATTRIBUTE_MESSAGE = "message";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException {
        HttpSession session = request.getSession(true);

        if(null == session.getAttribute(AUTHORIZATION) ||
                ((User)session.getAttribute(USER)).getUserRole() != UserRole.READER){
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(ERROR_PAGE);
            requestDispatcher.forward(request, response);
            return;
        }

        ServiceProvider provider = ServiceProvider.getInstance();
        ReturnService returnService = provider.getReturnService();

        int givindID = Integer.parseInt(request.getParameter(PARAM_GIVING_ID));
        try{
            boolean isBookReturned = returnService.returnBook(givindID);

            if(!isBookReturned){
                session.setAttribute(ATTRIBUTE_MESSAGE, ERROR_MESSAGE);
                response.sendRedirect(BOOKS_TO_RETURN_REDIRECT);
            }
            response.sendRedirect(BOOKS_TO_RETURN_REDIRECT);
        } catch(ServiceException e){
            session.setAttribute(ATTRIBUTE_MESSAGE, ERROR_MESSAGE);
            response.sendRedirect(BOOKS_TO_RETURN_REDIRECT);
        }
    }
}
