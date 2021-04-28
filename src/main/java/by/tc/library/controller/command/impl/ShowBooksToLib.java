package by.tc.library.controller.command.impl;

import by.tc.library.bean.Book;
import by.tc.library.bean.User;
import by.tc.library.bean.UserRole;
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

public class ShowBooksToLib implements Command {

    private final static String ERROR_PAGE = "/WEB-INF/jsp/error.jsp";
    private final static String USER = "user";
    private final static String AUTHORIZATION = "auth";
    private final static String ATTRIBUTE_BOOKS = "books";
    private final static String ATTRIBUTE_URL = "url";
    private final static String BOOKS_TO_LIB_REDIRECT = "Controller?command=showbookstolib";
    private final static String LIBRARIAN_BOOK_LIST = "/WEB-INF/jsp/book_list_lib.jsp";
    private final static String ATTRIBUTE_MESSAGE = "message";
    private final static String ERROR_MESSAGE = "Unable to show book list";
    private final static String INDEX_PAGE_REDIRECT = "Controller?command=gotoindexpage";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException {
        HttpSession session = request.getSession(true);

        if(null == session.getAttribute(AUTHORIZATION) ||
                ((User)session.getAttribute(USER)).getUserRole() != UserRole.LIBRARIAN){
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(ERROR_PAGE);
            requestDispatcher.forward(request, response);
            return;
        }

        ServiceProvider serviceProvider = ServiceProvider.getInstance();
        BookService bookService = serviceProvider.getBookService();

        try{
            List<Book> books = bookService.takeAll();

            request.setAttribute(ATTRIBUTE_BOOKS, books);
            session.setAttribute(ATTRIBUTE_URL, BOOKS_TO_LIB_REDIRECT);

            RequestDispatcher requestDispatcher = request.getRequestDispatcher(LIBRARIAN_BOOK_LIST);
            requestDispatcher.forward(request, response);
        } catch(ServiceException e){
            session.setAttribute(ATTRIBUTE_MESSAGE, ERROR_MESSAGE);
            response.sendRedirect(INDEX_PAGE_REDIRECT);
        }
    }
}
