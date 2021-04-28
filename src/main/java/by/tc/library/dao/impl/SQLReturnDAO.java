package by.tc.library.dao.impl;

import by.tc.library.bean.BookReturn;
import by.tc.library.dao.DAOException;
import by.tc.library.dao.ReturnDAO;
import by.tc.library.dao.pool.ConnectionPool;
import by.tc.library.dao.pool.ConnectionPoolException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SQLReturnDAO implements ReturnDAO {

    private final static Logger logger = Logger.getLogger(SQLReturnDAO.class.getName());
    private final static ConnectionPool connectionPool = ConnectionPool.getInstance();

    private final static String SELECT_RETURNS_TO_CONFIRM;
    private final static String SELECT_BY_READER_ID;
    private final static String SELECT_BY_OPERATION_ID;
    private final static String INSERT_BOOK_RETURN;
    private final static String UPDATE_LIBRARY_CARD;
    private final static String UPDATE_BOOK_STATUS;
    private final static String SELECT_BOOK_ID_FROM_OPER;

    private static final String BOOK_ID = "b_id";
    private final static String READER_ID = "r_id";
    private static final String OPERATION_ID = "operation_id";
    private static final String BOOK_NAME = "b_name";
    private static final String LIBRARIAN_ID = "l_id";
    private static final String OPERATION_DATE = "op_date";
    private static final String USER_LOGIN = "u_login";

    private final static String FATAL_ERROR_RESULT_SET = "Fatal error closing resultSet";
    private final static String FATAL_ERROR_PREPARED_STATEMENT = "Fatal error closing preparedStatement";


    private final static SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

    static {
        SELECT_RETURNS_TO_CONFIRM = "SELECT * FROM (`library_card` " +
                "INNER JOIN (`reader` INNER JOIN `user` " +
                "ON `reader`.`r_u_id`=`user`.`u_id`) " +
                "ON `library_card`.`r_id` = `reader`.`r_id`) " +
                "INNER JOIN `book_info` " +
                "ON `library_card`.`b_id` = `book_info`.`b_id` " +
                "WHERE `operation_type` = 'возвр' " +
                "AND `op_status` IS NULL";
        SELECT_BY_READER_ID = "SELECT * FROM `library_card` " +
                "INNER JOIN `book_info` " +
                "ON `library_card`.`b_id` = `book_info`.`b_id` " +
                "WHERE `r_id` = ? " +
                "AND `operation_type` = 'возвр' " +
                "AND `op_status` = 'завершена'";
        SELECT_BOOK_ID_FROM_OPER = "SELECT `b_id` FROM `library_card` " +
                "WHERE `operation_id` = ? ";
        SELECT_BY_OPERATION_ID = "SELECT * FROM `library_card` " +
                "WHERE operation_id = ? ";
        INSERT_BOOK_RETURN = "INSERT INTO library_card(operation_type,b_id,r_id,op_date)" +
                "VALUES( 'возвр' , ? , ? , ?)";
        UPDATE_LIBRARY_CARD = "UPDATE library_card " +
                "SET op_status = 'завершена' " +
                "WHERE operation_id = ? ";
        UPDATE_BOOK_STATUS = "UPDATE `book_info`" +
                "SET `b_status` = 'своб'" +
                "WHERE `b_id` = ? ";
    }

    @Override
    public List<BookReturn> all(int readerID) throws DAOException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        List<BookReturn> history = new ArrayList<>();
        try{
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SELECT_BY_READER_ID);
            st.setInt(1,readerID);
            rs = st.executeQuery();

            while(rs.next()){
                int operationID = rs.getInt(OPERATION_ID);
                int librarianID = rs.getInt(LIBRARIAN_ID);
                String bookName = rs.getString(BOOK_NAME);
                String date = rs.getString(OPERATION_DATE);
                String userLogin = null;

                BookReturn returnedBook = new BookReturn(bookName, readerID, librarianID,
                        operationID, userLogin, date);

                history.add(returnedBook);
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
        return history;
    }

    @Override
    public List<BookReturn> allToLib() throws DAOException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        List<BookReturn> booksToConfirm = new ArrayList<>();
        try{
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SELECT_RETURNS_TO_CONFIRM);
            rs = st.executeQuery();
            while(rs.next()){
                int operationID = rs.getInt(OPERATION_ID);
                int librarianID = rs.getInt(LIBRARIAN_ID);
                int readerID = rs.getInt(READER_ID);
                String bookName = rs.getString(BOOK_NAME);
                String date = rs.getString(OPERATION_DATE);
                String readerLogin = rs.getString(USER_LOGIN);

                BookReturn bookToConfirm = new BookReturn(bookName, readerID, librarianID,
                        operationID, readerLogin, date);

                booksToConfirm.add(bookToConfirm);
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

        return booksToConfirm;
    }

    @Override
    public boolean returnBook(int givingID) throws DAOException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        boolean isBookReturned = false;
        try{
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SELECT_BY_OPERATION_ID);
            st.setInt(1, givingID);
            rs = st.executeQuery();

            if(rs.next()){
                int readerID = rs.getInt(READER_ID);
                int bookID = rs.getInt(BOOK_ID);
                Timestamp date = new Timestamp(System.currentTimeMillis());

                st = con.prepareStatement(INSERT_BOOK_RETURN);
                st.setInt(1, bookID);
                st.setInt(2, readerID);
                st.setString(3, sdf.format(date));
                st.executeUpdate();

                st = con.prepareStatement(UPDATE_LIBRARY_CARD);
                st.setInt(1, givingID);
                st.executeUpdate();

                isBookReturned = true;
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
        return isBookReturned;
    }

    @Override
    public boolean confirmReturn(int returnID) throws DAOException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        boolean isReturnConfirmed = false;
        try{
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SELECT_BOOK_ID_FROM_OPER);
            st.setInt(1, returnID);
            rs = st.executeQuery();
            int bookID = 0;
            if(rs.next()){
                bookID = rs.getInt(BOOK_ID);
            }

            st = con.prepareStatement(UPDATE_LIBRARY_CARD);
            st.setInt(1, returnID);
            st.executeUpdate();

            st = con.prepareStatement(UPDATE_BOOK_STATUS);
            st.setInt(1,bookID);
            st.executeUpdate();

            isReturnConfirmed = true;
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

        return isReturnConfirmed;
    }
}
