package by.tc.library.controller.command.impl;

import by.tc.library.bean.BookReturn;
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
import java.util.List;

public class ShowReturnsToLib implements Command {

    private final static String ERROR_PAGE = "/WEB-INF/jsp/error.jsp";
    private final static String USER = "user";
    private final static String AUTHORIZATION = "auth";
    private final static String ATTRIBUTE_BOOKS_TO_CONFIRM = "bookstoconfirm";
    private final static String ATTRIBUTE_URL = "url";
    private final static String RETURNS_TO_LIB_REDIRECT = "Controller?command=showreturnstolib";
    private final static String RETURNS_TO_LIB = "/WEB-INF/jsp/lib_returns.jsp";
    private final static String ATTRIBUTE_MESSAGE = "message";
    private final static String ERROR_MESSAGE = "Something went wrong";
    private final static String INDEX_PAGE_REDIRECT = "Controller?command=gotoindexpage";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException {
        HttpSession session = request.getSession(true);

        if(null == session.getAttribute(AUTHORIZATION) ||
                ((User)session.getAttribute(USER)).getUserRole() != UserRole.LIBRARIAN){
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(ERROR_PAGE);
            requestDispatcher.forward(request, response);
            return;
        }

        ServiceProvider provider = ServiceProvider.getInstance();
        ReturnService returnService = provider.getReturnService();

        try{
            List<BookReturn> booksToConfirm = returnService.allToLib();

            request.setAttribute(ATTRIBUTE_BOOKS_TO_CONFIRM, booksToConfirm);
            session.setAttribute(ATTRIBUTE_URL, RETURNS_TO_LIB_REDIRECT);

            RequestDispatcher requestDispatcher = request.getRequestDispatcher(RETURNS_TO_LIB);
            requestDispatcher.forward(request, response);
        } catch (ServiceException e){
            session.setAttribute(ATTRIBUTE_MESSAGE, ERROR_MESSAGE);
            response.sendRedirect(INDEX_PAGE_REDIRECT);
        }
    }
}
