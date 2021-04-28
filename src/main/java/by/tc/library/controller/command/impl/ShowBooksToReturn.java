package by.tc.library.controller.command.impl;

import by.tc.library.bean.BookGiving;
import by.tc.library.bean.User;
import by.tc.library.bean.UserRole;
import by.tc.library.controller.command.Command;
import by.tc.library.service.GivingService;
import by.tc.library.service.ServiceException;
import by.tc.library.service.ServiceProvider;
import by.tc.library.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class ShowBooksToReturn implements Command {

    private final static String ERROR_PAGE = "/WEB-INF/jsp/error.jsp";
    private final static String USER = "user";
    private final static String AUTHORIZATION = "auth";
    private final static String ATTRIBUTE_BOOKS_TO_RETURN = "bookstoreturn";
    private final static String ATTRIBUTE_URL = "url";
    private final static String BOOKS_TO_RETURN_REDIRECT = "Controller?command=showbookstoreturn";
    private final static String BOOKS_TO_RETURN = "/WEB-INF/jsp/books_to_return.jsp";
    private final static String ERROR_MESSAGE = "Something went wrong";
    private final static String READER_ACCOUNT_REDIRECT = "Controller?command=gotoreaderaccount";
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
        GivingService givingService = provider.getGivingService();
        UserService userService = provider.getUserService();

        int readerID = userService.getReaderID(((User)session.getAttribute(USER)).getUserID());
        try{
            List<BookGiving> booksToReturn = givingService.all(readerID);

            request.setAttribute(ATTRIBUTE_BOOKS_TO_RETURN, booksToReturn);

            session.setAttribute(ATTRIBUTE_URL, BOOKS_TO_RETURN_REDIRECT);

            RequestDispatcher requestDispatcher = request.getRequestDispatcher(BOOKS_TO_RETURN);
            requestDispatcher.forward(request, response);
        } catch(ServiceException e){
            session.setAttribute(ATTRIBUTE_MESSAGE, ERROR_MESSAGE);
            response.sendRedirect(READER_ACCOUNT_REDIRECT);
        }
    }
}
