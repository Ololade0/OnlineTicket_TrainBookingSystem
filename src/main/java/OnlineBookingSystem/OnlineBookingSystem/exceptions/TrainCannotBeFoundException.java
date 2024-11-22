package OnlineBookingSystem.OnlineBookingSystem.exceptions;

public class TrainCannotBeFoundException extends RuntimeException {
    public TrainCannotBeFoundException(String message) {
        super(message);
    }
}
