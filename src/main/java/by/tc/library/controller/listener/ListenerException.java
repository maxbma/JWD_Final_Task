package by.tc.library.controller.listener;

public class ListenerException extends RuntimeException{
    private static final long serialVersionUID = -27958041039775537L;

    public ListenerException() {
        super();
    }

    public ListenerException(String message) {
        super(message);
    }

    public ListenerException(Exception e) {
        super(e);
    }

    public ListenerException(String message, Exception e) {
        super(message, e);
    }
}
