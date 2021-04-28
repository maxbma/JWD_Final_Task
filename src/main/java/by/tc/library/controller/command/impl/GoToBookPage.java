package by.tc.library.controller.command.impl;

import by.tc.library.bean.Book;
import by.tc.library.controller.command.Command;
import by.tc.library.service.BookService;
import by.tc.library.service.ServiceException;
import by.tc.library.service.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class GoToBookPage implements Command {

    private final static String ATTRIBUTE_LOCAL = "local";
    private final static String ATTRIBUTE_MESSAGE = "message";
    private final static String LOCAL_EN = "en";
    private final static String PARAM_BOOK_ID = "bookid";
    private final static String ATTRIBUTE_URL ="url";
    private final static String INDEX_PAGE_REDIRECT = "Controller?command=gotoindexpage";
    private final static String BOOK_PAGE = "/WEB-INF/jsp/book.jsp";
    private final static String BOOK_PAGE_REDIRECT = "Controller?command=gotobookpage&bookid=";
    private final static String ERROR_MESSAGE = "Unable to show this book right now";
    private final static String ATTRIBUTE_BOOK_TO_SHOW = "booktoshow";
    private final static String ATTRIBUTE_GENRES = "genres";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        ServiceProvider provider = ServiceProvider.getInstance();
        BookService bookService = provider.getBookService();

        try {
            if(null == session.getAttribute(ATTRIBUTE_LOCAL)){
                session.setAttribute(ATTRIBUTE_MESSAGE, LOCAL_EN);
            }

            Book bookToShow = bookService.takeByID(Integer.parseInt((String) request.getParameter(PARAM_BOOK_ID)));
            List<String> genres = bookService.allGenres();

            if(null == bookToShow){
                session.setAttribute(ATTRIBUTE_MESSAGE, ERROR_MESSAGE);
                response.sendRedirect(INDEX_PAGE_REDIRECT);
                return;
            }

            request.setAttribute(ATTRIBUTE_GENRES, genres);
            request.setAttribute(ATTRIBUTE_BOOK_TO_SHOW, bookToShow);

            session.setAttribute(ATTRIBUTE_URL, BOOK_PAGE_REDIRECT
                    + request.getParameter(PARAM_BOOK_ID));
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(BOOK_PAGE);
            requestDispatcher.forward(request, response);

        } catch (ServiceException e) {
            session.setAttribute(ATTRIBUTE_MESSAGE, ERROR_MESSAGE);
            response.sendRedirect(INDEX_PAGE_REDIRECT);
        }



    }
}
