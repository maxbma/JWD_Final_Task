package by.tc.library.controller.command.impl;

import by.tc.library.bean.User;
import by.tc.library.bean.UserRole;
import by.tc.library.controller.command.Command;
import by.tc.library.service.ServiceException;
import by.tc.library.service.ServiceProvider;
import by.tc.library.service.UserService;
import by.tc.library.service.validation.exception.ValidatorException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ChangePassword implements Command {

    private final static String ERROR_PAGE = "/WEB-INF/jsp/error.jsp";
    private final static String OLD_PASSWORD = "oldpassword";
    private final static String NEW_PASSWORD = "newpassword";
    private final static String CONFIRMED_PASSWORD = "newpassword1";
    private final static String USER = "user";
    private final static String AUTHORIZATION = "auth";
    private final static String ATTRIBUTE_LOCAL = "local";
    private final static String ATTRIBUTE_MESSAGE = "message";
    private final static String LOCAL_BASE_NAME = "localisation/local";
    private final static String UNCORRECT_INPUT = "local.uncorrectInputPassChange";
    private final static String REDIRECT_TO_READER_ACC = "Controller?command=gotoreaderaccount";
    private final static String SUCCESSFUL_PASS_CHANGE = "local.successfulPasswordChange";
    private final static String UNSUCC_PASS_CHANGE = "local.unsucPasswordChange";
    private final static String WRONG_PASSWORD = "You entered wrong password";

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

        String oldPassword = request.getParameter(OLD_PASSWORD);
        String newPassword = request.getParameter(NEW_PASSWORD);
        String confirmedPassword = request.getParameter(CONFIRMED_PASSWORD);
        int userID = ((User)session.getAttribute(USER)).getUserID();

        Locale locale = new Locale((String) session.getAttribute(ATTRIBUTE_LOCAL));
        ResourceBundle resourceBundle = ResourceBundle.getBundle(LOCAL_BASE_NAME, locale);

        try{
            boolean isPasswordChanged = userService.changePassword(oldPassword, newPassword, confirmedPassword, userID);
            if(!isPasswordChanged){
                session.setAttribute(ATTRIBUTE_MESSAGE, WRONG_PASSWORD);
                response.sendRedirect(REDIRECT_TO_READER_ACC);
                return;
            }

            session.setAttribute(ATTRIBUTE_MESSAGE, resourceBundle.getString(SUCCESSFUL_PASS_CHANGE));
            response.sendRedirect(REDIRECT_TO_READER_ACC);
        } catch(ServiceException e){
            session.setAttribute(ATTRIBUTE_MESSAGE, resourceBundle.getString(UNSUCC_PASS_CHANGE));
            response.sendRedirect(REDIRECT_TO_READER_ACC);
        } catch (ValidatorException e){
            session.setAttribute(ATTRIBUTE_MESSAGE, e.getMessage());
            response.sendRedirect(REDIRECT_TO_READER_ACC);
        }
    }
}
