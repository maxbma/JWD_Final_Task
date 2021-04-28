package by.tc.library.dao;

import by.tc.library.bean.BookGiving;

import java.util.List;

public interface GivingDAO {
    boolean addGiving(int orderID) throws DAOException;
    List<BookGiving> all(int readerID) throws DAOException;
}
