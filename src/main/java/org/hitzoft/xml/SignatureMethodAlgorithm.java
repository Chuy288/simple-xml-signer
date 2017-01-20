package org.hitzoft.xml;

/**
 *
 * @author jesus.espinoza
 */
public enum SignatureMethodAlgorithm {
    DSA_SHA1("http://www.w3.org/2000/09/xmldsig#dsa-sha1"),
    DSA_SHA256("http://www.w3.org/2009/xmldsig11#dsa-sha256"),
    
    RSA_SHA1("http://www.w3.org/2000/09/xmldsig#rsa-sha1"),
    RSA_SHA256("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256"),
    RSA_SHA512("http://www.w3.org/2001/04/xmldsig-more#rsa-sha512");

    private final String URI;

    private SignatureMethodAlgorithm(String URI) {
        this.URI = URI;
    }

    @Override
    public String toString() {
        return URI;
    }
}
