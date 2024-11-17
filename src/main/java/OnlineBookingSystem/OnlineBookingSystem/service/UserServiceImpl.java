package OnlineBookingSystem.OnlineBookingSystem.service;

import OnlineBookingSystem.OnlineBookingSystem.dto.response.SignUpUserResponse;
import OnlineBookingSystem.OnlineBookingSystem.model.Role;
import OnlineBookingSystem.OnlineBookingSystem.model.User;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.RoleType;
import OnlineBookingSystem.OnlineBookingSystem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public SignUpUserResponse signUp(User user) {
        User signupUser = User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .gender(user.getGender())
                .password(user.getPassword())
                .roleSet(new HashSet<>())
                .build();
//        signupUser.getRoleSet().add(new Role(RoleType.USER_ROLE));
        log.info("User Details: {}", signupUser);
        userRepository.save(signupUser);
        return getSignUpUserResponse(signupUser);
    }

    private static SignUpUserResponse getSignUpUserResponse(User signupUser) {
        SignUpUserResponse response = new SignUpUserResponse(
                signupUser.getId(),
                signupUser.getName(),
                signupUser.getEmail(),
               signupUser.getGender(),
                "User signed up successfully"
        );
       return response;
    }


}





