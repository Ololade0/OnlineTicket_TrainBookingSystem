package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidPassengerTypeException;
import OnlineBookingSystem.OnlineBookingSystem.model.*;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.IdentificationType;
import OnlineBookingSystem.OnlineBookingSystem.repositories.OtherPassengerRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.OtherPassengerService;
import OnlineBookingSystem.OnlineBookingSystem.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OtherPassengerServiceImpl implements OtherPassengerService {

    private final OtherPassengerRepository otherPassengerRepository;
    private final SeatService seatService;




    public List<OtherPassenger> bookTrainForOtherPassengers(BookTrainDTO bookTrainDTO, User foundUser, Fare fare, Booking primaryBooking) throws InvalidPassengerTypeException {
        List<OtherPassenger> savedAdditionalPassengers = new ArrayList<>();
        if (bookTrainDTO.getAdditionalPassengers() != null) {
            for (OtherPassenger additionalPassenger : bookTrainDTO.getAdditionalPassengers()) {
                Seat additionalSeat = seatService.bookSeat(bookTrainDTO.getTrainClassName(), additionalPassenger.getSeatNumber());
                Double additionalPassengerFare = getFareForPassengerType(additionalPassenger.getPassengerType(), fare);

                OtherPassenger savedPassenger = OtherPassenger.builder()
                        .name(additionalPassenger.getName())
                        .email(additionalPassenger.getEmail())
                        .gender(additionalPassenger.getGender())
                        .phoneNumber(additionalPassenger.getPhoneNumber())
                        .idNumber(additionalPassenger.getIdNumber())
                        .identificationType(additionalPassenger.getIdentificationType())
                        .passengerType(additionalPassenger.getPassengerType())
                        .seatNumber(additionalSeat.getSeatNumber())
                        .booking(primaryBooking)
                        .user(foundUser)
                        .build();

                otherPassengerRepository.save(savedPassenger);
                savedAdditionalPassengers.add(savedPassenger);
            }
        }

        return savedAdditionalPassengers;
    }

    public Double calculateTotalFare(BookTrainDTO bookTrainDTO, Fare fare) throws InvalidPassengerTypeException {
        int convenienceCharge = 200;
        Double totalFare = getFareForPassengerType(bookTrainDTO.getPassengerType(), fare) + convenienceCharge;

        if (bookTrainDTO.getAdditionalPassengers() != null) {
            for (OtherPassenger passenger : bookTrainDTO.getAdditionalPassengers()) {
                totalFare += getFareForPassengerType(passenger.getPassengerType(), fare) + convenienceCharge;
            }
        }
        return totalFare;
    }

    private Double getFareForPassengerType(String passengerType, Fare fare) throws InvalidPassengerTypeException {
        if ("adult".equalsIgnoreCase(passengerType)) {
            return fare.getAdultPrices();
        } else if ("minor".equalsIgnoreCase(passengerType)) {
            return fare.getMinorPrices();
        } else {
            throw new InvalidPassengerTypeException("Invalid passenger type: " + passengerType);
        }
    }

    @Override
    public OtherPassenger findByEmailOrNull(String email) {
       return otherPassengerRepository.findByEmail(email).orElseGet(()->{

           return null;
       });
    }

    @Override
    public void deleteAll() {
        otherPassengerRepository.deleteAll();
    }
}
