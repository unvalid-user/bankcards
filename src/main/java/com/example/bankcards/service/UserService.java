package com.example.bankcards.service;

import com.example.bankcards.dto.request.CreateUserRequest;
import com.example.bankcards.dto.request.UpdateUserRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ResourceAlreadyExistsException;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.dto.filter.UserFilter;
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
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public User findUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new ResourceNotFoundException(USER, PHONE_NUMBER, phoneNumber));
    }

    public User createUser(CreateUserRequest request) {
        userShouldNotExistByPhoneNumber(request.phoneNumber());

        User user = User.builder()
                .phoneNumber(request.phoneNumber())
                .password(passwordEncoder.encode(request.password()))
                .role(roleService.findRoleByName(request.role()))
                .build();

        return userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return findUserById(userId);
    }

    public Page<User> getUsersWithFilter(Pageable pageable, UserFilter userFilter) {
        return findUsersWithSpecification(pageable, userFilter);
    }

    public User updateUser(Long userId, UpdateUserRequest request) {
        User user = findUserById(userId);

        if (request.phoneNumber() != null) {
            userShouldNotExistByPhoneNumber(request.phoneNumber());
            user.setPhoneNumber(request.phoneNumber());
        }
        if (request.password() != null) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        return userRepository.save(user);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(USER, ID, userId));
    }

    private void userShouldNotExistByPhoneNumber(String phoneNumber) {
        if (userRepository.existsByPhoneNumber(phoneNumber))
            throw new ResourceAlreadyExistsException(USER, PHONE_NUMBER, phoneNumber);
    }

    private Page<User> findUsersWithSpecification(Pageable pageable, UserFilter userFilter) {
        Specification<User> spec = UserSpecifications.withFilter(userFilter);
        return userRepository.findAll( spec, pageable);
    }
}
