package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidPassengerTypeException;
import OnlineBookingSystem.OnlineBookingSystem.model.*;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.IdentificationType;
import OnlineBookingSystem.OnlineBookingSystem.repositories.OtherPassengerRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.OtherPassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OtherPassengerServiceImpl implements OtherPassengerService {

    @Autowired
    private  OtherPassengerRepository otherPassengerRepository;




    @Override
    public List<OtherPassenger> addNewPassengers(List<OtherPassenger> otherPassengers) {
        List<OtherPassenger> savedPassengers = new ArrayList<>();

        for (OtherPassenger otherPassenger : otherPassengers) {
            OtherPassenger otherPassenger1 = OtherPassenger.builder()
                    .name(otherPassenger.getName())
                    .gender(otherPassenger.getGender())
                    .identificationType(IdentificationType.valueOf(otherPassenger.getIdentificationType().toString()))
                    .phoneNumber(otherPassenger.getPhoneNumber())
                    .idNumber(otherPassenger.getIdNumber())
                    .email(otherPassenger.getEmail())
                    .build();
            OtherPassenger newPassenger = otherPassengerRepository.save(otherPassenger1);

            savedPassengers.add(newPassenger);
        }

        return savedPassengers;
    }


    public List<OtherPassenger> bookTrainForOtherPassengers(BookTrainDTO bookTrainDTO) {
        List<OtherPassenger> savedAdditionalPassengers = new ArrayList<>();
        if (bookTrainDTO.getAdditionalPassengers() != null) {
            for (OtherPassenger additionalPassenger : bookTrainDTO.getAdditionalPassengers()) {
//                Seat additionalSeat = seatService.bookSeat(bookTrainDTO.getTrainClassName(), additionalPassenger.getSeatNumber());
//                Double additionalPassengerFare = getFareForPassengerType(additionalPassenger.getPassengerType(), fare);

                // Save additional passenger
                OtherPassenger savedPassenger = OtherPassenger.builder()
                        .name(additionalPassenger.getName())
                        .email(additionalPassenger.getEmail())
                        .gender(additionalPassenger.getGender())
                        .phoneNumber(additionalPassenger.getPhoneNumber())
                        .idNumber(additionalPassenger.getIdNumber())
                        .identificationType(additionalPassenger.getIdentificationType())
                        .passengerType(additionalPassenger.getPassengerType())
//                        .seatNumber(additionalSeat.getSeatNumber())
//                        .booking(primaryBooking) // Ensure this is set
//                        .user(foundUser)
                        .build();
                otherPassengerRepository.save(savedPassenger);
                savedAdditionalPassengers.add(savedPassenger);
            }
        }

        return savedAdditionalPassengers;
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
