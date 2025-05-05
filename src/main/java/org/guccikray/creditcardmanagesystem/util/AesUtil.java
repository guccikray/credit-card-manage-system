package org.guccikray.creditcardmanagesystem.util;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

/**
 * Утилитный класс для работы с шифрованием данных
 */
@Component
public final class AesUtil {

    private static final String SHA = "SHA-256";
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_ALGORITHM_GCM = "AES/GCM/NoPadding";
    // для gcm алгоритма рекомендуется использовать iv длинною в 12 байт
    private static final Integer IV_LENGTH = 12;
    // тэг должен быть 16 байт, но мы передаём его в битах
    private static final Integer TAG_LENGTH = 128;
    // так открыто храниться не должно, но в нашем случае допустимо
    private static final String SECRET_PASSPHRASE = "aesSecretPassword";

    //TODO catch exceptions
    public static String encrypt(@NotNull String plainText) throws NoSuchPaddingException,
        NoSuchAlgorithmException,
        InvalidAlgorithmParameterException,
        InvalidKeyException,
        IllegalBlockSizeException,
        BadPaddingException
    {
        SecretKeySpec keySpec = generateAesKeyFromSecretPassphrase();
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM_GCM);
        // генерирую рандомный iv, для обеспечения большей безопасности
        GCMParameterSpec gmcSpec = generateIv();
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gmcSpec);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        byte[] iv = gmcSpec.getIV();

        byte[] combinedIvAndText = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combinedIvAndText, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combinedIvAndText, iv.length, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(combinedIvAndText);
    }

    public static String decrypt(@NotNull String cipherText) throws NoSuchAlgorithmException,
        NoSuchPaddingException,
        InvalidAlgorithmParameterException,
        InvalidKeyException,
        IllegalBlockSizeException,
        BadPaddingException
    {
        byte[] decodedCipherText = Base64.getDecoder().decode(cipherText);

        SecretKeySpec keySpec = generateAesKeyFromSecretPassphrase();

        byte[] iv = new byte[IV_LENGTH];
        System.arraycopy(decodedCipherText, 0, iv, 0, iv.length);
        byte[] encryptedText = new byte[decodedCipherText.length - IV_LENGTH];
        System.arraycopy(decodedCipherText, IV_LENGTH, encryptedText, 0, encryptedText.length);

        GCMParameterSpec gmcSpec = new GCMParameterSpec(TAG_LENGTH, iv);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM_GCM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gmcSpec);
        // здесь дешифрую только часть без iv, т.к. она не входит в шифр,
        // а лишь является частью данных и кладётся рядом чтобы в дальнейшем дешифровать текст
        byte[] decryptedBytes = cipher.doFinal(encryptedText);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * Метод для генерации случайного IV, для работы с GCM режимом AES алгоритма
     * @return GCM параметры
     */
    private static GCMParameterSpec generateIv() {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return new GCMParameterSpec(TAG_LENGTH, iv);
    }

    /**
     * Метод генерирует секретный ключ на основе переданной фразы
     * @return сгенерированный ключ
     * @throws NoSuchAlgorithmException
     */
    private static SecretKeySpec generateAesKeyFromSecretPassphrase() throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance(SHA);
        byte[] keyBytes = sha256.digest(SECRET_PASSPHRASE.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, AES_ALGORITHM);
    }
}
