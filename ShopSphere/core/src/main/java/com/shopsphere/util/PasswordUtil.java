package com.shopsphere.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Utility for hashing and verifying passwords using PBKDF2 with HMAC-SHA256.
 * Salted hashes are stored in the format salt:hash (Base64 encoded).
 */
public class PasswordUtil {

    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {
        // Private constructor for utility class
    }

    /**
     * Generates a salted PBKDF2 hash of the given plain-text password.
     *
     * @param password plain text password
     * @return salt:hash string
     */
    public static String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        byte[] hash = pbkdf2(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Verifies a plain-text password against a stored salt:hash string.
     *
     * @param password plain text password
     * @param storedHash stored salt:hash string
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String storedHash) {
        if (password == null || storedHash == null || !storedHash.contains(":")) {
            return false;
        }
        String[] parts = storedHash.split(":");
        if (parts.length != 2) {
            return false;
        }
        try {
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] hash = Base64.getDecoder().decode(parts[1]);
            byte[] testHash = pbkdf2(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);

            if (hash.length != testHash.length) {
                return false;
            }
            int diff = 0;
            for (int i = 0; i < hash.length; i++) {
                diff |= hash[i] ^ testHash[i];
            }
            return diff == 0;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int keyLength) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Error generating PBKDF2 key", e);
        }
    }

    public static void main(String[] args) {
        String adminPass = "Admin@123";
        String custPass = "Customer@123";
        String adminHash = hashPassword(adminPass);
        String custHash = hashPassword(custPass);
        System.out.println("ADMIN_HASH: " + adminHash);
        System.out.println("CUSTOMER_HASH: " + custHash);
        System.out.println("VERIFY ADMIN: " + verifyPassword(adminPass, adminHash));
        System.out.println("VERIFY CUST: " + verifyPassword(custPass, custHash));
    }
}
