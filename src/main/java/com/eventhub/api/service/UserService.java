package com.eventhub.api.service;

import com.eventhub.api.dto.UserDTO;
import com.eventhub.api.exception.DuplicateResourceException;
import com.eventhub.api.exception.ResourceNotFoundException;
import com.eventhub.api.model.User;
import com.eventhub.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDTO).toList();
    }

    public UserDTO getUserById(Long id) {
        return toDTO(findOrThrow(id));
    }

    public UserDTO createUser(UserDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("User with email " + dto.getEmail() + " already exists");
        }
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        return toDTO(userRepository.save(user));
    }

    public UserDTO updateUser(Long id, UserDTO dto) {
        User user = findOrThrow(id);
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        return toDTO(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        findOrThrow(id);
        userRepository.deleteById(id);
    }

    private User findOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        return dto;
    }

    public User findEntityById(Long id) {
        return findOrThrow(id);
    }
}
