package OnlineBookingSystem.OnlineBookingSystem.service;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.model.Booking;
import OnlineBookingSystem.OnlineBookingSystem.model.OtherPassenger;
import OnlineBookingSystem.OnlineBookingSystem.model.User;

import java.util.List;

public interface OtherPassengerService {
    List<OtherPassenger> addNewPassengers(List<OtherPassenger> otherPassengers);

    OtherPassenger findByEmailOrNull(String email);
    void deleteAll();
}
