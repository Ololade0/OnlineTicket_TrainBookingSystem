package OnlineBookingSystem.OnlineBookingSystem.controller;

import OnlineBookingSystem.OnlineBookingSystem.dto.response.SignUpUserResponse;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.UserCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.model.User;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.RoleType;
import OnlineBookingSystem.OnlineBookingSystem.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/user/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid  @RequestBody User signUpUser){
        SignUpUserResponse registeredUser = userService.signUp(signUpUser);
        log.info("Incoming user payload: {}", registeredUser);

        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }
    @GetMapping("/find-user/{email}")
    public ResponseEntity<?> findUserByEmail(@PathVariable("email") String email) {
        try {
            User foundUserByEmail = userService.findUserByEmail(email);
            return new ResponseEntity<>(foundUserByEmail, HttpStatus.OK);
        } catch (UserCannotBeFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
