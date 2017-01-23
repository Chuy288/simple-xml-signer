package org.hitzoft.xml.test;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.hitzoft.xml.DigestMethodAlgorithm;
import org.hitzoft.xml.SignatureMethodAlgorithm;
import org.hitzoft.xml.XMLSigner;
import org.hitzoft.xml.XMLSignerValidationException;
import org.hitzoft.xml.keypair.KeyFilesImpl;
import org.hitzoft.xml.keypair.KeyPairHolder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 *
 * @author jesus.espinoza
 */
public class SignWithKeyFileslTest {

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

    private boolean valid(Document signedXML) {
        try {
            return signer.valid(signedXML);
        } catch (XMLSignerValidationException ex) {
            System.out.println(ex.getValidationMessages());
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Test
    public void signXMLSHA1WithRSA() {
        try {
            // Load Keystore

            String privateKeyPath = "src/main/resources/AAA010101AAA_FIEL.key";
            String password = "12345678a";
            String certificate = "src/main/resources/aaa010101aaa_FIEL.cer";
            KeyPairHolder keyHolder = new KeyFilesImpl(privateKeyPath, password, certificate);

            signer.setDigestMethodAlgorithm(DigestMethodAlgorithm.SHA1);
            signer.setSignatureMethodAlgorithm(SignatureMethodAlgorithm.RSA_SHA1);
            signer.setKeyPairProvider(keyHolder);

            signedXML = signer.sign(unsignedXML);

        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        }
        Assert.assertTrue(valid(signedXML));
    }

    @Test
    public void signXMLSHA256WithRSA() {
        try {
            // Load Keystore

            String privateKeyPath = "src/main/resources/AAA010101AAA_FIEL.key";
            String password = "12345678a";
            String certificate = "src/main/resources/aaa010101aaa_FIEL.cer";
            KeyPairHolder keyHolder = new KeyFilesImpl(privateKeyPath, password, certificate);

            signer.setDigestMethodAlgorithm(DigestMethodAlgorithm.SHA256);
            signer.setSignatureMethodAlgorithm(SignatureMethodAlgorithm.RSA_SHA256);
            signer.setKeyPairProvider(keyHolder);

            signedXML = signer.sign(unsignedXML);

        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        }
        Assert.assertTrue(valid(signedXML));
    }

    @Test
    public void signXMLSHA512WithRSA() {
        try {
            // Load Keystore

            String privateKeyPath = "src/main/resources/AAA010101AAA_FIEL.key";
            String password = "12345678a";
            String certificate = "src/main/resources/aaa010101aaa_FIEL.cer";
            KeyPairHolder keyHolder = new KeyFilesImpl(privateKeyPath, password, certificate);

            signer.setDigestMethodAlgorithm(DigestMethodAlgorithm.SHA512);
            signer.setSignatureMethodAlgorithm(SignatureMethodAlgorithm.RSA_SHA512);
            signer.setKeyPairProvider(keyHolder);

            signedXML = signer.sign(unsignedXML);

        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        }
        Assert.assertTrue(valid(signedXML));
    }

}
