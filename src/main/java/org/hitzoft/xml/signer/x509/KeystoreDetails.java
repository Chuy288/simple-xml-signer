package org.hitzoft.xml.signer.x509;

/**
 *
 * @author jesus.espinoza
 */
public class KeystoreDetails {

    private final String keystorePath;
    private final char[] keystorePassword;
    private final String privateKeyAlias;
    private final char[] privateKeyPassword;
    private final String certificateAlias;

    public KeystoreDetails(String keystorePath, char[] keystorePassword, String privateKeyAlias, char[] privateKeyPassword, String certificateAlias) {
        this.keystorePath = keystorePath;
        this.keystorePassword = keystorePassword;
        this.privateKeyAlias = privateKeyAlias;
        this.privateKeyPassword = privateKeyPassword;
        this.certificateAlias = certificateAlias;
    }

    public String getKeystorePath() {
        return keystorePath;
    }

    public String getPrivateKeyAlias() {
        return privateKeyAlias;
    }

    public char[] getKeystorePassword() {
        return keystorePassword;
    }

    public String getCertificateAlias() {
        return certificateAlias;
    }

    public char[] getPrivateKeyPassword() {
        return privateKeyPassword;
    }

}
