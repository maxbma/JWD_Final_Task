package by.tc.library.bean;

public class BookGiving {
    private final String bookName;
    private final int readerID;
    private final int librarianID;
    private final int operationID;
    private final String givingDate;
    private final String dateToReturn;

    public BookGiving(String bookName, int readerID, int librarianID, int operationID, String givingDate, String dateToReturn){
        this.bookName = bookName;
        this.givingDate = givingDate;
        this.librarianID = librarianID;
        this.operationID = operationID;
        this.readerID = readerID;
        this.dateToReturn = dateToReturn;
    }

    @Override
    public boolean equals(Object obj){
        if(null == obj || obj.getClass()!=this.getClass()){
            return false;
        }
        if(this == obj){
            return true;
        }
        BookGiving giving = (BookGiving)obj;
        return this.bookName.equals(giving.bookName) &&
                this.givingDate.equals(giving.givingDate) &&
                this.dateToReturn.equals(giving.dateToReturn) &&
                this.operationID == giving.operationID &&
                this.readerID == giving.readerID &&
                this.librarianID == giving.librarianID;
    }

    public String getGivingDate() {
        return givingDate;
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

    public String getDateToReturn() {
        return dateToReturn;
    }
}
