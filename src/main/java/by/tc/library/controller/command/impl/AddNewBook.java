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
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@MultipartConfig
public class AddNewBook implements Command {

    private static final String SAVE_DIRECTORY = "E:/УЧЁБА/Проекты Java/213/src/main/webapp/css/img";
    private static final String PART_PHOTO = "photo";
    private static final String ID = "id";
    private static final String JPG = ".jpg";
    private static final String PARAM_BOOK_NAME = "bookname";
    private static final String PARAM_AUTHOR_NAME = "authorname";
    private static final String PARAM_AUTHOR_SURNAME = "authorsurname";
    private static final String PARAM_RELEASE_YEAR = "releaseyear";
    private static final String PARAM_DESCRIPTION = "description";
    private static final String DEFAULT_PICTURE = "default.jpg";
    private static final String ATTRIBUTE_MESSAGE = "message";
    private static final String BOOK_PAGE_REDIRECT = "Controller?command=gotobookpage&bookid=";
    private static final String INDEX_PAGE_REDIRECT = "Controller?command=gotoindexpage";
    private final static String ERROR_PAGE = "/WEB-INF/jsp/error.jsp";
    private final static String USER = "user";
    private final static String AUTHORIZATION = "auth";

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

        String bookName = request.getParameter(PARAM_BOOK_NAME);
        String authorName = request.getParameter(PARAM_AUTHOR_NAME);
        String authorSurname = request.getParameter(PARAM_AUTHOR_SURNAME);
        String description = request.getParameter(PARAM_DESCRIPTION);
        int releaseYear = Integer.parseInt(request.getParameter(PARAM_RELEASE_YEAR));
        Part photo = request.getPart(PART_PHOTO);
        int bookID = 0;

        try{
            List<String> allGenres = bookService.allGenres();
            List<String> bookGenres = new ArrayList<>();
            for(String genre : allGenres){
                if(null != request.getParameter(genre)){
                    bookGenres.add(request.getParameter(genre));
                }
            }
            String picture = null;
            bookID = bookService.bookAmount() + 1;
            boolean isPictureAdded = false;
            if(photo.getSize() > 0){
                picture = ID + bookID + JPG;
                isPictureAdded = true;
            } else {
                picture = DEFAULT_PICTURE;
            }
            Book bookToAdd = new Book(bookID, bookName, authorSurname,
                    authorName, releaseYear, description, picture);
            bookToAdd.setGenres(bookGenres);

            boolean isBookAdded = bookService.addBook(bookToAdd);

            if(isBookAdded && isPictureAdded){
                String fileName = SAVE_DIRECTORY + File.separator + picture;
                photo.write(fileName);
            }

            response.sendRedirect(BOOK_PAGE_REDIRECT + bookID);
        } catch(ServiceException e){
            response.sendRedirect(INDEX_PAGE_REDIRECT);
        } catch(ValidatorException e){
            session.setAttribute(ATTRIBUTE_MESSAGE, e.getMessage());
            response.sendRedirect(INDEX_PAGE_REDIRECT);
        }
    }
}
