package com.bpp.oauthserver;

import com.bpp.oauthserver.security.KeyUtil;

import javax.crypto.Cipher;
import java.security.PublicKey;
import java.util.Base64;

public class TestEncrypt {
    public static void main(String[] args) throws Exception {

        PublicKey publicKey = KeyUtil.loadPublicKey("C:\\Users\\MyPC\\Downloads\\oauthserver\\oauthserver\\src\\main\\resources\\public_cert.pem");

        String plainText = "{\n" +
                "    \"productName\":\"Tshirt\",\n" +
                "    \"quantity\":1\n" +
                "}";

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] encrypted = cipher.doFinal(plainText.getBytes());

        String base64 = Base64.getEncoder().encodeToString(encrypted);

        System.out.println("Encrypted:");
        System.out.println(base64);
    }
}
