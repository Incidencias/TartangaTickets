/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.utils;

import com.tartangatickets.utils.exceptions.EncrypterException;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * @author ubuntu
 */
public class Encrypter {
    
    private static final String ALG_ENC = "AES/CBC/PKCS5Padding";
    private static final String ALG = "PBKDF2WithHmacSHA1";
    private static final Random RANDOM = new SecureRandom();
    private static final String AES = "AES";
    private static final String FILE_CONF = "mail.conf";
    private static final String FILE_SALT = "salt.conf";
    private static final String PASSWORD = "SergioIsATroll1.+";
    private static final String USER_NAME = "incidencias.tartanga@gmail.com";
    private static final String EMAIL_PASSWORD = "equipoa2018";
    
    /*
    public static void encryptFile() throws EncrypterException {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        storeSalt(salt);
        KeySpec spec = new PBEKeySpec(PASSWORD.toCharArray(), salt, 65536, 128);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_CONF))) {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALG);
            byte [] key = factory.generateSecret(spec).getEncoded();
            SecretKey privKey = new SecretKeySpec(key, 0, key.length, AES);
            Cipher cipher = Cipher.getInstance(ALG_ENC);
            cipher.init(Cipher.ENCRYPT_MODE, privKey);
            String content = USER_NAME + " " + EMAIL_PASSWORD;
            byte [] encodedMessage = cipher.doFinal(content.getBytes());
            byte [] iv = cipher.getIV();
            out.writeObject(iv);
            out.writeObject(encodedMessage);
            System.out.println("Message cyphed and saved");
        } catch (NoSuchAlgorithmException e) {
            throw new EncrypterException("Error encrypting file", e);
        } catch (InvalidKeySpecException e) {
            throw new EncrypterException("Error encrypting file", e);
        } catch (NoSuchPaddingException e) {
            throw new EncrypterException("Error encrypting file", e);
        } catch (InvalidKeyException e) {
            throw new EncrypterException("Error encrypting file", e);
        } catch (FileNotFoundException e) {
            throw new EncrypterException("Error encrypting file", e);
        } catch (IOException e) {
            throw new EncrypterException("Error encrypting file", e);
        } catch (IllegalBlockSizeException e) {
            throw new EncrypterException("Error encrypting file", e);
        } catch (BadPaddingException e) {
            throw new EncrypterException("Error encrypting file", e);
        }
    }
    */
    
    public static List<String> decryptFile() throws EncrypterException {
        List<String> emailCredentials = new ArrayList<>();
        byte[] salt = getSalt();
        KeySpec spec = new PBEKeySpec(PASSWORD.toCharArray(), salt, 65536, 128);
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_CONF))) {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALG);
            byte [] key = factory.generateSecret(spec).getEncoded();
            SecretKey privKey = new SecretKeySpec(key, 0, key.length, AES);

            byte [] iv = (byte []) in.readObject();
            byte [] encodedMessage = (byte []) in.readObject();

            Cipher cipher = Cipher.getInstance(ALG_ENC);
            IvParameterSpec ivParam = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, privKey, ivParam);
            byte [] decodedMessage = cipher.doFinal(encodedMessage);
            String decodedString = new String(decodedMessage);

            emailCredentials.add(decodedString.split(" ")[0]);
            emailCredentials.add(decodedString.split(" ")[1]);
            
        } catch (NoSuchAlgorithmException e) {
            throw new EncrypterException("Error decrypting file", e);
        } catch (InvalidKeySpecException e) {
            throw new EncrypterException("Error decrypting file", e);
        } catch (FileNotFoundException e) {
            throw new EncrypterException("Error decrypting file", e);
        } catch (IOException e) {
            throw new EncrypterException("Error decrypting file", e);
        } catch (ClassNotFoundException e) {
            throw new EncrypterException("Error decrypting file", e);
        } catch (NoSuchPaddingException e) {
            throw new EncrypterException("Error decrypting file", e);
        } catch (InvalidKeyException e) {
            throw new EncrypterException("Error decrypting file", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new EncrypterException("Error decrypting file", e);
        } catch (IllegalBlockSizeException e) {
            throw new EncrypterException("Error decrypting file", e);
        } catch (BadPaddingException e) {
            throw new EncrypterException("Error decrypting file", e);
        }
        return emailCredentials;
    }

    private static void storeSalt(byte[] salt) throws EncrypterException {
        try (FileOutputStream out = new FileOutputStream(FILE_SALT)) {
            out.write(salt);
        } catch (FileNotFoundException e) {
            throw new EncrypterException("Error encrypting file", e);
        } catch (IOException e) {
            throw new EncrypterException("Error encrypting file", e);
        }
    }
    
    private static byte[] getSalt() throws EncrypterException {
        byte [] salt;
        try (FileInputStream in = new FileInputStream(FILE_SALT)) {
            salt = new byte[in.available()];
            in.read(salt);
        } catch (FileNotFoundException e) {
            throw new EncrypterException("Error decrypting file", e);
        } catch (IOException e) {
            throw new EncrypterException("Error decrypting file", e);
        }
        return salt;
    }
}
