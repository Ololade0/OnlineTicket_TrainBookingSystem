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
