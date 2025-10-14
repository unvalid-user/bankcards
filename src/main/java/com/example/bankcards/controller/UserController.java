package com.example.bankcards.controller;

import com.example.bankcards.dto.user.CreateUserRequest;
import com.example.bankcards.dto.user.UpdateUserRequest;
import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.repository.specification.UserFilter;
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


    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest createUserRequest
    ) {
       UserResponse createdUser = userService.createUser(createUserRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/users/{id}")
                .buildAndExpand(createdUser.id())
                .toUri();

        return ResponseEntity.created(location)
                .body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable("id") Long userId
    ) {
       UserResponse user = userService.getUserById(userId);

       return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable,
            @ModelAttribute UserFilter userFilter
    ) {
        Page<UserResponse> pagedUsers = userService.getUsersWithFilter(pageable, userFilter);

        return ResponseEntity.ok(pagedUsers);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable("id") Long userId,
            @Valid @RequestBody UpdateUserRequest updateUserRequest
    ) {
        UserResponse user = userService.updateUser(userId, updateUserRequest);

        return ResponseEntity.ok(user);
    }


}
