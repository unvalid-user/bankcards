package com.example.bankcards.util;

import com.example.bankcards.exception.EncryptorException;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;


@Component
public class Encryptor {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;
    @Value("${app.encryptor.secret}")
    private String SECRET;

    public String encrypt(String data) {
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);

        byte[] result = encrypt(dataBytes);

        return Base64.getEncoder().encodeToString(result);
    }
    public byte[] encrypt(byte[] data) {
        byte[] iv = generateIv();
        byte[] encryptedBytes = initCipherAndDoFinal(Cipher.ENCRYPT_MODE, iv, data);

        return combineByteArrays(iv, encryptedBytes);
    }

    public String decrypt(String data) {
        byte[] dataBytes = Base64.getDecoder().decode(data);
        byte[] decryptedBytes = decrypt(dataBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
    public byte[] decrypt(byte[] data) {
        byte[] iv = Arrays.copyOfRange(data, 0, IV_LENGTH);
        byte[] encryptedBytes = Arrays.copyOfRange(data, IV_LENGTH, data.length);

        return initCipherAndDoFinal(Cipher.DECRYPT_MODE, iv, encryptedBytes);
    }


    private byte[] initCipherAndDoFinal(int opmode, byte[] iv, byte[] data) {
        SecretKey key = new SecretKeySpec(Decoders.BASE64.decode(SECRET), "AES");
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(opmode, key, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            return cipher.doFinal(data);
        }
        catch (Exception e) {
            throw new EncryptorException("Error while encrypting/decrypting data.", e);
        }
    }

    private byte[] generateIv() {
        byte[] iv = new byte[IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        return iv;
    }

    private byte[] combineByteArrays(byte[] a, byte[] b) {
        byte[] combined = new byte[a.length + b.length];
        System.arraycopy(a, 0, combined, 0, a.length);
        System.arraycopy(b, 0, combined, a.length, b.length);

        return combined;
    }
}
