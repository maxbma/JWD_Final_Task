package by.tc.library.controller.command.impl;

import by.tc.library.bean.User;
import by.tc.library.controller.command.Command;
import by.tc.library.service.OrderService;
import by.tc.library.service.ServiceException;
import by.tc.library.service.ServiceProvider;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class RegistrateOrder implements Command {

    private final static String INDEX_PAGE_REDIRECT = "Controller?command=gotoindexpage";
    private final static String ERROR_MESSAGE = "ERROR";
    private final static String ATTRIBUTE_MESSAGE = "message";
    private final static String BOOK_PAGE_REDIRECT = "Controller?command=gotobookpage&bookid=";
    private final static String AUTH_MESSAGE = "Login first to order";
    private final static String AUTHORIZATION = "auth";
    private final static String PARAM_BOOK_ID = "bookid";
    private final static String USER = "user";
    private final static String LOCAL_BASE_NAME = "localisation/local";
    private final static String ATTRIBUTE_LOCAL = "local";
    private final static String BOOK_IS_NOT_AVAILABLE = "local.bookIsNotAvailable";
    private final static String SUCCESSFULL_ORDER = "local.successfulOrder";


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException {

        HttpSession session = request.getSession();

        if(session == null) {
            session.setAttribute(ATTRIBUTE_MESSAGE, ERROR_MESSAGE);
            response.sendRedirect(INDEX_PAGE_REDIRECT);
            return;
        }

        Boolean isAuth = (Boolean) session.getAttribute(AUTHORIZATION);

        if (isAuth == null || !isAuth) {
            response.sendRedirect(BOOK_PAGE_REDIRECT + request.getParameter(PARAM_BOOK_ID) + AUTH_MESSAGE);
            return;
        }

        int userID = ((User)session.getAttribute(USER)).getUserID();
        int bookID = Integer.parseInt(request.getParameter(PARAM_BOOK_ID));

        ServiceProvider provider = ServiceProvider.getInstance();
        OrderService orderService = provider.getOrderService();

        Locale locale = new Locale((String) session.getAttribute(ATTRIBUTE_LOCAL));
        ResourceBundle resourceBundle = ResourceBundle.getBundle(LOCAL_BASE_NAME, locale);

        try{
            boolean isOrderRegistrated = orderService.addOrder(userID,bookID);

            if(!isOrderRegistrated){
                session.setAttribute(ATTRIBUTE_MESSAGE, resourceBundle.getString(BOOK_IS_NOT_AVAILABLE));
                response.sendRedirect(BOOK_PAGE_REDIRECT + bookID);
                return;
            }

            session.setAttribute(ATTRIBUTE_MESSAGE, resourceBundle.getString(SUCCESSFULL_ORDER));
            response.sendRedirect(BOOK_PAGE_REDIRECT + bookID);
        } catch (ServiceException e) {
            session.setAttribute(ATTRIBUTE_MESSAGE, ERROR_MESSAGE);
            response.sendRedirect(BOOK_PAGE_REDIRECT + bookID);
            return;
        }

    }
}
