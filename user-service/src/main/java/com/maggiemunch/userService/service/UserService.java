package com.maggiemunch.userService.service;

import com.maggiemunch.userService.dto.UserRequest;
import com.maggiemunch.userService.dto.UserResponse;
import com.maggiemunch.userService.entity.User;
import com.maggiemunch.userService.enums.Role;
import com.maggiemunch.userService.exception.ResourceAlreadyExistsException;
import com.maggiemunch.userService.exception.ResourceNotFoundException;
import com.maggiemunch.userService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        log.debug("Fetching all users");
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        log.debug("Fetching user by id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", id);
                    return new ResourceNotFoundException("User", id);
                });
        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        log.debug("Fetching user by email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", email);
                    return new ResourceNotFoundException("User", "email", email);
                });
        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByMobile(String mobile) {
        log.debug("Fetching user by mobile: {}", mobile);
        User user = userRepository.findByMobile(mobile)
                .orElseThrow(() -> {
                    log.error("User not found with mobile: {}", mobile);
                    return new ResourceNotFoundException("User", "mobile", mobile);
                });
        return toResponse(user);
    }

    public UserResponse createUser(UserRequest request) {
        log.info("Creating new user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Email already exists: {}", request.getEmail());
            throw new ResourceAlreadyExistsException("User", "email", request.getEmail());
        }

        if (userRepository.existsByMobile(request.getMobile())) {
            log.warn("Mobile already exists: {}", request.getMobile());
            throw new ResourceAlreadyExistsException("User", "mobile", request.getMobile());
        }

        User user = toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (user.getRole() == null) {
            user.setRole(Role.CUSTOMER);
        }
        User savedUser = userRepository.save(user);
        log.info("Successfully created user with id: {}", savedUser.getId());

        return toResponse(savedUser);
    }

    public UserResponse updateUser(Long id, UserRequest request) {
        log.info("Updating user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", id);
                    return new ResourceNotFoundException("User", id);
                });

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new ResourceAlreadyExistsException("User", "email", request.getEmail());
            }
            user.setEmail(request.getEmail());
        }

        if (request.getMobile() != null && !request.getMobile().equals(user.getMobile())) {
            if (userRepository.existsByMobile(request.getMobile())) {
                throw new ResourceAlreadyExistsException("User", "mobile", request.getMobile());
            }
            user.setMobile(request.getMobile());
        }

        if (request.getName() != null) {
            user.setName(request.getName());
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        User updatedUser = userRepository.save(user);
        log.info("Successfully updated user with id: {}", id);

        return toResponse(updatedUser);
    }

    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", id);
                    return new ResourceNotFoundException("User", id);
                });

        userRepository.delete(user);
        log.info("Successfully deleted user with id: {}", id);
    }

    private User toEntity(UserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .role(request.getRole())
                .build();
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
