package by.tc.library.bean;

public class BookOrder {

    private final String bookName;
    private final String authorName;
    private final String authorSurname;
    private final int readerID;
    private final String readerLogin;
    private final int operationID;
    private String orderStatus;

    public BookOrder(String bookName, int readerID, int operationID, String authorName, String authorSurname, String readerLogin){
        this.bookName = bookName;
        this.operationID = operationID;
        this.readerID = readerID;
        this.authorSurname = authorSurname;
        this.authorName = authorName;
        this.readerLogin = readerLogin;
    }

    @Override
    public boolean equals(Object obj){
        if(null == obj || obj.getClass()!=this.getClass()){
            return false;
        }
        if(this == obj){
            return true;
        }
        BookOrder order = (BookOrder)obj;
        return this.authorName.equals(order.authorName) &&
                this.authorSurname.equals(order.authorSurname) &&
                this.bookName.equals(order.bookName) &&
                this.orderStatus.equals(order.orderStatus) &&
                this.operationID == order.operationID &&
                this.readerID ==  order.readerID;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getReaderLogin() {
        return readerLogin;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorSurname() {
        return authorSurname;
    }

    public String getBookName() {
        return bookName;
    }

    public int getOperationID() {
        return operationID;
    }

    public int getReaderID() {
        return readerID;
    }
}
