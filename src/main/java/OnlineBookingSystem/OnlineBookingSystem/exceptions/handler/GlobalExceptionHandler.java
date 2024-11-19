package OnlineBookingSystem.OnlineBookingSystem.exceptions.handler;

import OnlineBookingSystem.OnlineBookingSystem.exceptions.StationAlreadyExistException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.StationCannotBeFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StationCannotBeFoundException.class)
    public ResponseEntity<Map<String, Object>> handleStationNotFoundException(StationCannotBeFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", "Station Not Found");

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    public ResponseEntity<Map<String, Object>> handleStationAlreadyExistException(StationAlreadyExistException exception){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("message", exception.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", "Station Already Exist");
        return new ResponseEntity<>(response, HttpStatus.FOUND);


    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Internal Server Error");

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

