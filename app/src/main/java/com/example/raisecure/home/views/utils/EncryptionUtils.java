package com.example.raisecure.home.views.utils;

import android.content.Context;
import android.os.Build;

import com.example.raisecure.home.views.Const;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class EncryptionUtils {
    public static String encrypt(Context context, String token) {
        SecurityKey securityKey = getSecurityKey(context);
        return securityKey != null ? securityKey.encrypt(token) : null;
    }

    public static String decrypt(Context context, String token) {
        SecurityKey securityKey = getSecurityKey(context);
        return securityKey != null ? securityKey.decrypt(token) : null;
    }

    private static SecurityKey getSecurityKey(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return KeyGenerator.generateSecretKey(getKeyStore());
        } else {
            return KeyGenerator.generateKeyPairPreM(context, getKeyStore());
        }
    }

    private static KeyStore getKeyStore() {
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(Const.ANDROID_KEY_STORE);
            keyStore.load(null);
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
        }
        return keyStore;
    }

}
