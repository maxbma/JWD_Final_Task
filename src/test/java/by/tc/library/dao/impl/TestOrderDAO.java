package by.tc.library.dao.impl;

import by.tc.library.bean.BookOrder;
import by.tc.library.controller.listener.ContextListener;
import by.tc.library.dao.DAOException;
import by.tc.library.dao.DAOProvider;
import by.tc.library.dao.OrderDAO;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestOrderDAO {
    private final static DAOProvider provider = DAOProvider.getInstance();
    private final static OrderDAO orderDAO = provider.getOrderDAO();

    @Test
    public void test1AllOrders() throws DAOException{

        List<BookOrder> expected = new ArrayList<>();
        BookOrder order1 = new BookOrder("Название", 1, 1, "Имя",
                "Фамилия", "login");
        order1.setOrderStatus("отклонена");
        expected.add(order1);
        BookOrder order2 = new BookOrder("Название", 1, 2, "Имя",
                "Фамилия", "login");
        order2.setOrderStatus("ожидание");
        expected.add(order2);
        List<BookOrder> actual = orderDAO.all(1);
        assertEquals(expected,actual);
    }

    @Test
    public void test2AllOrdersToLib() throws DAOException{
        List<BookOrder> expected = new ArrayList<>();
        BookOrder order = new BookOrder("Название", 1, 2, "Имя",
                "Фамилия", "login");
        order.setOrderStatus("ожидание");
        expected.add(order);
        List<BookOrder> actual = orderDAO.allToLib();
        assertEquals(expected,actual);
    }
}
