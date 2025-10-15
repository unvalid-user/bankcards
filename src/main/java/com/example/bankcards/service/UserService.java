package com.example.bankcards.service;

import com.example.bankcards.dto.mapper.UserMapper;
import com.example.bankcards.dto.user.CreateUserRequest;
import com.example.bankcards.dto.user.UpdateUserRequest;
import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ResourceAlreadyExists;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.repository.specification.UserFilter;
import com.example.bankcards.repository.specification.UserSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.bankcards.util.AppConst.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new ResourceNotFoundException(USER, PHONE_NUMBER, phoneNumber));
    }

    public User createUser(CreateUserRequest createUserRequest) {
        userShouldNotExistByPhoneNumber(createUserRequest.phoneNumber());

        User user = User.builder()
                .phoneNumber(createUserRequest.phoneNumber())
                .password(passwordEncoder.encode(createUserRequest.password()))
                .role(createUserRequest.role())
                .build();

        return userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return findUserById(userId);
    }

    public Page<User> getUsersWithFilter(Pageable pageable, UserFilter userFilter) {
        return findUsersWithSpecification(pageable, userFilter);
    }

    public User updateUser(Long userId, UpdateUserRequest updateUserRequest) {
        User user = findUserById(userId);

        if (updateUserRequest.phoneNumber() != null)
            userShouldNotExistByPhoneNumber(updateUserRequest.phoneNumber());

        userMapper.updateUserFromDto(updateUserRequest, user);

        return userRepository.save(user);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(USER, ID, userId));
    }

    private void userShouldNotExistByPhoneNumber(String phoneNumber) {
        if (userRepository.existsByPhoneNumber(phoneNumber))
            throw new ResourceAlreadyExists(USER, PHONE_NUMBER, phoneNumber);
    }

    private Page<User> findUsersWithSpecification(Pageable pageable, UserFilter userFilter) {
        Specification<User> spec = UserSpecifications.withFilter(userFilter);
        return userRepository.findAll( spec, pageable);
    }
}
