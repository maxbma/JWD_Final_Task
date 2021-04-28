package by.tc.library.controller.command.impl;

import by.tc.library.bean.BookOrder;
import by.tc.library.bean.User;
import by.tc.library.bean.UserRole;
import by.tc.library.controller.command.Command;
import by.tc.library.service.OrderService;
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

public class ShowReaderOrders implements Command {

    private final static String ERROR_PAGE = "/WEB-INF/jsp/error.jsp";
    private final static String USER = "user";
    private final static String AUTHORIZATION = "auth";
    private final static String PARAM_ID = "id";
    private final static String ATTRIBUTE_ORDERS = "orders";
    private final static String ATTRIBUTE_URL = "url";
    private final static String READER_ORDERS_REDIRECT = "Controller?command=showreaderorders";
    private final static String READER_ORDERS = "/WEB-INF/jsp/reader_orders.jsp";

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
        UserService userService = provider.getUserService();
        OrderService orderService = provider.getOrderService();

        int readerID = 0;
        try {
            if (null == request.getParameter(PARAM_ID)) {
                int userID = ((User) session.getAttribute(USER)).getUserID();
                readerID = userService.getReaderID(userID);
            } else {
                readerID = Integer.parseInt(request.getParameter(PARAM_ID));
            }

            List<BookOrder> orders = orderService.takeAll(readerID);

            request.setAttribute(ATTRIBUTE_ORDERS, orders);
            session.setAttribute(ATTRIBUTE_URL, READER_ORDERS_REDIRECT);


            RequestDispatcher requestDispatcher = request.getRequestDispatcher(READER_ORDERS);
            requestDispatcher.forward(request, response);

        } catch (ServiceException e) {
            response.sendRedirect((String) session.getAttribute(ATTRIBUTE_URL));
        }
    }
}
