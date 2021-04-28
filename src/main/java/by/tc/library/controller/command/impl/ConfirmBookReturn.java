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

public class ConfirmBookReturn implements Command {

    private final static String ERROR_PAGE = "/WEB-INF/jsp/error.jsp";
    private final static String USER = "user";
    private final static String AUTHORIZATION = "auth";
    private final static String PARAM_RETURN_ID = "returnid";
    private final static String ATTRIBUTE_MESSAGE = "message";
    private final static String RETURNS_TO_LIB_REDIRECT = "Controller?command=showreturnstolib";
    private final static String SOMETHING_WENT_WRONG = "Somethin went wrong";


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



        int returnID = Integer.parseInt(request.getParameter(PARAM_RETURN_ID));
        try{
            boolean isReturnConfirmed = returnService.confirmReturn(returnID);

            if(!isReturnConfirmed){
                session.setAttribute(ATTRIBUTE_MESSAGE, SOMETHING_WENT_WRONG);
                response.sendRedirect(RETURNS_TO_LIB_REDIRECT);
            }

            response.sendRedirect(RETURNS_TO_LIB_REDIRECT);
        } catch (ServiceException e){
            session.setAttribute(ATTRIBUTE_MESSAGE, SOMETHING_WENT_WRONG);
            response.sendRedirect(RETURNS_TO_LIB_REDIRECT);
        }
    }
}
