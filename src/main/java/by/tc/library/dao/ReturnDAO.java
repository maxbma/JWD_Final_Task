package by.tc.library.dao;

import by.tc.library.bean.BookReturn;

import java.util.List;

public interface ReturnDAO {
    List<BookReturn> all(int readerID) throws DAOException;
    List<BookReturn> allToLib() throws DAOException;
    boolean returnBook(int givingID) throws  DAOException;
    boolean confirmReturn(int returnID) throws DAOException;
}
