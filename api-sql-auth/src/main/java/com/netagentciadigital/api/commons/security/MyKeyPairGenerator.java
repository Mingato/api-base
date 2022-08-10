package com.netagentciadigital.api.commons.security;

import io.micrometer.core.instrument.util.IOUtils;
import lombok.extern.slf4j.Slf4j;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Slf4j
@Service
public class MyKeyPairGenerator {

    private final String ALGORITHM = "RSA";

    @Value("${wm3.security.privateKey-file}")
    private String PRIVATE_KEY_FILE;

    @Value("${wm3.security.publicKey-file}")
    private String PUBLIC_KEY_FILE;


    private PublicKey getPublicKeyFromFile() {
        log.info("getPublicKeyFromFile");

        try {
            String publicKeyPEM = "";
            if(PUBLIC_KEY_FILE.contains("http")){
                try (InputStream inputStream = (new URL(PUBLIC_KEY_FILE)).openStream()) {
                    assert inputStream != null;
                    publicKeyPEM = IOUtils.toString(inputStream);
                }
            }else {
                try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PUBLIC_KEY_FILE)) {
                    assert inputStream != null;
                    publicKeyPEM = IOUtils.toString(inputStream);
                }
            }

            publicKeyPEM = publicKeyPEM
                    .replace("-----BEGIN PUBLIC KEY-----\r\n", "")
                    .replace("-----BEGIN PUBLIC KEY-----\n", "")
                    .replace("-----END PUBLIC KEY-----", "");

            return KeyFactory.getInstance(ALGORITHM).generatePublic(
                    new X509EncodedKeySpec(Base64.decodeBase64(publicKeyPEM))
            );
        }catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e2){
            e2.printStackTrace();
        }

        return null;
    }

    private PrivateKey getPrivateKeyFromFile() {
        log.info("getPrivateKeyFromFile");
        try {
            String privateKeyPEM = "";

            if(PRIVATE_KEY_FILE.equals("http")){
                try (InputStream inputStream = (new URL(PRIVATE_KEY_FILE)).openStream()) {
                    assert inputStream != null;
                    privateKeyPEM = IOUtils.toString(inputStream);
                }
            }else {
                try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PRIVATE_KEY_FILE)) {
                    assert inputStream != null;
                    privateKeyPEM = IOUtils.toString(inputStream);
                }
            }

            privateKeyPEM = privateKeyPEM
                    .replace("-----BEGIN PRIVATE KEY-----\r\n", "")
                    .replace("-----BEGIN PRIVATE KEY-----\n", "")
                    .replace("-----END PRIVATE KEY-----", "");

            return KeyFactory.getInstance(ALGORITHM).generatePrivate(
                    new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyPEM))
            );
        }catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e2){
            e2.printStackTrace();
        }

        return null;
    }

    public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        //openssl genrsa -out mykey.pem 2048
        //openssl pkcs8 -topk8 -inform PEM -outform PEM -in mykey.pem -out private_key.pem -nocrypt
        //openssl rsa -in mykey.pem -out public_key.pem -pubout -outform PEM

        log.info("Generating keyPair from file");
        PrivateKey privateKey = getPrivateKeyFromFile();
        PublicKey publicKey = getPublicKeyFromFile();

        if(publicKey != null && privateKey != null) {
            return new KeyPair(publicKey, privateKey);
        }
        log.info("KeyPair from file does not work");

        log.info("Generating random keyPair");
        java.security.KeyPairGenerator gen = java.security.KeyPairGenerator.getInstance(ALGORITHM);
        int KEY_SIZE = 2048;
        gen.initialize(KEY_SIZE);

        return gen.generateKeyPair();
    }

}
