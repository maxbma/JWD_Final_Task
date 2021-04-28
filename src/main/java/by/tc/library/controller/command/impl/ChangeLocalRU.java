package by.tc.library.controller.command.impl;

import by.tc.library.controller.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ChangeLocalRU implements Command {
    private final static String ATTRIBUTE_LOCAL = "local";
    private final static String ATTRIBUTE_URL = "url";
    private final static String LOCAL_RU = "ru";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        session.setAttribute(ATTRIBUTE_LOCAL, LOCAL_RU);

        String requestPath = (String) session.getAttribute(ATTRIBUTE_URL);
        response.sendRedirect(requestPath);
    }
}
