package OnlineBookingSystem.OnlineBookingSystem.exceptions;

public class SeatCannotBeFoundException extends RuntimeException{
    public SeatCannotBeFoundException(String message) {
        super(message);
    }
}
