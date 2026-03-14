package com.hasithmalshan.confession_form.service;

import com.hasithmalshan.confession_form.dto.UserDTO;
import com.hasithmalshan.confession_form.dto.UserRegistrationDTO;
import com.hasithmalshan.confession_form.model.User;

import java.util.List;

public interface UserService {
    User registerUser(UserRegistrationDTO registrationDTO);
    UserDTO getUserById(Long id);
    UserDTO getUserByUsername(String username);
    User getUserByUsernameEntity(String username);
    UserDTO getUserByEmail(String email);
    List<User> getAllUsers();
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    boolean userExists(Long id);
    UserDTO convertToDTO(User user);
}

