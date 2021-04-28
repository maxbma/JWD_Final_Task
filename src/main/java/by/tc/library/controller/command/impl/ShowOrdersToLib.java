package by.tc.library.controller.command.impl;

import by.tc.library.bean.BookOrder;
import by.tc.library.bean.User;
import by.tc.library.bean.UserRole;
import by.tc.library.controller.command.Command;

import by.tc.library.service.OrderService;
import by.tc.library.service.ServiceException;
import by.tc.library.service.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class ShowOrdersToLib implements Command {

    private final static String ERROR_PAGE = "/WEB-INF/jsp/error.jsp";
    private final static String USER = "user";
    private final static String AUTHORIZATION = "auth";
    private final static String ATTRIBUTE_ORDERS = "orders";
    private final static String ATTRIBUTE_URL = "url";
    private final static String ORDERS_TO_LIB_REDIRECT = "Controller?command=showorderstolib";
    private final static String ACTIVE_ORDERS = "/WEB-INF/jsp/active_orders.jsp";
    private final static String INDEX_PAGE_REDIRECT = "Controller?command=gotoindexpage";
    private final static String ATTRIBUTE_MESSAGE = "message";
    private final static String ERROR_MESSAGE = "Something went wrong";


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
        OrderService orderService = provider.getOrderService();

        try {
            List<BookOrder> orders = orderService.takeAllToLib();

            request.setAttribute(ATTRIBUTE_ORDERS, orders);

            session.setAttribute(ATTRIBUTE_URL, ORDERS_TO_LIB_REDIRECT);

            RequestDispatcher requestDispatcher = request.getRequestDispatcher(ACTIVE_ORDERS);
            requestDispatcher.forward(request, response);
        } catch (ServletException e) {
            session.setAttribute(ATTRIBUTE_MESSAGE, ERROR_MESSAGE);
            response.sendRedirect(INDEX_PAGE_REDIRECT);
        }
    }
}
