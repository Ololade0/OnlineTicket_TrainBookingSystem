package OnlineBookingSystem.OnlineBookingSystem.service;

import OnlineBookingSystem.OnlineBookingSystem.dto.response.SignUpUserResponse;
import OnlineBookingSystem.OnlineBookingSystem.model.User;

public interface UserService {
   SignUpUserResponse signUp(User user);
   User findUserByEmail(String email);
   User findUserById(Long userId);

   User findUserByEmailOrNull(String email);
}
