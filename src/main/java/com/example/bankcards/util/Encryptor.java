package com.example.bankcards.util;

import com.example.bankcards.exception.EncryptorException;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;


// TODO:
//  - test
//  - refactor
@Component
public class Encryptor {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;
    // TODO: store secret in app constants
    private static final byte[] SECRET = "secretKey".getBytes();


    public static String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            byte[] iv = new byte[IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            SecretKey key = new SecretKeySpec(SECRET, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());

            byte[] result = new byte[iv.length + encryptedBytes.length];
            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, result, 0, encryptedBytes.length);

            return new String(result);
        }
        catch (Exception e) {
            throw new EncryptorException("Error while encrypting data", e);
        }
    }

    public static String decrypt(String encrypted) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            byte[] iv = Arrays.copyOfRange(encrypted.getBytes(), 0, IV_LENGTH);
            byte[] encryptedBytes = Arrays.copyOfRange(encrypted.getBytes(), IV_LENGTH, encrypted.length());

            SecretKey key = new SecretKeySpec(SECRET, "AES");
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes);
        }
        catch (Exception e) {
            throw new EncryptorException("Error while decrypting data", e);
        }
    }
}
