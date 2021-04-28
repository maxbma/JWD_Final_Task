package by.tc.library.service;

import by.tc.library.bean.BookReturn;

import java.util.List;

public interface ReturnService {
    List<BookReturn> all(int readerID) throws ServiceException;
    List<BookReturn> allToLib() throws ServiceException;
    boolean returnBook(int givingID) throws ServiceException;
    boolean confirmReturn(int returnID) throws ServiceException;
}
