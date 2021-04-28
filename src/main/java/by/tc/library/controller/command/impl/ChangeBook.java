package by.tc.library.controller.command.impl;

import by.tc.library.bean.Book;
import by.tc.library.bean.User;
import by.tc.library.bean.UserRole;
import by.tc.library.controller.command.Command;
import by.tc.library.service.BookService;
import by.tc.library.service.ServiceException;
import by.tc.library.service.ServiceProvider;
import by.tc.library.service.validation.exception.ValidatorException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ChangeBook implements Command {

    private final static String ERROR_PAGE = "/WEB-INF/jsp/error.jsp";
    private final static String USER = "user";
    private final static String AUTHORIZATION = "auth";
    private final static String ATTRIBUTE_LOCAL = "local";
    private final static String ATTRIBUTE_MESSAGE = "message";
    private final static String LOCAL_BASE_NAME = "localisation/local";
    private final static String PARAM_PICTURE = "picture";
    private final static String PARAM_BOOK_ID = "bookid";
    private final static String PARAM_BOOK_NAME = "bookname";
    private final static String PARAM_AUTHOR_NAME = "authorname";
    private final static String PARAM_AUTHOR_SURNAME = "authorsurname";
    private final static String PARAM_DESCRIPTION = "description";
    private final static String PARAM_RELEASE_YEAR = "releaseyear";

    private final static String PARAM_NEW_BOOK_NAME = "newbookname";
    private final static String PARAM_NEW_AUTHOR_NAME = "newauthorname";
    private final static String PARAM_NEW_AUTHOR_SURNAME = "newauthorsurname";
    private final static String PARAM_NEW_DESCRIPTION = "newdescription";
    private final static String PARAM_NEW_RELEASE_YEAR = "newreleaseyear";
    private final static String SUCCESSFUL_BOOK_CHANGE = "local.successfulBookChange";
    private final static String UNSUCC_BOOK_CHANGE = "local.unsuccBookChange";
    private final static String BOOK_PAGE_REDIRECT = "Controller?command=gotobookpage&bookid=";
    private final static String BOOKS_TO_LIB_REDIRECT = "Controller?command=showbookstolib";


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

        Locale locale = new Locale((String) session.getAttribute(ATTRIBUTE_LOCAL));
        ResourceBundle resourceBundle = ResourceBundle.getBundle(LOCAL_BASE_NAME, locale);

        try{
            List<String> allGenres = bookService.allGenres();
            List<String> bookGenres = new ArrayList<>();
            for(String genre : allGenres){
                if(null != request.getParameter(genre)){
                    bookGenres.add(request.getParameter(genre));
                }
            }
            String picture = request.getParameter(PARAM_PICTURE);
            int id = Integer.parseInt(request.getParameter(PARAM_BOOK_ID));

            String bookName = request.getParameter(PARAM_BOOK_NAME);
            String authorName = request.getParameter(PARAM_AUTHOR_NAME);
            String authorSurname = request.getParameter(PARAM_AUTHOR_SURNAME);
            String description = request.getParameter(PARAM_DESCRIPTION);
            int releaseYear = Integer.parseInt(request.getParameter(PARAM_RELEASE_YEAR));

            Book oldBook = new Book(id, bookName, authorSurname, authorName,
                    releaseYear, description, picture);

            String newBookName = request.getParameter(PARAM_NEW_BOOK_NAME);
            String newAuthorName = request.getParameter(PARAM_NEW_AUTHOR_NAME);
            String newAuthorSurname = request.getParameter(PARAM_NEW_AUTHOR_SURNAME);
            String newDescription = request.getParameter(PARAM_NEW_DESCRIPTION);
            int newReleaseYear = Integer.parseInt(request.getParameter(PARAM_NEW_RELEASE_YEAR));

            Book newBook = new Book(id, newBookName, newAuthorSurname, newAuthorName,
                    newReleaseYear, newDescription, picture);
            newBook.setGenres(bookGenres);

            boolean isBookChanged = bookService.changeBook(oldBook, newBook);

            session.setAttribute(ATTRIBUTE_MESSAGE, resourceBundle.getString(SUCCESSFUL_BOOK_CHANGE));
            response.sendRedirect(BOOK_PAGE_REDIRECT + id);
        }catch (ServiceException e){
            session.setAttribute(ATTRIBUTE_MESSAGE, resourceBundle.getString(UNSUCC_BOOK_CHANGE));
            response.sendRedirect(BOOKS_TO_LIB_REDIRECT);
        }
        catch (ValidatorException e){
            session.setAttribute(ATTRIBUTE_MESSAGE, e.getMessage());
            response.sendRedirect(BOOKS_TO_LIB_REDIRECT);
        }
    }
}
