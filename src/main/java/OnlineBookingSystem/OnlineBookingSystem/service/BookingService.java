package OnlineBookingSystem.OnlineBookingSystem.service;
import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.BookingResponse;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidPassengerTypeException;

import java.io.IOException;


public interface BookingService  {
     BookingResponse  createBooking(BookTrainDTO bookTrainDTO) throws InvalidPassengerTypeException, IOException;

}
