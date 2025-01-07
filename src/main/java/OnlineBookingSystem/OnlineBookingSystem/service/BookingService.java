package OnlineBookingSystem.OnlineBookingSystem.service;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.BookingResponse;

import java.util.List;

public interface BookingService  {
     BookingResponse  createBooking(BookTrainDTO bookTrainDTO);

}
