package org.hitzoft.xml.test;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.hitzoft.xml.DigestMethodAlgorithm;
import org.hitzoft.xml.SignatureMethodAlgorithm;
import org.hitzoft.xml.XMLSigner;
import org.hitzoft.xml.keypair.KeyStoreImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 *
 * @author jesus.espinoza
 */
public class SignXmlTest {

    private Document unsignedXML;
    private Document signedXML;
    private XMLSigner signer;

    @Before
    public void setup() {
        try {
            File unsigned = new File("src/main/resources/example.xml");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            unsignedXML = builder.parse(unsigned);

            signedXML = builder.newDocument();

            signer = new XMLSigner();
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        }
    }

    @After
    public void drop() {
        signedXML = null;
        signer = null;
    }

    @Test
    public void signXMLSHA1WithRSA() {
        try {
            // Load Keystore
            KeyStoreImpl keystore = new KeyStoreImpl("RSA");

            signer.setDigestMethodAlgorithm(DigestMethodAlgorithm.SHA1);
            signer.setSignatureMethodAlgorithm(SignatureMethodAlgorithm.RSA_SHA1);
            signer.setKeyPairProvider(keystore);

            signedXML = signer.sign(unsignedXML);

        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        }
        Assert.assertTrue(valid(signedXML));
    }

    private boolean valid(Document signedXML) {
        try {
            return signer.valid(signedXML);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
