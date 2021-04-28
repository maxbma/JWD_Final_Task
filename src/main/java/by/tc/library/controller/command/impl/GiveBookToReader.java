package by.tc.library.controller.command.impl;

import by.tc.library.bean.User;
import by.tc.library.bean.UserRole;
import by.tc.library.controller.command.Command;
import by.tc.library.service.GivingService;
import by.tc.library.service.ServiceException;
import by.tc.library.service.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class GiveBookToReader implements Command {

    private final static String USER = "user";
    private final static String AUTHORIZATION = "auth";
    private final static String ERROR_PAGE = "/WEB-INF/jsp/error.jsp";
    private final static String PARAM_ORDER_ID = "orderid";
    private final static String READER_ORDERS_REDIRECT = "Controller?command=showreaderorders";
    private final static String ATTRIBUTE_MESSAGE = "message";
    private final static String UNABLE_TO_GIVE_BOOK = "Unable to give book right now";

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

        int orderID = Integer.parseInt(request.getParameter(PARAM_ORDER_ID));

        try{
            boolean isBookGiven = givingService.addGiving(orderID);

            if(!isBookGiven){
                session.setAttribute(ATTRIBUTE_MESSAGE, UNABLE_TO_GIVE_BOOK);
                response.sendRedirect(READER_ORDERS_REDIRECT);
            }
            response.sendRedirect(READER_ORDERS_REDIRECT);
        } catch(ServiceException e){
            session.setAttribute(ATTRIBUTE_MESSAGE, UNABLE_TO_GIVE_BOOK);
            response.sendRedirect(READER_ORDERS_REDIRECT);
        }
    }
}
