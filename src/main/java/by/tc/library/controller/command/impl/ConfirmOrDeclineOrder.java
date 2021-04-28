package by.tc.library.controller.command.impl;

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

public class ConfirmOrDeclineOrder implements Command {

    private final static String ERROR_PAGE = "/WEB-INF/jsp/error.jsp";
    private final static String USER = "user";
    private final static String AUTHORIZATION = "auth";
    private final static String PARAM_OPERATION_ID = "operationid";
    private final static String OPERATION_TYPE = "optype";
    private final static String ORDERS_TO_LIB_REDIRECT = "Controller?command=showorderstolib";
    private final static String SOMETHING_WENT_WRONG = "Somethin went wrong";
    private final static String ATTRIBUTE_MESSAGE = "message";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException {
        HttpSession session = request.getSession(true);

        if(null == session.getAttribute(AUTHORIZATION) ||
                ((User)session.getAttribute(USER)).getUserRole() != UserRole.LIBRARIAN){
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(ERROR_PAGE);
            requestDispatcher.forward(request, response);
            return;
        }

        int operationID = Integer.parseInt(request.getParameter(PARAM_OPERATION_ID));
        int librarianID = ((User)session.getAttribute(USER)).getUserID();
        String operationType = request.getParameter(OPERATION_TYPE);

        ServiceProvider provider = ServiceProvider.getInstance();
        OrderService orderService = provider.getOrderService();

        try{
            boolean isOrderOperated = orderService.operateOrder(operationID, librarianID, operationType);

            if(!isOrderOperated){
                session.setAttribute(ATTRIBUTE_MESSAGE, SOMETHING_WENT_WRONG);
                response.sendRedirect(ORDERS_TO_LIB_REDIRECT);
                return;
            }

            response.sendRedirect(ORDERS_TO_LIB_REDIRECT);
        } catch (ServiceException e) {
            session.setAttribute(ATTRIBUTE_MESSAGE, SOMETHING_WENT_WRONG);
            response.sendRedirect(ORDERS_TO_LIB_REDIRECT);
            return;
        }

    }
}
