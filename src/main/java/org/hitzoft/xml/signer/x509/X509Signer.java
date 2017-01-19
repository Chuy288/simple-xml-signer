package org.hitzoft.xml.signer.x509;

import org.hitzoft.xml.signer.SignatureType;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import org.hitzoft.xml.signer.Signer;
import org.hitzoft.xml.signer.SignerException;

/**
 *
 * @author jesus.espinoza
 */
public class X509Signer implements Signer {

    private final KeyPair keyPair;
    private final Certificate certificate;

    public X509Signer(KeystoreDetails keystoreDetails, String keystoreProvider) throws NoSuchAlgorithmException, KeyStoreException, IOException {

        try {
            KeyStore keystore;
            if (keystoreProvider == null) {
                keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            } else {
                keystore = KeyStore.getInstance(keystoreProvider);

            }
            keystore.load(new FileInputStream(keystoreDetails.getKeystorePath()), keystoreDetails.getKeystorePassword());
            Key privateKey = keystore.getKey(keystoreDetails.getPrivateKeyAlias(), keystoreDetails.getPrivateKeyPassword());
            if (!(privateKey instanceof PrivateKey)) {
                throw new KeyStoreException("The keystore doesn't contain a private key with the alias: " + keystoreDetails.getPrivateKeyAlias());
            }
            certificate = keystore.getCertificate(keystoreDetails.getCertificateAlias());
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

    @Override
    public byte[] sign(SignatureType signatureType, byte[] data) throws SignerException {
        try {
            Signature signature = Signature.getInstance(signatureType.toString());
            signature.initSign(keyPair.getPrivate());
            signature.update(data);
            return signature.sign();
        } catch (NoSuchAlgorithmException ex) {
            throw new SignerException(ex);
        } catch (InvalidKeyException ex) {
            throw new SignerException(ex);
        } catch (SignatureException ex) {
            throw new SignerException(ex);
        }
    }

    @Override
    public boolean verify(SignatureType signatureType, byte[] signedData, byte[] data) throws SignerException {
        try {
            Signature signature = Signature.getInstance(signatureType.toString());
            signature.initVerify(keyPair.getPublic());
            signature.update(data);
            return signature.verify(signedData);
        } catch (InvalidKeyException ex) {
            throw new SignerException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new SignerException(ex);
        } catch (SignatureException ex) {
            throw new SignerException(ex);
        }
    }

    public byte[] getCertificate() throws CertificateEncodingException {
        return certificate.getEncoded();
    }
}
