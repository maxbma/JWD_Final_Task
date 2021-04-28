package by.tc.library.service;

import by.tc.library.bean.BookGiving;

import java.util.List;

public interface GivingService {
    boolean addGiving(int orderID) throws ServiceException;
    List<BookGiving> all(int readerID) throws ServiceException;
}
