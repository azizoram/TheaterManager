package userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import userservice.exception.UnAuthorizedException;
import userservice.service.AuthorizationService;
import userservice.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthorizationController {

    private final UserService userService;
    private final AuthorizationService authorizationService;
    @Autowired
    public AuthorizationController(AuthorizationService authorizationService, UserService userService) {
        this.authorizationService = authorizationService;
        this.userService = userService;
    }

    @PostMapping ("/login")
    public ResponseEntity<Void> login(@RequestBody Map<String, String> credentials) {
        try {
            String token = authorizationService.login(credentials.get("email"), credentials.get("password"));
            return ResponseEntity.ok().header("Authorization", token).build();
        } catch (Exception | UnAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
