package org.hitzoft.xml.signer;


/**
 *
 * @author jesus.espinoza
 */
public interface Signer {

    public byte[] sign(SignatureType signatureType, byte[] data) throws SignerException;

    public boolean verify(SignatureType signatureType, byte[] signedData, byte[] data) throws SignerException;

}
