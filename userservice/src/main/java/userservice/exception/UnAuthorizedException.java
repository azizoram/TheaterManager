package userservice.exception;

public class UnAuthorizedException extends Throwable {
    public UnAuthorizedException(String invalidCredentials) {
        super(invalidCredentials);
    }
}
