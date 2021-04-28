package by.tc.library.dao.impl;

import by.tc.library.bean.BookGiving;
import by.tc.library.dao.DAOException;
import by.tc.library.dao.GivingDAO;
import by.tc.library.dao.pool.ConnectionPool;
import by.tc.library.dao.pool.ConnectionPoolException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SQLGivingDAO implements GivingDAO {

    private final static Logger logger = Logger.getLogger(SQLGivingDAO.class.getName());
    private final static ConnectionPool connectionPool = ConnectionPool.getInstance();

    private final static String SELECT_BY_OPERATION_ID;
    private final static String SELECT_BY_READER_ID;
    private final static String INSERT_INTO_LIBRARY_CARD;
    private final static String UPDATE_LIBRARY_CARD;
    private final static String UPDATE_BOOK_STATUS;

    private static final String BOOK_ID = "b_id";
    private final static String READER_ID = "r_id";
    private static final String OPERATION_ID = "operation_id";
    private static final String BOOK_NAME = "b_name";
    private static final String LIBRARIAN_ID = "l_id";
    private static final String OPERATION_DATE = "op_date";
    private static final String RETURN_DATE = "return_date";

    private final static String FATAL_ERROR_RESULT_SET = "Fatal error closing resultSet";
    private final static String FATAL_ERROR_PREPARED_STATEMENT = "Fatal error closing preparedStatement";


    private final static SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

    static {
        SELECT_BY_OPERATION_ID = "SELECT * FROM `library_card`" +
                "WHERE operation_id= ? ";
        SELECT_BY_READER_ID = "SELECT * FROM `library_card` INNER JOIN `book_info` " +
                "ON `library_card`.`b_id` = `book_info`.`b_id` " +
                "WHERE `r_id`= ? " +
                "AND `operation_type`='выд' " +
                "AND `op_status` IS NULL";
        INSERT_INTO_LIBRARY_CARD = "INSERT INTO `library_card`(operation_type, b_id,r_id,l_id,op_date,return_date)" +
                "VALUES('выд', ? , ? , ? , ? , ?)";
        UPDATE_LIBRARY_CARD = "UPDATE `library_card`" +
                "SET op_status='завершена'" +
                "WHERE operation_id= ? ";
        UPDATE_BOOK_STATUS = "UPDATE `book_info`" +
                "SET b_status = 'выд'" +
                "WHERE b_id = ? ";
    }

    @Override
    public boolean addGiving(int orderID) throws DAOException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        boolean isBookGiven = false;
        try{
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SELECT_BY_OPERATION_ID);
            st.setInt(1, orderID);
            rs = st.executeQuery();
            if(rs.next()){
                int bookID = rs.getInt(BOOK_ID);
                int readerID = rs.getInt(READER_ID);
                int librarianID = rs.getInt(LIBRARIAN_ID);
                Timestamp operationDate = new Timestamp(System.currentTimeMillis());
                Timestamp returnDate = new Timestamp(System.currentTimeMillis() + 1000*60*60*24*7);

                st = con.prepareStatement(INSERT_INTO_LIBRARY_CARD);
                st.setInt(1, bookID);
                st.setInt(2, readerID);
                st.setInt(3, librarianID);
                st.setString(4, sdf.format(operationDate));
                st.setString(5, sdf.format(returnDate));
                st.executeUpdate();

                st = con.prepareStatement(UPDATE_LIBRARY_CARD);
                st.setInt(1, orderID);
                st.executeUpdate();

                st = con.prepareStatement(UPDATE_BOOK_STATUS);
                st.setInt(1,bookID);
                st.executeUpdate();

                isBookGiven = true;
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
        return isBookGiven;
    }

    @Override
    public List<BookGiving> all(int readerID) throws DAOException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        List<BookGiving> booksToReturn = new ArrayList<>();
        try {
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SELECT_BY_READER_ID);
            st.setInt(1, readerID);
            rs = st.executeQuery();

            while(rs.next()){
                String bookName = rs.getString(BOOK_NAME);
                String givingDate = rs.getString(OPERATION_DATE);
                String returnDate = rs.getString(RETURN_DATE);
                int operationID = rs.getInt(OPERATION_ID);
                int librarianID = rs.getInt(LIBRARIAN_ID);

                BookGiving givedBook = new BookGiving(bookName, readerID, librarianID,
                        operationID, givingDate, returnDate);

                booksToReturn.add(givedBook);
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

        return booksToReturn;
    }
}
