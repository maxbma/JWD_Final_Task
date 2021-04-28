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

public class ShowPopular implements Command {

    private final static String ATTRIBUTE_POPULAR = "popular";
    private final static String ATTRIBUTE_URL = "url";
    private final static String POPULAR_REDIRECT = "Controller?command=showpopular";
    private final static String POPULAR_BOOKS = "/WEB-INF/jsp/popular_books.jsp";
    private final static String INDEX_PAGE_REDIRECT = "Controller?commmand=gotoindexpage";
    private final static String ATTRIBUTE_MESSAGE = "message";
    private final static String ERROR_MESSAGE = "Something went wrong";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException {
        HttpSession session = request.getSession(true);

        ServiceProvider serviceProvider = ServiceProvider.getInstance();
        BookService bookService = serviceProvider.getBookService();

        try{
            List<Book> popularBooks = bookService.getPopular();

            request.setAttribute(ATTRIBUTE_POPULAR, popularBooks);

            session.setAttribute(ATTRIBUTE_URL, POPULAR_REDIRECT);

            RequestDispatcher requestDispatcher = request.getRequestDispatcher(POPULAR_BOOKS);
            requestDispatcher.forward(request, response);

        } catch (ServiceException e){
            session.setAttribute(ATTRIBUTE_MESSAGE, ERROR_MESSAGE);
            response.sendRedirect(INDEX_PAGE_REDIRECT);
        }
    }
}
