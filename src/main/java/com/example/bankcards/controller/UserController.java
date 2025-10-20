package com.example.bankcards.controller;

import com.example.bankcards.dto.response.PagedResponse;
import com.example.bankcards.dto.mapper.UserMapper;
import com.example.bankcards.dto.request.CreateUserRequest;
import com.example.bankcards.dto.request.UpdateUserRequest;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.User;
import com.example.bankcards.dto.filter.UserFilter;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.example.bankcards.util.AppConst.DEFAULT_PAGE_SIZE;


@RestController
@RequestMapping("/users")
@Secured("ROLE_ADMIN")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;


    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest createUserRequest
    ) {
       User createdUser = userService.createUser(createUserRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/users/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(userMapper.toResponse(createdUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable("id") Long userId
    ) {
        User user = userService.getUserById(userId);

       return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @GetMapping("/all")
    public ResponseEntity<PagedResponse<UserResponse>> getAllUsers(
            @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable,
            @ModelAttribute UserFilter userFilter
    ) {
        Page<User> pageUsers = userService.getUsersWithFilter(pageable, userFilter);

        return ResponseEntity.ok(userMapper.toPagedResponse(pageUsers));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable("id") Long userId,
            @Valid @RequestBody UpdateUserRequest updateUserRequest
    ) {
        User user = userService.updateUser(userId, updateUserRequest);

        return ResponseEntity.ok(userMapper.toResponse(user));
    }


}
