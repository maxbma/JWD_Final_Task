package by.tc.library.service.impl;

import by.tc.library.bean.BookGiving;
import by.tc.library.dao.DAOException;
import by.tc.library.dao.DAOProvider;
import by.tc.library.dao.GivingDAO;
import by.tc.library.service.GivingService;
import by.tc.library.service.ServiceException;

import java.util.ArrayList;
import java.util.List;

public class GivingServiceImpl implements GivingService {
    @Override
    public boolean addGiving(int orderID) throws ServiceException {
        DAOProvider provider = DAOProvider.getInstance();
        GivingDAO givingDAO = provider.getGivingDAO();

        boolean isBookGiven = false;
        try{
            isBookGiven = givingDAO.addGiving(orderID);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return isBookGiven;
    }

    @Override
    public List<BookGiving> all(int readerID) throws ServiceException {
        DAOProvider provider = DAOProvider.getInstance();
        GivingDAO givingDAO = provider.getGivingDAO();

        List<BookGiving> booksToReturn = new ArrayList<>();
        try{
            booksToReturn = givingDAO.all(readerID);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return booksToReturn;
    }
}
