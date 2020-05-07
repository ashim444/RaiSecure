package com.example.raisecure.home.views.utils;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import androidx.annotation.RequiresApi;
import androidx.security.crypto.MasterKeys;

import com.example.raisecure.home.views.Const;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.util.Calendar;

import javax.security.auth.x500.X500Principal;

public class KeyGenerator {


    @RequiresApi(api = Build.VERSION_CODES.M)
    static SecurityKey generateSecretKey(KeyStore keyStore) {
        try {
            if (!keyStore.containsAlias(Const.KEY_ALIAS)) {
                javax.crypto.KeyGenerator keyGenerator =
                        javax.crypto.KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, Const.ANDROID_KEY_STORE);
                keyGenerator.init(new KeyGenParameterSpec.Builder(Const.KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT).setBlockModes(
                        KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setRandomizedEncryptionRequired(false)
                        .build());
                return new SecurityKey(keyGenerator.generateKey());
            }
        } catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
        }
        try {
            final KeyStore.SecretKeyEntry entry =
                    (KeyStore.SecretKeyEntry) keyStore.getEntry(Const.KEY_ALIAS, null);
            return new SecurityKey(entry.getSecretKey());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableEntryException e) {
        }
        return null;
    }

    //this is the solution if the api is below 23
    static SecurityKey generateKeyPairPreM(Context context, KeyStore keyStore) {
        try {
            if (!keyStore.containsAlias(Const.KEY_ALIAS)) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                //1 Year validity
                end.add(Calendar.YEAR, 1);

                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context).setAlias(Const.KEY_ALIAS)
                        .setSubject(new X500Principal("CN=" + Const.KEY_ALIAS))
                        .setSerialNumber(BigInteger.TEN)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();

                KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", Const.ANDROID_KEY_STORE);
                kpg.initialize(spec);
                kpg.generateKeyPair();
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchProviderException e) {
        }

        try {
            final KeyStore.PrivateKeyEntry entry =
                    (KeyStore.PrivateKeyEntry) keyStore.getEntry(Const.KEY_ALIAS, null);
            return new SecurityKey(
                    new KeyPair(entry.getCertificate().getPublicKey(), entry.getPrivateKey()));
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableEntryException e) {
        }
        return null;
    }

    public void createKey() throws GeneralSecurityException, IOException {
        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
        MasterKeys.getOrCreate(keyGenParameterSpec);
    }
}
