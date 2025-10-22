package com.mobileapptracker;

import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: classes.dex */
public class MATEncryption {
    private Cipher cipher;
    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;

    public MATEncryption(String SecretKey, String iv) {
        this.ivspec = new IvParameterSpec(iv.getBytes());
        this.keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");
        try {
            this.cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e2) {
            e2.printStackTrace();
        }
    }

    public byte[] encrypt(String text) throws Exception {
        if (text == null || text.length() == 0) {
            throw new Exception("Empty string");
        }
        try {
            this.cipher.init(1, this.keyspec, this.ivspec);
            byte[] encrypted = this.cipher.doFinal(padString(text).getBytes());
            return encrypted;
        } catch (Exception e) {
            throw new Exception("[encrypt] " + e.getMessage());
        }
    }

    public byte[] decrypt(String code) throws Exception {
        if (code == null || code.length() == 0) {
            throw new Exception("Empty string");
        }
        try {
            this.cipher.init(2, this.keyspec, this.ivspec);
            byte[] decrypted = this.cipher.doFinal(MATUtils.hexToBytes(code));
            return decrypted;
        } catch (Exception e) {
            throw new Exception("[decrypt] " + e.getMessage());
        }
    }

    private static String padString(String source) {
        int x = source.length() % 16;
        int padLength = 16 - x;
        for (int i = 0; i < padLength; i++) {
            source = String.valueOf(source) + ' ';
        }
        return source;
    }
}
