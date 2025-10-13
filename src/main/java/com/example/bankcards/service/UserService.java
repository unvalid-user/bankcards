package com.example.bankcards.service;

import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.bankcards.util.AppConst.PHONE_NUMBER;
import static com.example.bankcards.util.AppConst.USER;

public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new ResourceNotFoundException(USER, PHONE_NUMBER, phoneNumber));
    }
}
