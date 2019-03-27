package com.prilaga.data.utils;

import android.R.string;
import android.util.Base64;
import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * A class to make more easy simple encrypt routines
 */
public final class Encryption {

    private static final String TAG = "Encryption";

    private String mCharsetName = "UTF8";
    private String mAlgorithm = "DES";
    private int mBase64Mode = Base64.DEFAULT;

    /**
     * @return the charset name
     */
    public String getCharsetName() {
        return mCharsetName;
    }

    /**
     * @param charsetName the new charset name
     */
    public void setCharsetName(String charsetName) {
        mCharsetName = charsetName;
    }

    /**
     * @return the mAlgorithm used
     */
    public String getAlgorithm() {
        return mAlgorithm;
    }

    /**
     * @param algorithm the mAlgorithm to be used
     */
    public void setAlgorithm(String algorithm) {
        mAlgorithm = algorithm;
    }

    /**
     * @return the Base 64 mode
     */
    public int getBase64Mode() {
        return mBase64Mode;
    }

    /**
     * @param base64Mode set the base 64 mode
     */
    public void setBase64Mode(int base64Mode) {
        mBase64Mode = base64Mode;
    }

    /**
     * Encrypt a {@link string}
     *
     * @param key  the {@link String} key
     * @param data the {@link String} to be encrypted
     * @return the encrypted {@link String} or <code>null</code> if occur some error
     */
    public String encrypt(String key, String data) {
        if (key == null || data == null) return null;
        try {
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(mCharsetName));
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(mAlgorithm);
            SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
            byte[] dataBytes = data.getBytes(mCharsetName);
            Cipher cipher = Cipher.getInstance(mAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.encodeToString(cipher.doFinal(dataBytes), mBase64Mode);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    /**
     * Decrypt a {@link string}
     *
     * @param key  the {@link String} key
     * @param data the {@link String} to be decrypted
     * @return the decrypted {@link String} or <code>null</code> if occur some error
     */
    public String decrypt(String key, String data) {
        if (key == null || data == null) return null;
        try {
            byte[] dataBytes = Base64.decode(data, mBase64Mode);
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(mCharsetName));
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(mAlgorithm);
            SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
            Cipher cipher = Cipher.getInstance(mAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] dataBytesDecrypted = (cipher.doFinal(dataBytes));
            return new String(dataBytesDecrypted);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

}
