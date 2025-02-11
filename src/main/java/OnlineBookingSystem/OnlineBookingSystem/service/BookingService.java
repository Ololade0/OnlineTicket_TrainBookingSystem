package OnlineBookingSystem.OnlineBookingSystem.service;
import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.BookingResponse;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidPassengerTypeException;
import OnlineBookingSystem.OnlineBookingSystem.model.Booking;
import com.paypal.base.rest.PayPalRESTException;

import java.io.IOException;
import java.util.Optional;


public interface BookingService  {
     BookingResponse  createBooking(BookTrainDTO bookTrainDTO) throws InvalidPassengerTypeException, IOException, PayPalRESTException, InterruptedException;

     BookingResponse confirmBooking(Long bookingId);

     Optional<Booking> getBookingById(Long bookingId);
}
