package by.tc.library.dao.impl;

import by.tc.library.bean.RegistrationInfo;
import by.tc.library.bean.User;
import by.tc.library.controller.listener.ContextListener;
import by.tc.library.dao.DAOException;
import by.tc.library.dao.DAOProvider;
import by.tc.library.dao.UserDAO;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUserDAO {
    private final static DAOProvider provider = DAOProvider.getInstance();
    private final static UserDAO userDAO = provider.getUserdao();

    private final static String LOGIN = "login";
    private final static String PASSWORD = "password";
    private final static String NEW_PASSWORD = "new_password";
    private final static String ROLE_READER = "READER";
    private final static String UNCORRECT_PASSWORD = "uncorrect_password";

    @Test
    public void test1Authorization() throws DAOException{

        User expected = new User(ROLE_READER, 1);
        User actual = userDAO.authorization(LOGIN, PASSWORD);
        assertEquals(expected,actual);
    }

    @Test
    public void test2NegativeAuthorization() throws DAOException{
        User unexpected = new User(ROLE_READER, 1);
        User actual = userDAO.authorization(LOGIN, UNCORRECT_PASSWORD);
        assertNotEquals(unexpected, actual);
    }

    @Test
    public void test3ChangePassword() throws DAOException{
        boolean expected = true;
        boolean actual = userDAO.changePassword(PASSWORD, NEW_PASSWORD, 1);
        assertEquals(expected,actual);
    }

    @Test
    public void test4AuthorizationWithNewPassword() throws DAOException{
        User expected = new User(ROLE_READER, 1);
        User actual = userDAO.authorization(LOGIN, NEW_PASSWORD);
        assertEquals(expected,actual);
    }

    @Test
    public void test5ChangePassword() throws DAOException{
        boolean expected = true;
        boolean actual = userDAO.changePassword(NEW_PASSWORD, PASSWORD, 1);
        assertEquals(expected,actual);
    }
}
