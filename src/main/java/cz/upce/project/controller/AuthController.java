package cz.upce.project.controller;

import cz.upce.project.dto.LoginRequest;
import cz.upce.project.dto.UserDto;
import cz.upce.project.service.AuthServiceImpl;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDto userDto) {
        return authService.registerUser(userDto);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> sendResetToken(@RequestBody @Valid UserDto userDto) {
        return authService.sendResetToken(userDto);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String newPassword, @RequestParam String resetToken) {
        return authService.resetPassword(newPassword, resetToken);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid LoginRequest loginRequest){
        return authService.changePassword(loginRequest);
    }
}
