package OnlineBookingSystem.OnlineBookingSystem.service;
import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.BookingResponse;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidPassengerTypeException;
import com.paypal.base.rest.PayPalRESTException;

import java.io.IOException;


public interface BookingService  {
     BookingResponse  createBooking(BookTrainDTO bookTrainDTO) throws InvalidPassengerTypeException, IOException, PayPalRESTException, InterruptedException;

     BookingResponse confirmBooking(Long bookingId);

}
