package by.tc.library.dao;

import by.tc.library.bean.BookOrder;

import java.util.List;

public interface OrderDAO {
    List<BookOrder> all(int readerID) throws DAOException;
    boolean addOrder(int userID, int bookID) throws DAOException;
    boolean confirmOrder(int operationID, int librarianID) throws DAOException;
    boolean declineOrder(int operationID, int librarianID) throws DAOException;
    List<BookOrder> allToLib() throws DAOException;
}
