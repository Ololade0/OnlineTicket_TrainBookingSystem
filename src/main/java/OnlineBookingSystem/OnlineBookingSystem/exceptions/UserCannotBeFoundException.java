package OnlineBookingSystem.OnlineBookingSystem.exceptions;

public class UserCannotBeFoundException extends RuntimeException {
    public UserCannotBeFoundException(String message) {
        super(message);
    }
}
