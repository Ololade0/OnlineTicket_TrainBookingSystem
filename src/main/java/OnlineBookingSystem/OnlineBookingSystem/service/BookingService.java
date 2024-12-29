package OnlineBookingSystem.OnlineBookingSystem.service;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.request.FindScheduleDTO;
import OnlineBookingSystem.OnlineBookingSystem.model.Booking;

public interface BookingService  {
     Booking createBooking(BookTrainDTO bookTrainDTO, FindScheduleDTO findScheduleDTO);
}
