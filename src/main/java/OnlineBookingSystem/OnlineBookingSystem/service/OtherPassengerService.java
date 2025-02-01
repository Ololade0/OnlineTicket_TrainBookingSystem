package OnlineBookingSystem.OnlineBookingSystem.service;

import OnlineBookingSystem.OnlineBookingSystem.model.OtherPassenger;

import java.util.List;

public interface OtherPassengerService {
    List<OtherPassenger> addNewPassengers(List<OtherPassenger> otherPassengers);
    OtherPassenger findByEmailOrNull(String email);
    void deleteAll();
}
