package by.tc.library.dao.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        TestBookDAO.class,
        TestUserDAO.class,
        TestOrderDAO.class
})
public class TestSuite {
}
