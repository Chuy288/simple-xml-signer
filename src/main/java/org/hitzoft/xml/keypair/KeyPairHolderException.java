package org.hitzoft.xml.keypair;

import java.security.GeneralSecurityException;

/**
 *
 * @author jesus.espinoza
 */
public class KeyPairHolderException extends GeneralSecurityException {

    public KeyPairHolderException(String msg) {
        super(msg);
    }

    public KeyPairHolderException(Throwable cause) {
        super(cause);
    }

}
