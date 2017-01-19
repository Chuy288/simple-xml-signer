package org.hitzoft.xml.signer;

import java.security.GeneralSecurityException;

/**
 *
 * @author jesus.espinoza
 */
public class SignerException extends GeneralSecurityException {

    public SignerException(String msg) {
        super(msg);
    }

    public SignerException(Throwable cause) {
        super(cause);
    }

}
