package com.example.bankcards.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EncryptorTest {
    @Autowired
    private Encryptor encryptor;

    @Test
    void encryptorTest() {
        String cardNumber = "1234234534564567";

        String encryptedData = encryptor.encrypt(cardNumber);
        String decryptedData = encryptor.decrypt(encryptedData);

        assertThat(decryptedData).isEqualTo(cardNumber);
    }
}
