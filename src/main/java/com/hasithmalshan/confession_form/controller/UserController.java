package com.hasithmalshan.confession_form.controller;

import com.hasithmalshan.confession_form.dto.UserDTO;
import com.hasithmalshan.confession_form.dto.UserRegistrationDTO;
import com.hasithmalshan.confession_form.dto.response.ApiResponse;
import com.hasithmalshan.confession_form.model.User;
import com.hasithmalshan.confession_form.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        User registeredUser = userService.registerUser(registrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(userService.convertToDTO(registeredUser), "User registered successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(userDTO, "User retrieved successfully"));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserByUsername(@PathVariable String username) {
        UserDTO userDTO = userService.getUserByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(userDTO, "User retrieved successfully"));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserByEmail(@PathVariable String email) {
        UserDTO userDTO = userService.getUserByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(userDTO, "User retrieved successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users, "Users retrieved successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "User updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.noContent("User deleted successfully"));
    }

    @GetMapping("/check/{id}")
    public ResponseEntity<ApiResponse<Boolean>> userExists(@PathVariable Long id) {
        boolean exists = userService.userExists(id);
        return ResponseEntity.ok(ApiResponse.success(exists, "User existence checked"));
    }
}
