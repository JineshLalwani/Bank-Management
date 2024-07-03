package banking.management.exception;

public class CreationException extends ServiceException {
    public CreationException(String message) {
        super(message);
    }

    public CreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
