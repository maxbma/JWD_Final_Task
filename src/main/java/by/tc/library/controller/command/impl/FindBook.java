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

public class FindBook implements Command {

    private static final String PARAM_NAME_TO_FIND = "nameToFind";
    private static final String PARAM_SELECTED_GENRE = "selectedgenre";
    private static final String MAIN_PAGE_REDIRECT = "Controller?command=gotomainpage";
    private static final String ATTRIBUTE_MESSAGE = "message";
    private static final String MESSAGE_BOOK_NOT_FOUND = "There are no such books in our library";
    private static final String ATTRIBUTE_FOUND_BOOKS = "foundBooks";
    private static final String ATTRIBUTE_GENRES = "genres";
    private static final String MAIN_PAGE = "/WEB-INF/jsp/main.jsp";
    private static final String SOMETHING_WENT_WRONG = "Something went wrong";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        ServiceProvider provider = ServiceProvider.getInstance();
        BookService bookService = provider.getBookService();

        String bookOrAuthor = request.getParameter(PARAM_NAME_TO_FIND);
        String genreToFind = request.getParameter(PARAM_SELECTED_GENRE);
        try {
            List<Book> foundBooks = bookService.takeFound(bookOrAuthor, genreToFind);

            if(foundBooks.isEmpty()){
                session.setAttribute(ATTRIBUTE_MESSAGE, MESSAGE_BOOK_NOT_FOUND);
                response.sendRedirect(MAIN_PAGE_REDIRECT);
                return;
            }

            request.setAttribute(ATTRIBUTE_FOUND_BOOKS, foundBooks);
            List<String> bookGenres = bookService.allGenres();

            request.setAttribute(ATTRIBUTE_GENRES, bookGenres);

            RequestDispatcher requestDispatcher = request.getRequestDispatcher(MAIN_PAGE);
            requestDispatcher.forward(request, response);

        } catch (ServiceException e) {
            session.setAttribute(ATTRIBUTE_MESSAGE, SOMETHING_WENT_WRONG);
            response.sendRedirect(MAIN_PAGE_REDIRECT);
        }
    }
}
