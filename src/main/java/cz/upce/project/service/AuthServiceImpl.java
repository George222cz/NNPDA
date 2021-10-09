package cz.upce.project.service;

import cz.upce.project.dto.LoginRequest;
import cz.upce.project.dto.LoginResponse;
import cz.upce.project.dto.RoleEnum;
import cz.upce.project.dto.UserDto;
import cz.upce.project.entity.User;
import cz.upce.project.repository.UserRepository;
import cz.upce.project.security.JwtTokenUtil;
import net.bytebuddy.utility.RandomString;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final JwtTokenUtil jwtTokenUtil;

    private final JavaMailSender emailSender;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder encoder, JwtTokenUtil jwtTokenUtil, JavaMailSender emailSender) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.emailSender = emailSender;
    }

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenUtil.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return ResponseEntity.ok(new LoginResponse(token,userDetails.getId(),userDetails.getUsername(),userDetails.getEmail(),userDetails.getPhone(),roles));
    }

    public ResponseEntity<?> registerUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setPhone(userDto.getPhone());
        user.setRole(RoleEnum.valueOf(userDto.getRole()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    public ResponseEntity<?> sendResetToken(UserDto userDto) {
        Optional<User> optionalUser = userRepository.findByUsername(userDto.getUsername());
        if (optionalUser.isPresent()) {
            String token = RandomString.make(40);
            User user = optionalUser.get();
            user.setResetPasswordToken(token);
            userRepository.save(user);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Password reset");
            mailMessage.setText("For password reset click here: http://localhost:8080/api/auth/reset-password?resetToken="+token);
            emailSender.send(mailMessage);

            return ResponseEntity.ok("Reset token was send to your email.");
        }
        return ResponseEntity.badRequest().body("Error: User not found!");
    }

    public ResponseEntity<?> resetPassword(String newPassword, String resetToken) {
        Optional<User> optionalUser = userRepository.findByResetPasswordToken(resetToken);
        if(optionalUser.isPresent() && !newPassword.isEmpty()){
            User user = optionalUser.get();
            user.setResetPasswordToken(null);
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);
            return ResponseEntity.ok("Password reset");
        }
        return ResponseEntity.badRequest().body("Error!");
    }

    public ResponseEntity<?> changePassword(LoginRequest loginRequest){
        try{
            UserDetailsImpl userDetails = (UserDetailsImpl) authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())).getPrincipal();
            User user = new User();
            user.setId(userDetails.getId());
            user.setPassword(encoder.encode(loginRequest.getNewPassword()));
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setPhone(userDetails.getPhone());
            user.setRole(RoleEnum.valueOf(userDetails.getAuthorities().stream().findFirst().get().getAuthority()));
            userRepository.save(user);
        }catch (AuthenticationException ex) {
            return ResponseEntity.badRequest().body("Error: Original password entered incorrectly!");
        }catch (NoSuchElementException ex){
            return ResponseEntity.badRequest().body("Error: Role error");
        }
        return ResponseEntity.badRequest().body("Password changed!");
    }
}
