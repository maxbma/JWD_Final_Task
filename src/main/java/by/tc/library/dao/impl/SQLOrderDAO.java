package by.tc.library.dao.impl;

import by.tc.library.bean.BookOrder;
import by.tc.library.dao.DAOException;
import by.tc.library.dao.OrderDAO;
import by.tc.library.dao.pool.ConnectionPool;
import by.tc.library.dao.pool.ConnectionPoolException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SQLOrderDAO implements OrderDAO {

    private final static Logger logger = Logger.getLogger(SQLOrderDAO.class.getName());
    private final static ConnectionPool connectionPool = ConnectionPool.getInstance();

    private final static String SELECT_ALL_ORDERS;
    private final static String SELECT_BOOK_BY_ID;
    private final static String SELECT_BOOK_BY_INFO;
    private final static String SELECT_READER_BY_USER;
    private final static String INSERT_INTO_CARD;
    private final static String SELECT_ALL_ORDERS_TO_LIB;
    private final static String UPDATE_BOOK_STATUS_ORDERED;
    private final static String UPDATE_CARD_BY_CONFIRM;
    private final static String UPDATE_CARD_BY_DECLINE;
    private static final String SELECT_BOOK_BY_OPERATION;
    private static final String UPDATE_BOOK_BY_ID;
    private static final String UPDATE_BOOK_STATUS_FREE;

    private static final String BOOK_NAME = "b_name";
    private static final String AUTHOR_NAME = "b_author_name";
    private static final String AUTHOR_SURNAME = "b_author_surname";
    private static final String OPERATION_ID = "operation_id";
    private static final String OPERATION_STATUS = "op_status";
    private static final String USER_LOGIN = "u_login";
    private final static String RELEASE_YEAR = "b_release_year";
    private final static String READER_ID = "r_id";
    private static final String BOOK_STATUS = "b_status";
    private static final String FREE_BOOK = "своб";
    private static final String BOOK_ID = "b_id";
    private static final String AMOUNT_OF_ORDERS = "amount_of_orders";

    private final static String FATAL_ERROR_RESULT_SET = "Fatal error closing resultSet";
    private final static String FATAL_ERROR_PREPARED_STATEMENT = "Fatal error closing preparedStatement";

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");


    static {
        SELECT_ALL_ORDERS = "SELECT * FROM (`library_card` " +
                "INNER JOIN (`reader` INNER JOIN `user` " +
                "ON `reader`.`r_u_id`=`user`.`u_id`) " +
                "ON `library_card`.`r_id` = `reader`.`r_id`) " +
                "INNER JOIN `book_info` " +
                "ON `library_card`.`b_id` = `book_info`.`b_id` " +
                "WHERE `reader`.`r_id`= ? " +
                " AND `operation_type`='зак'" +
                " AND `op_status`!='завершена'";
        SELECT_BOOK_BY_ID = "SELECT *  FROM `book_info` WHERE `b_id`= ? ";
        SELECT_BOOK_BY_INFO = "SELECT *  FROM `book_info`" +
                "WHERE `b_name`= ? AND" +
                "`b_author_name`= ? AND" +
                "`b_author_surname`= ? AND" +
                "`b_release_year`= ? ";
        SELECT_READER_BY_USER = "SELECT `r_id` FROM `reader` WHERE `r_u_id`= ? ";
        SELECT_BOOK_BY_OPERATION = "SELECT `b_id` FROM `library_card` WHERE operation_id = ?";
        INSERT_INTO_CARD = "INSERT INTO `library_card`(operation_type,b_id,r_id,op_date,op_status)" +
                "VALUES('зак', ? , ? , ? , 'ожидание')";
        UPDATE_BOOK_BY_ID = "UPDATE `book_info`" +
                "SET amount_of_orders = ? " +
                "WHERE b_id = ?";
        UPDATE_BOOK_STATUS_FREE = "UPDATE `book_info` " +
                "SET `b_status`=" + "'своб'"+
                "WHERE `b_id`= ? ";
        UPDATE_BOOK_STATUS_ORDERED = "UPDATE `book_info` " +
                "SET `b_status`=" + "'зак'"+
                "WHERE `b_id`= ? ";
        UPDATE_CARD_BY_CONFIRM = "UPDATE `library_card`" +
                "SET op_date= ? , l_id= ? , " +
                "op_status='заказана'" +
                "WHERE operation_id= ? ";
        UPDATE_CARD_BY_DECLINE = "UPDATE `library_card`" +
                "SET l_id= ? , " +
                "op_status='отклонена'" +
                "WHERE operation_id= ? ";
        SELECT_ALL_ORDERS_TO_LIB = "SELECT * FROM (`library_card` " +
                "INNER JOIN (`reader` INNER JOIN `user` " +
                "ON `reader`.`r_u_id`=`user`.`u_id`) " +
                "ON `library_card`.`r_id` = `reader`.`r_id`) " +
                "INNER JOIN `book_info` " +
                "ON `library_card`.`b_id` = `book_info`.`b_id`" +
                "WHERE `operation_type`='зак' AND " +
                "`op_status`='ожидание'";
    }

    @Override
    public List<BookOrder> all(int readerID) throws DAOException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        List<BookOrder> orders = new ArrayList<>();
        try {
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SELECT_ALL_ORDERS);
            st.setInt(1, readerID);

            rs = st.executeQuery();

            while(rs.next()){
                int operationID = rs.getInt(OPERATION_ID);
                String bookName = rs.getString(BOOK_NAME);
                String authorName = rs.getString(AUTHOR_NAME);
                String authorSurname = rs.getString(AUTHOR_SURNAME);
                String readerLogin = rs.getString(USER_LOGIN);
                String operationStatus = rs.getString(OPERATION_STATUS);

                BookOrder order = new BookOrder(bookName, readerID, operationID,
                        authorName, authorSurname, readerLogin);

                order.setOrderStatus(operationStatus);

                orders.add(order);
            }
        } catch (SQLException | ConnectionPoolException e) {
            logger.error(e.getMessage());
            throw new DAOException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                }catch (SQLException e){
                    logger.log(Level.FATAL, FATAL_ERROR_RESULT_SET, e);
                }
            }
            if (st != null) {
                try {
                    st.close();
                }catch (SQLException e){
                    logger.log(Level.FATAL, FATAL_ERROR_PREPARED_STATEMENT, e);
                }
            }
            if(con != null){
                connectionPool.releaseConnection(con);
            }
        }

        return orders;
    }

    @Override
    public boolean addOrder(int userID, int bookID) throws DAOException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        boolean isOrderRegistrated = false;

        try{
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SELECT_BOOK_BY_ID);
            st.setInt(1, bookID);

            String bookName = null;
            String authorName = null;
            String authorSurname = null;
            int releaseYear = 0;

            rs = st.executeQuery();
            if(rs.next()){
                bookName = rs.getString(BOOK_NAME);
                authorName = rs.getString(AUTHOR_NAME);
                authorSurname = rs.getString(AUTHOR_SURNAME);
                releaseYear = rs.getInt(RELEASE_YEAR);
            }

            st = con.prepareStatement(SELECT_BOOK_BY_INFO);
            st.setString(1, bookName);
            st.setString(2, authorName);
            st.setString(3, authorSurname);
            st.setInt(4, releaseYear);
            rs = st.executeQuery();

            while(rs.next()){
                if(!FREE_BOOK.equals(rs.getString(BOOK_STATUS))){
                    if(rs.isLast()){
                        return false;
                    }
                    continue;
                }
                bookID = rs.getInt(BOOK_ID);
                break;
            }

            st = con.prepareStatement(SELECT_READER_BY_USER);
            st.setInt(1, userID);
            rs = st.executeQuery();
            int readerID = 0;
            if(rs.next()){
                readerID = rs.getInt(READER_ID);
            }

            Timestamp date = new Timestamp(System.currentTimeMillis());

            st = con.prepareStatement(INSERT_INTO_CARD);
            st.setInt(1, bookID);
            st.setInt(2,readerID);
            st.setString(3, sdf.format(date));
            st.executeUpdate();

            isOrderRegistrated = true;

            st = con.prepareStatement(UPDATE_BOOK_STATUS_ORDERED);
            st.setInt(1, bookID);
            st.executeUpdate();

        } catch (SQLException | ConnectionPoolException e) {
            logger.error(e.getMessage());
            throw new DAOException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                }catch (SQLException e){
                    logger.log(Level.FATAL, FATAL_ERROR_RESULT_SET, e);
                }
            }
            if (st != null) {
                try {
                    st.close();
                }catch (SQLException e){
                    logger.log(Level.FATAL, FATAL_ERROR_PREPARED_STATEMENT, e);
                }
            }
            if(con != null){
                connectionPool.releaseConnection(con);
            }
        }

        return isOrderRegistrated;
    }

    @Override
    public boolean confirmOrder(int operationID, int librarianID) throws DAOException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        boolean isOrderConfirmed = false;
        try{
            con = connectionPool.takeConnection();

            Timestamp date = new Timestamp(System.currentTimeMillis());

            st = con.prepareStatement(UPDATE_CARD_BY_CONFIRM);
            st.setString(1, sdf.format(date));
            st.setInt(2, librarianID);
            st.setInt(3, operationID);
            st.executeUpdate();

            st = con.prepareStatement(SELECT_BOOK_BY_OPERATION);
            st.setInt(1, operationID);
            rs = st.executeQuery();
            int bookID = 0;
            if(rs.next()){
                bookID = rs.getInt(BOOK_ID);
            } else {
                throw new SQLException();
            }

            st = con.prepareStatement(SELECT_BOOK_BY_ID);
            st.setInt(1, bookID);
            rs = st.executeQuery();

            int ordersAmount = 0;
            if(rs.next()){
                ordersAmount = rs.getInt(AMOUNT_OF_ORDERS);
            } else{
                throw new SQLException();
            }

            ordersAmount += 1;
            st = con.prepareStatement(UPDATE_BOOK_BY_ID);
            st.setInt(1, ordersAmount);
            st.setInt(2, bookID);
            st.executeUpdate();

            isOrderConfirmed = true;
        } catch (SQLException | ConnectionPoolException e) {
            logger.error(e.getMessage());
            throw new DAOException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                }catch (SQLException e){
                    logger.log(Level.FATAL, FATAL_ERROR_RESULT_SET, e);
                }
            }
            if (st != null) {
                try {
                    st.close();
                }catch (SQLException e){
                    logger.log(Level.FATAL, FATAL_ERROR_PREPARED_STATEMENT, e);
                }
            }
            if(con != null){
                connectionPool.releaseConnection(con);
            }
        }

        return isOrderConfirmed;
    }

    @Override
    public boolean declineOrder(int operationID, int librarianID) throws DAOException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        boolean isOrderDeclined = false;
        try{
            con = connectionPool.takeConnection();

            st = con.prepareStatement(UPDATE_CARD_BY_DECLINE);
            st.setInt(1, librarianID);
            st.setInt(2, operationID);
            st.executeUpdate();

            st = con.prepareStatement(SELECT_BOOK_BY_OPERATION);
            st.setInt(1, operationID);
            rs = st.executeQuery();
            int bookID = 0;
            if(rs.next()){
                bookID = rs.getInt(BOOK_ID);
            } else {
                throw new SQLException();
            }

            st = con.prepareStatement(UPDATE_BOOK_STATUS_FREE);
            st.setInt(1, bookID);
            st.executeUpdate();

            isOrderDeclined = true;
        } catch (SQLException | ConnectionPoolException e) {
            logger.error(e.getMessage());
            throw new DAOException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                }catch (SQLException e){
                    logger.log(Level.FATAL, FATAL_ERROR_RESULT_SET, e);
                }
            }
            if (st != null) {
                try {
                    st.close();
                }catch (SQLException e){
                    logger.log(Level.FATAL, FATAL_ERROR_PREPARED_STATEMENT, e);
                }
            }
            if(con != null){
                connectionPool.releaseConnection(con);
            }
        }
        return isOrderDeclined;
    }

    @Override
    public List<BookOrder> allToLib() throws DAOException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        List<BookOrder> orders = new ArrayList<>();
        try{
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SELECT_ALL_ORDERS_TO_LIB);
            rs = st.executeQuery();

            while(rs.next()){
                int readerID = rs.getInt(READER_ID);
                String bookName = rs.getString(BOOK_NAME);
                int operationID = rs.getInt(OPERATION_ID);
                String authorName = rs.getString(AUTHOR_NAME);
                String authorSurname = rs.getString(AUTHOR_SURNAME);
                String readerLogin = rs.getString(USER_LOGIN);
                String orderStatus = rs.getString(OPERATION_STATUS);

                BookOrder order = new BookOrder(bookName, readerID, operationID,
                        authorName, authorSurname, readerLogin);

                order.setOrderStatus(orderStatus);

                orders.add(order);
            }
        } catch (SQLException | ConnectionPoolException e) {
            logger.error(e.getMessage());
            throw new DAOException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                }catch (SQLException e){
                    logger.log(Level.FATAL, FATAL_ERROR_RESULT_SET, e);
                }
            }
            if (st != null) {
                try {
                    st.close();
                }catch (SQLException e){
                    logger.log(Level.FATAL, FATAL_ERROR_PREPARED_STATEMENT, e);
                }
            }
            if(con != null){
                connectionPool.releaseConnection(con);
            }
        }

        return orders;
    }
}
