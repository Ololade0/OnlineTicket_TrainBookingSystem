package OnlineBookingSystem.OnlineBookingSystem.exceptions;


public class InvalidSeatNumberException extends RuntimeException {
    public InvalidSeatNumberException(String message) {
        super(message);
    }
}