package by.tc.library.service.impl;

import by.tc.library.bean.BookOrder;
import by.tc.library.dao.DAOException;
import by.tc.library.dao.DAOProvider;
import by.tc.library.dao.OrderDAO;
import by.tc.library.service.OrderService;
import by.tc.library.service.ServiceException;

import java.util.List;

public class OrderServiceImpl implements OrderService {
    @Override
    public List<BookOrder> takeAll(int readerID) throws ServiceException {
        DAOProvider provider = DAOProvider.getInstance();
        OrderDAO orderDAO = provider.getOrderDAO();

        List<BookOrder> orders;

        try {
            orders = orderDAO.all(readerID);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return orders;
    }

    @Override
    public List<BookOrder> takeAllToLib() throws ServiceException {
        DAOProvider provider = DAOProvider.getInstance();
        OrderDAO orderDAO = provider.getOrderDAO();

        List<BookOrder> orders;
        try {
            orders = orderDAO.allToLib();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return orders;
    }

    @Override
    public boolean addOrder(int userID, int bookID) throws ServiceException {

        DAOProvider provider = DAOProvider.getInstance();
        OrderDAO orderDAO = provider.getOrderDAO();

        boolean isOrderRegistrated = false;

        try{
            isOrderRegistrated = orderDAO.addOrder(userID, bookID);
        } catch (DAOException e) {
            throw new ServiceException("error");
        }

        return isOrderRegistrated;
    }

    @Override
    public boolean operateOrder(int operationID, int librarianID, String operationType) throws ServiceException {
        DAOProvider provider = DAOProvider.getInstance();
        OrderDAO orderDAO = provider.getOrderDAO();

        boolean isOrderOperated = false;

        try{
            if("confirm".equals(operationType)){
                isOrderOperated = orderDAO.confirmOrder(operationID, librarianID);
            } else {
                isOrderOperated = orderDAO.declineOrder(operationID, librarianID);
            }
        } catch (DAOException e) {
            throw new ServiceException("error");
        }

        return isOrderOperated;
    }


}
