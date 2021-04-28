package by.tc.library.controller.command.impl;

import by.tc.library.bean.BookReturn;
import by.tc.library.bean.User;
import by.tc.library.bean.UserRole;
import by.tc.library.controller.command.Command;
import by.tc.library.service.ReturnService;
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

public class ShowReaderHistory implements Command {

    private final static String AUTHORIZATION = "auth";
    private final static String ERROR_PAGE = "/WEB-INF/jsp/error.jsp";
    private final static String USER = "user";
    private final static String ATTRIBUTE_URL = "url";
    private final static String PARAM_ID = "id";
    private final static String ATTRIBUTE_HISTORY = "history";
    private final static String READER_HISTORY_REDIRECT = "Controller?command=showreaderhistory";
    private final static String READER_HISTORY_REDIRECT_TO_LIB = "Controller?command=showreaderhistory&id=";
    private final static String ATTRIBUTE_MESSAGE = "message";
    private final static String ERROR_MESSAGE = "Something went wrong";
    private final static String READER_HISTORY = "/WEB-INF/jsp/reader_history.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException {

        HttpSession session = request.getSession(true);

        if(null == session.getAttribute(AUTHORIZATION) ||
                (((User)session.getAttribute(USER)).getUserRole() == UserRole.READER &&
                        request.getParameter(PARAM_ID) != null)) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(ERROR_PAGE);
            requestDispatcher.forward(request, response);
            return;
        }

        ServiceProvider provider = ServiceProvider.getInstance();
        ReturnService returnService = provider.getReturnService();
        UserService userService = provider.getUserService();

        try{
            int readerID = 0;
            if(null != request.getParameter(PARAM_ID)){
                readerID = Integer.parseInt(request.getParameter(PARAM_ID));
            } else{
                int userID = ((User)session.getAttribute(USER)).getUserID();
                readerID = userService.getReaderID(userID);
            }

            List<BookReturn> history = returnService.all(readerID);

            request.setAttribute(ATTRIBUTE_HISTORY, history);

            if(((User)session.getAttribute(USER)).getUserRole() == UserRole.READER){
                session.setAttribute(ATTRIBUTE_URL, READER_HISTORY_REDIRECT);
            } else {
                session.setAttribute(ATTRIBUTE_URL, READER_HISTORY_REDIRECT_TO_LIB + readerID);
            }

            RequestDispatcher requestDispatcher = request.getRequestDispatcher(READER_HISTORY);
            requestDispatcher.forward(request, response);

        } catch (ServiceException e) {
            session.setAttribute(ATTRIBUTE_MESSAGE, ERROR_MESSAGE);
            response.sendRedirect((String) session.getAttribute(ATTRIBUTE_URL));
        }
    }
}
