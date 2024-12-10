package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.dto.response.SignUpUserResponse;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.IdNumberAlreadyExist;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidIdNumber;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.PasswordDoesNotMatchException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.UserAlreadyExistException;
import OnlineBookingSystem.OnlineBookingSystem.model.User;
import OnlineBookingSystem.OnlineBookingSystem.repositories.UserRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public SignUpUserResponse signUp(User user) {
//        validateUserInfo(user);
        User signupUser = User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .gender(user.getGender())
                .DateOfBirth(user.getDateOfBirth())
                .identificationType(user.getIdentificationType())
                .phoneNumber(user.getPhoneNumber())
                .password(user.getPassword())
                .idNumber(user.getIdNumber())
                .confirmPassword(user.getConfirmPassword())
                .idNumber(user.getIdNumber())
                .roleSet(new HashSet<>())
                .build();
//        signupUser.getRoleSet().add(new Role(RoleType.USER_ROLE));
        log.info("User Details: {}", signupUser);
        userRepository.save(signupUser);
        return getSignUpUserResponse(signupUser);
    }




    private static SignUpUserResponse getSignUpUserResponse(User signupUser) {
        return SignUpUserResponse.builder()
                .id(signupUser.getId())
                .firstName(signupUser.getFirstName())
                .lastName(signupUser.getLastName())
                .email(signupUser.getEmail())
                .gender(signupUser.getGender())
                .dateOfBirth(signupUser.getDateOfBirth())
                .password(signupUser.getPassword())
                .confirmPassword(signupUser.getConfirmPassword())
                .idNumber(signupUser.getIdNumber())
                .phoneNumber(signupUser.getPhoneNumber())
                .message("User signed up successfully") // Success message
                .build();
    }

    private void validateUserInfo(User user){
        if(!Objects.equals(user.getPassword(), user.getConfirmPassword())){
            throw new PasswordDoesNotMatchException("The passwords you entered do not match. Please ensure both fields are identical.");
        }
        if(userRepository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistException("User with the email already Exist");
        }
        if(user.getIdNumber() == null || user.getIdNumber().length() < 10 || user.getIdNumber().length() > 15){
            throw new InvalidIdNumber("IDss number must be between 10 and 15 characters.");

        }
        if(userRepository.existsByIdNumber(user.getIdNumber())){
            throw new IdNumberAlreadyExist("user identification number already exist");

        }
    }




}





