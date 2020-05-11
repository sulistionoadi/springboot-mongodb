/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sulistionoadi.belajar.jwt.security;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author adi
 */
@Component
public class EncryptionUtil {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final String PUBLIC_KEY_FILE_PATH = System.getProperty("java.io.tmpdir").concat(File.separator).concat("public.key");
    private final String PRIVATE_KEY_FILE_PATH = System.getProperty("java.io.tmpdir").concat(File.separator).concat("private.key");
    private final String CHIPER_ALGORITHM = "RSA";
    private final Charset CHARSET_ENCODING = StandardCharsets.UTF_8;

    @PostConstruct
    public void checkAndGenerateKeypair() {
        /**
         * Check File Private Key *
         */
        File filePrivate = new File(PRIVATE_KEY_FILE_PATH);
        File filePublic = new File(PUBLIC_KEY_FILE_PATH);

        if (!filePrivate.exists() || !filePublic.exists()) {
            try {
                generateKey();
            } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException | NoSuchProviderException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }

    }

    public String encrypt(String text) throws Exception {
        LOGGER.debug("Encrypt Plain Text: {}", text);
        PublicKey pubkey = readPublicKeyFromFile();
        if(pubkey==null){
            throw new Exception("Cannot get Public Key");
        }
        
        Cipher cipher = Cipher.getInstance(CHIPER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, pubkey);

        String encryptedText = Base64.encodeBase64String(cipher.doFinal(text.getBytes(CHARSET_ENCODING)));
        LOGGER.debug("Encryted Text: {}", encryptedText);
        return encryptedText;
    }

    public String decrypt(String text) throws Exception {
        LOGGER.debug("Encrypted Text: {}", text);
        PrivateKey privateKey = readPrivateKeyFromFile();
        if(privateKey==null){
            throw new Exception("Cannot get Private Key");
        }
        
        Cipher chiper = Cipher.getInstance(CHIPER_ALGORITHM);
        chiper.init(Cipher.DECRYPT_MODE, privateKey);

        // encrypt with known character encoding, you should probably use hybrid cryptography instead 
        String decryptedText = new String(chiper.doFinal(Base64.decodeBase64(text)), CHARSET_ENCODING);
        LOGGER.debug("Decrypted Text: {}", decryptedText);
        return decryptedText;
    }

    public PublicKey readPublicKeyFromFile() throws IOException {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            File file = new File(PUBLIC_KEY_FILE_PATH);
            fis = new FileInputStream(file);

            //Get Public Key
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(IOUtils.toByteArray(fis));
            KeyFactory fact = KeyFactory.getInstance("RSA", "SunRsaSign");
            PublicKey publicKey = fact.generatePublic(keySpec);
            return publicKey;
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (ois != null) {
                ois.close();
                if (fis != null) {
                    fis.close();
                }
            }
        }
        return null;
    }
    
    public PrivateKey readPrivateKeyFromFile() throws IOException {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            File file = new File(PRIVATE_KEY_FILE_PATH);
            fis = new FileInputStream(file);

            //Get Public Key
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(IOUtils.toByteArray(fis));
            KeyFactory fact = KeyFactory.getInstance("RSA", "SunRsaSign");
            PrivateKey privateKey = fact.generatePrivate(keySpec);
            return privateKey;
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (ois != null) {
                ois.close();
                if (fis != null) {
                    fis.close();
                }
            }
        }
        return null;
    }

    public void generateKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, NoSuchProviderException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(1024, random);

        KeyPair keyPair = keyGen.genKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        LOGGER.debug("Public Key - " + new String(publicKey.getEncoded()));
        LOGGER.debug("Private Key - " + new String(privateKey.getEncoded()));

        //Share public key with other so they can encrypt data and decrypt thoses using private key(Don't share with Other)
        LOGGER.info("\n--------SAVING PUBLIC KEY AND PRIVATE KEY TO FILES-------\n");
        saveKeys(PUBLIC_KEY_FILE_PATH, publicKey.getEncoded());
        saveKeys(PRIVATE_KEY_FILE_PATH, privateKey.getEncoded());
    }

    private void saveKeys(String fileName, byte[] keyBytes) throws IOException {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            LOGGER.info("Generating " + fileName + "...");
            fos = new FileOutputStream(fileName);
            oos = new ObjectOutputStream(new BufferedOutputStream(fos));

            oos.write(keyBytes);

            LOGGER.info(fileName + " generated successfully");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (oos != null) {
                oos.close();

                if (fos != null) {
                    fos.close();
                }
            }
        }
    }

}
