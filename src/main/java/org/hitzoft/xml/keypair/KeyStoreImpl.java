package org.hitzoft.xml.keypair;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

/**
 *
 * @author jesus.espinoza
 */
public class KeyStoreImpl implements KeyPairHolder {

    private final KeyPair keyPair;

    public KeyStoreImpl(InputStream keyStoreInputStream, String keyStorePassword, String aliasName, String aliasPassword) throws KeyStoreException, IOException {

        try {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());

            keystore.load(keyStoreInputStream, keyStorePassword.toCharArray());
            Key privateKey = keystore.getKey(aliasName, aliasPassword.toCharArray());
            if (!(privateKey instanceof PrivateKey)) {
                throw new KeyStoreException("The keystore doesn't contain a private key with the alias: " + aliasName);
            }
            Certificate certificate = keystore.getCertificate(aliasName);
            PublicKey publicKey = certificate.getPublicKey();
            keyPair = new KeyPair(publicKey, (PrivateKey) privateKey);

        } catch (CertificateException ex) {
            throw new KeyStoreException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new KeyStoreException(ex);
        } catch (UnrecoverableKeyException ex) {
            throw new KeyStoreException(ex);
        }
    }

    public KeyStoreImpl(String algorithm) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(algorithm);
        kpg.initialize(2048);
        keyPair = kpg.generateKeyPair();
    }

    @Override
    public KeyPair getKeyPair() throws KeyPairHolderException {
        return keyPair;
    }

}
