package org.hitzoft.xml.keypair;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 *
 * @author jesus.espinoza
 */
public class KeyPairGenerated implements KeyPairHolder {

    private final KeyPair keyPair;

    public KeyPairGenerated(String algorithm, int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(algorithm);
        SecureRandom secureRandom = new SecureRandom();

        kpg.initialize(keySize, secureRandom);
        keyPair = kpg.generateKeyPair();
    }

    @Override
    public KeyPair getKeyPair() {
        return keyPair;
    }
}
