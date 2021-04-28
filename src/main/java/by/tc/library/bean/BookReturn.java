package by.tc.library.bean;

public class BookReturn {
    private final String bookName;
    private final int readerID;
    private final int librarianID;
    private final int operationID;
    private final String returnDate;
    private String readerLogin;

    public BookReturn(String bookName, int readerID, int librarianID, int operationID,String readerLogin, String returnDate){
        this.bookName = bookName;
        this.returnDate = returnDate;
        this.librarianID = librarianID;
        this.operationID = operationID;
        this.readerLogin = readerLogin;
        this.readerID = readerID;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getReaderLogin() {
        return readerLogin;
    }

    public String getBookName() {
        return bookName;
    }

    public int getLibrarianID() {
        return librarianID;
    }

    public int getOperationID() {
        return operationID;
    }

    public int getReaderID() {
        return readerID;
    }
}
