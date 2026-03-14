package com.hasithmalshan.confession_form.service.impl;

import com.hasithmalshan.confession_form.dto.UserDTO;
import com.hasithmalshan.confession_form.dto.UserRegistrationDTO;
import com.hasithmalshan.confession_form.exception.DuplicateResourceException;
import com.hasithmalshan.confession_form.exception.ResourceNotFoundException;
import com.hasithmalshan.confession_form.model.User;
import com.hasithmalshan.confession_form.model.enums.Role;
import com.hasithmalshan.confession_form.repo.UserRepository;
import com.hasithmalshan.confession_form.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
            throw new DuplicateResourceException("User", "username", registrationDTO.getUsername());
        }
        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new DuplicateResourceException("User", "email", registrationDTO.getEmail());
        }

        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setFname(registrationDTO.getFname());
        user.setLname(registrationDTO.getLname());
        user.setEmail(registrationDTO.getEmail());
        user.setMobileNo(registrationDTO.getMobileNo());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setRole(Role.USER);
        user.setActive(true);
        return userRepository.save(user);
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return convertToDTO(user);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return convertToDTO(user);
    }

    @Override
    public User getUserByUsernameEntity(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return convertToDTO(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long id, User user) {
        user.setId(id);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setFname(user.getFname());
        userDTO.setLname(user.getLname());
        userDTO.setEmail(user.getEmail());
        userDTO.setMobileNo(user.getMobileNo());
        userDTO.setRole(user.getRole().toString());
        return userDTO;
    }
}

