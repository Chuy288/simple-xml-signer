package org.hitzoft.xml.keypair;

import java.security.KeyPair;

/**
 *
 * @author jesus.espinoza
 */
public interface KeyPairHolder {

    public KeyPair getKeyPair() throws KeyPairHolderException;

}
