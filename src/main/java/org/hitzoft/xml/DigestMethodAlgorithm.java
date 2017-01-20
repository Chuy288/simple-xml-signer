/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hitzoft.xml;

/**
 *
 * @author jesus.espinoza
 */
public enum DigestMethodAlgorithm {
    SHA1("http://www.w3.org/2000/09/xmldsig#sha1"),
    SHA256("http://www.w3.org/2001/04/xmlenc#sha256"),
    SHA512("http://www.w3.org/2001/04/xmlenc#sha512"),
    RIPEMD160("http://www.w3.org/2001/04/xmlenc#ripemd160");

    private final String URI;

    private DigestMethodAlgorithm(String URI) {
        this.URI = URI;
    }

    @Override
    public String toString() {
        return URI;
    }

}
