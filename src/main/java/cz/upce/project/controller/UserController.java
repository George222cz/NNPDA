package cz.upce.project.controller;

import cz.upce.project.dto.UserDto;
import cz.upce.project.entity.User;
import cz.upce.project.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@SecurityRequirement(name = "BasicAuth")
@RequestMapping(value = "/api/user")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> createOrUpdateUser(@RequestBody UserDto dto){
        return userService.createOrUpdateUser(dto);
    }

    @DeleteMapping("{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> deleteUser(@PathVariable Long userId){
        return userService.deleteUser(userId);
    }
}
