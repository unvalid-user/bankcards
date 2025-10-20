package com.example.bankcards.service;

import com.example.bankcards.dto.mapper.UserMapper;
import com.example.bankcards.dto.request.CreateUserRequest;
import com.example.bankcards.dto.request.UpdateUserRequest;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.RoleName;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ResourceAlreadyExists;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;


    @Test
    void createUser_shouldCreateSuccessfully() {
        Long userId = 3L;
        String phoneNumber = "89123456789";
        String password = "password";
        String encodedPassword = "encoded";
        RoleName role = RoleName.ROLE_USER;
        CreateUserRequest request = new CreateUserRequest(
                phoneNumber,
                password,
                role
        );


        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User user = inv.getArgument(0);
            user.setId(userId);
            return user;
        });
        when(userRepository.existsByPhoneNumber(phoneNumber)).thenReturn(false);
        when(roleService.findRoleByName(role)).thenReturn(new Role(role));


        User user = userService.createUser(request);


        assertEquals(user.getId(), userId);
        assertEquals(user.getPassword(), encodedPassword);
        assertEquals(user.getPhoneNumber(), phoneNumber);
        assertEquals(user.getRole().getName(), role);
    }

    @Test
    void createUser_shouldThrowWhenAlreadyExistsByPhoneNumber() {
        Long userId = 3L;
        String phoneNumber = "89123456789";
        String password = "password";
        String encodedPassword = "encoded";
        RoleName role = RoleName.ROLE_USER;
        CreateUserRequest request = new CreateUserRequest(
                phoneNumber,
                password,
                role
        );


        when(userRepository.existsByPhoneNumber(phoneNumber)).thenReturn(true);


        assertThrows(ResourceAlreadyExists.class, () -> {
            userService.createUser(request);
        });
    }

    @Test
    void updateUser_shouldUpdateSuccessfully() {
        Long userId = 3L;
        String phoneNumber = "89123456789";
        String password = "password";
        String encodedPassword = "encoded";
        RoleName role = RoleName.ROLE_USER;

        User user = User.builder()
                .id(userId)
                .role(new Role(role))
                .build();
        UpdateUserRequest request = new UpdateUserRequest(
                phoneNumber,
                password
        );


        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userRepository.existsByPhoneNumber(phoneNumber)).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));


        User updatedUser = userService.updateUser(userId, request);


        assertEquals(updatedUser.getId(), userId);
        assertEquals(updatedUser.getPassword(), encodedPassword);
        assertEquals(updatedUser.getPhoneNumber(), phoneNumber);
        assertEquals(updatedUser.getRole().getName(), role);
    }

    @Test
    void updateUser_shouldThrowWhenAlreadyExistsByPhoneNumber() {
        Long userId = 3L;
        String phoneNumber = "89123456789";
        String password = "password";
        String encodedPassword = "encoded";
        RoleName role = RoleName.ROLE_USER;

        User user = User.builder()
                .id(userId)
                .role(new Role(role))
                .build();
        UpdateUserRequest request = new UpdateUserRequest(
                phoneNumber,
                password
        );


        when(userRepository.existsByPhoneNumber(phoneNumber)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));


        assertThrows(ResourceAlreadyExists.class, () -> userService.updateUser(userId, request));
    }

    @Test
    void updateUser_shouldThrowWhenUserNotExists() {
        Long userId = 3L;
        String phoneNumber = "89123456789";
        String password = "password";
        String encodedPassword = "encoded";
        RoleName role = RoleName.ROLE_USER;

        UpdateUserRequest request = new UpdateUserRequest(
                phoneNumber,
                password
        );


        when(userRepository.findById(userId)).thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(userId, request));
    }
}
