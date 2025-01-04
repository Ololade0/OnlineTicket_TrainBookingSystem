package OnlineBookingSystem.OnlineBookingSystem.service;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.BookingResponse;

public interface BookingService  {
     BookingResponse createBooking(BookTrainDTO bookTrainDTO);

}
