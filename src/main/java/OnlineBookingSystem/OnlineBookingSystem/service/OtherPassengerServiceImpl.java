//package OnlineBookingSystem.OnlineBookingSystem.service;
//
//import OnlineBookingSystem.OnlineBookingSystem.model.OtherPassenger;
//import OnlineBookingSystem.OnlineBookingSystem.repositories.OtherPassengerRepository;
//import OnlineBookingSystem.OnlineBookingSystem.service.Impl.OtherPassengerService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//@Service
//@RequiredArgsConstructor
//public class OtherPassengerServiceImpl implements OtherPassengerService {
//
//
//    private final OtherPassengerRepository otherPassengerRepository;
//
//    public OtherPassenger findUserByEmailOrNull(String email) {
//        Optional<OtherPassenger> passenger = otherPassengerRepository.findByPassengerEmail(email);
//        return passenger.orElse(null);
//    }
//}
