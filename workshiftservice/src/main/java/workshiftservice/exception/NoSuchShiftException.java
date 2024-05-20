package workshiftservice.exception;

public class NoSuchShiftException extends RuntimeException {
    public NoSuchShiftException(String message) {
        super(message);
    }
}
