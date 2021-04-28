package by.tc.library.service;

import by.tc.library.bean.BookOrder;

import java.util.List;

public interface OrderService {
    List<BookOrder> takeAll(int readerID) throws ServiceException;
    List<BookOrder> takeAllToLib() throws ServiceException;
    boolean addOrder(int userID, int bookID) throws ServiceException;
    boolean operateOrder(int operationID, int librarianID, String operationType) throws ServiceException;
}
