package org.hitzoft.xml;

import java.util.List;

/**
 *
 * @author jesus.espinoza
 */
public class XMLSignerValidationException extends Exception {

    private List<String> validationMessages;

    public XMLSignerValidationException(List<String> validationMessages) {
        this.validationMessages = validationMessages;
    }

    public List<String> getValidationMessages() {
        return validationMessages;
    }

}
