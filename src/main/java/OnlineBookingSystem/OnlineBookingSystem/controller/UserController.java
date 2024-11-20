package OnlineBookingSystem.OnlineBookingSystem.controller;

import OnlineBookingSystem.OnlineBookingSystem.dto.response.SignUpUserResponse;
import OnlineBookingSystem.OnlineBookingSystem.model.User;
import OnlineBookingSystem.OnlineBookingSystem.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/user/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User signUpUser){
        SignUpUserResponse registeredUser = userService.signUp(signUpUser);
        log.info("Incoming user payload: {}", registeredUser);

        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

}
