package by.tc.library.service.impl;

import by.tc.library.bean.BookReturn;
import by.tc.library.dao.DAOException;
import by.tc.library.dao.DAOProvider;
import by.tc.library.dao.ReturnDAO;
import by.tc.library.service.ReturnService;
import by.tc.library.service.ServiceException;

import java.util.List;

public class ReturnServiceImpl implements ReturnService {
    @Override
    public List<BookReturn> all(int readerID) throws ServiceException {
        DAOProvider provider = DAOProvider.getInstance();
        ReturnDAO returnDAO = provider.getReturnDAO();

        List<BookReturn> returnedBooks;

        try{
            returnedBooks = returnDAO.all(readerID);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return returnedBooks;
    }

    @Override
    public List<BookReturn> allToLib() throws ServiceException {
        DAOProvider provider = DAOProvider.getInstance();
        ReturnDAO returnDAO = provider.getReturnDAO();

        List<BookReturn> booksToConfirm;
        try{
            booksToConfirm = returnDAO.allToLib();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return booksToConfirm;
    }

    @Override
    public boolean returnBook(int givingID) throws ServiceException {
        DAOProvider provider = DAOProvider.getInstance();
        ReturnDAO returnDAO = provider.getReturnDAO();

        boolean isBookReturned = false;

        try{
            isBookReturned = returnDAO.returnBook(givingID);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return isBookReturned;
    }

    @Override
    public boolean confirmReturn(int returnID) throws ServiceException {
        DAOProvider provider = DAOProvider.getInstance();
        ReturnDAO returnDAO = provider.getReturnDAO();

        boolean isReturnConfirmed = false;
        try{
            isReturnConfirmed = returnDAO.confirmReturn(returnID);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return  isReturnConfirmed;
    }
}
