package OnlineBookingSystem.OnlineBookingSystem.exceptions;

import jakarta.validation.constraints.NotBlank;

public class StationAlreadyExistException extends RuntimeException {
    public StationAlreadyExistException(String message) {
        super(message);
    }
}
