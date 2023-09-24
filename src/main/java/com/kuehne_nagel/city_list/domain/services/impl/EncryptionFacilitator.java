package com.kuehne_nagel.city_list.domain.services.impl;

import com.kuehne_nagel.city_list.domain.entities.enums.ErrorCodes;
import com.kuehne_nagel.city_list.domain.exception.DomainException;
import com.kuehne_nagel.city_list.domain.services.CommonService;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Helper service for encryption and decryption
 * TODO: use async encryption method , and key rotation
 */
@Service
public class EncryptionFacilitator implements CommonService {

    private SecretKeySpec secretKey;

    public void setKey(String myKey) throws DomainException {
        try {
            secretKey = new SecretKeySpec(myKey.getBytes(StandardCharsets.UTF_8), "AES");
        } catch (Exception e) {
            logger.error(ErrorCodes.CITY_ERROR_CREATING_SECRET_KEY.getDescription(), e);
            throw new DomainException(String.format(ErrorCodes.CITY_ERROR_CREATING_SECRET_KEY.getMessage(), e.getLocalizedMessage()), ErrorCodes.CITY_ERROR_CREATING_SECRET_KEY.getCode());
        }
    }

    /**
     * Encrypt the String.
     *
     * @param strToEncrypt
     * @param secret
     * @return
     */
    public String encrypt(String strToEncrypt, String secret) throws DomainException {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            logger.error(ErrorCodes.CITY_ERROR_ENCRYPTING.getDescription(), e);
            throw new DomainException(String.format(ErrorCodes.CITY_ERROR_ENCRYPTING.getMessage(), e.getLocalizedMessage()), ErrorCodes.CITY_ERROR_ENCRYPTING.getCode());
        }
    }

    /**
     * Decrypt the String.
     *
     * @param strToDecrypt
     * @param secret
     * @return
     */
    public String decrypt(String strToDecrypt, String secret) throws DomainException {
        logger.info("strToDecrypt: ", strToDecrypt);
        logger.info("secret: ", secret);
        try {
            setKey(secret);
            byte[] encryptedBytes = Base64.getDecoder().decode(strToDecrypt);

            // Extract the IV (first 16 bytes)
            byte[] ivBytes = new byte[16];
            System.arraycopy(encryptedBytes, 0, ivBytes, 0, 16);

            // Initialize the Cipher with the IV and decryption key
            SecretKey secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

            // Decrypt the data
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes, 16, encryptedBytes.length - 16);

            // Convert the decrypted bytes to a string
            String decryptedText = new String(decryptedBytes, "UTF-8");

            return decryptedText;
        } catch (Exception e) {
            logger.error(ErrorCodes.CITY_ERROR_DECRYPTING.getDescription(), e);
            throw new DomainException(String.format(ErrorCodes.CITY_ERROR_DECRYPTING.getMessage(), e.getLocalizedMessage()), ErrorCodes.CITY_ERROR_DECRYPTING.getCode());
        }
    }

}
