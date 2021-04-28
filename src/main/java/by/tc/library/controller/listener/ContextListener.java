package by.tc.library.controller.listener;

import by.tc.library.dao.pool.ConnectionPool;
import by.tc.library.dao.pool.ConnectionPoolException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {
    private static final Logger logger= Logger.getLogger(ContextListener.class.getName());
    private static final String INIT_POOL_ERROR ="Error initializing pool data";
    private static final String CLOSING_POOL_ERROR ="Error closing connection pool";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            ConnectionPool.getInstance().init();
        } catch (ConnectionPoolException e) {
            logger.log(Level.FATAL, INIT_POOL_ERROR);
            throw new ListenerException(INIT_POOL_ERROR, e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            ConnectionPool.getInstance().destroy();
        } catch (ConnectionPoolException e) {
            logger.log(Level.FATAL, CLOSING_POOL_ERROR);
            throw new ListenerException(CLOSING_POOL_ERROR,e);
        }
    }
}
