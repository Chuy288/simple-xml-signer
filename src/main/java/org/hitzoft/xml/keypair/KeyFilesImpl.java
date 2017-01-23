package org.hitzoft.xml.keypair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import org.apache.commons.ssl.PKCS8Key;

/**
 *
 * @author jesus.espinoza
 */
public class KeyFilesImpl implements KeyPairHolder {

    private final KeyPair keyPair;

    public KeyFilesImpl(String privateKeyPath, String privateKeyPassword, String certificatePath) throws KeyPairHolderException {

        PrivateKey privateKey = loadPKCS8Key(privateKeyPath, privateKeyPassword);
        PublicKey publicKey = loadX509Certificate(certificatePath);

        this.keyPair = new KeyPair(publicKey, privateKey);
    }

    private PrivateKey loadPKCS8Key(String privateKeyPath, String password) throws KeyPairHolderException {
        try {
            FileInputStream in = new FileInputStream(privateKeyPath);
            PKCS8Key pkcs8 = new PKCS8Key(in, password.toCharArray());
            return pkcs8.getPrivateKey();
        } catch (GeneralSecurityException ex) {
            throw new KeyPairHolderException("Error trying to load private key file", ex);
        } catch (IOException ex) {
            throw new KeyPairHolderException("Error trying to load private key file", ex);
        }
    }

    private PublicKey loadX509Certificate(String certificatePath) throws KeyPairHolderException {
        try {
            FileInputStream inputStream = new FileInputStream(certificatePath);

            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
            return certificate.getPublicKey();
        } catch (FileNotFoundException ex) {
            throw new KeyPairHolderException("Error trying to load certificate file", ex);
        } catch (CertificateException ex) {
            throw new KeyPairHolderException("Error trying to load certificate file", ex);
        }
    }

    @Override
    public KeyPair getKeyPair() {
        return keyPair;
    }

}
