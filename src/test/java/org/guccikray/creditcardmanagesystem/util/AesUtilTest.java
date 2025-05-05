package org.guccikray.creditcardmanagesystem.util;

import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AesUtilTest {

    @Test
    void testEncrypt()
        throws InvalidAlgorithmParameterException,
        NoSuchPaddingException,
        IllegalBlockSizeException,
        NoSuchAlgorithmException,
        BadPaddingException,
        InvalidKeyException
    {
        String text = "test";

        String encrypted = AesUtil.encrypt(text);
        String decrypted = AesUtil.decrypt(encrypted);


        assertEquals(text, decrypted);
    }
}
