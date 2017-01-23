package org.hitzoft.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.hitzoft.xml.keypair.KeyPairHolder;
import org.hitzoft.xml.keypair.KeyPairHolderException;
import org.w3c.dom.NodeList;

/**
 *
 * @author jesus.espinoza
 */
public class XMLSigner {

    private String providerName = "XMLDSig";
    private DigestMethodAlgorithm digestMethodAlgorithm = DigestMethodAlgorithm.SHA1;
    private SignatureMethodAlgorithm signatureMethodAlgorithm = SignatureMethodAlgorithm.RSA_SHA1;
    private KeyPairHolder keyPairProvider;

    /**
     * Returns the provider name. Default: "XMLDSig"
     *
     * @return
     */
    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    /**
     * Returns digest method algorithm. Default: SHA1
     *
     * @return
     */
    public DigestMethodAlgorithm getDigestMethodAlgorithm() {
        return digestMethodAlgorithm;
    }

    public void setDigestMethodAlgorithm(DigestMethodAlgorithm digestMethodAlgorithm) {
        this.digestMethodAlgorithm = digestMethodAlgorithm;
    }

    /**
     * Returns signature method algorithm. Default: RSA with SHA1
     *
     * @return
     */
    public SignatureMethodAlgorithm getSignatureMethodAlgorithm() {
        return signatureMethodAlgorithm;
    }

    public void setSignatureMethodAlgorithm(SignatureMethodAlgorithm signatureMethodAlgorithm) {
        this.signatureMethodAlgorithm = signatureMethodAlgorithm;
    }

    public KeyPairHolder getKeyPairProvider() {
        return keyPairProvider;
    }

    public void setKeyPairProvider(KeyPairHolder keyPairProvider) {
        this.keyPairProvider = keyPairProvider;
    }

    public void sign(InputStream documentInputStream, OutputStream signedDocumentOutputStream) throws InvalidAlgorithmParameterException,
            KeyException, KeyPairHolderException, MarshalException, NoSuchAlgorithmException, NoSuchProviderException,
            TransformerException, XMLSignatureException, SAXException, ParserConfigurationException, IOException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document document = builder.parse(documentInputStream);

        sign(document, signedDocumentOutputStream);
    }

    public void sign(Document document, OutputStream signedDocumentOutputStream) throws InvalidAlgorithmParameterException,
            KeyException, KeyPairHolderException, MarshalException, NoSuchAlgorithmException, NoSuchProviderException,
            TransformerException, XMLSignatureException {

        document = sign(document);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.transform(new DOMSource(document), new StreamResult(signedDocumentOutputStream));
    }

    public Document sign(Document document) throws InvalidAlgorithmParameterException,
            KeyException, KeyPairHolderException, MarshalException, NoSuchAlgorithmException, NoSuchProviderException,
            TransformerException, XMLSignatureException {

        XMLSignatureFactory xmlSignatureFactory = XMLSignatureFactory.getInstance("DOM", providerName);

        DigestMethod digestMethod = xmlSignatureFactory.newDigestMethod(digestMethodAlgorithm.toString(), null);
        Transform transform = xmlSignatureFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null);
        Reference reference = xmlSignatureFactory.newReference("", digestMethod, Collections.singletonList(transform), null, null);
        SignatureMethod signatureMethod = xmlSignatureFactory.newSignatureMethod(signatureMethodAlgorithm.toString(), null);
        CanonicalizationMethod canonicalizationMethod = xmlSignatureFactory.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE, (C14NMethodParameterSpec) null);

        // Create the SignedInfo
        SignedInfo signedInfo = xmlSignatureFactory.newSignedInfo(canonicalizationMethod, signatureMethod, Collections.singletonList(reference));

        KeyPair keyPair = keyPairProvider.getKeyPair();

        KeyInfoFactory keyInfoFactory = xmlSignatureFactory.getKeyInfoFactory();
        KeyValue keyValue = keyInfoFactory.newKeyValue(keyPair.getPublic());

        // Create a KeyInfo and add the KeyValue to it
        KeyInfo KeyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(keyValue));
        DOMSignContext domSignContext = new DOMSignContext(keyPair.getPrivate(), document.getDocumentElement());

        // Create the XMLSignature, but don't sign it yet.
        XMLSignature signature = xmlSignatureFactory.newXMLSignature(signedInfo, KeyInfo);

        // Marshal, generate, and sign the enveloped signature.
        signature.sign(domSignContext);
        
//        TransformerFactory transformerFactory = TransformerFactory.newInstance();
//        Transformer transformer = transformerFactory.newTransformer();
//
//        transformer.transform(new DOMSource(document), new StreamResult(System.out));
        
        return document;
    }

    public boolean valid(Document signedDocument) throws XMLSignatureException,
            ParserConfigurationException, SAXException, IOException, NoSuchProviderException,
            MarshalException, KeyPairHolderException, XMLSignerValidationException {

        // Find Signature element.
        NodeList nl = signedDocument.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
        if (nl.getLength() == 0) {
            throw new XMLSignatureException("Cannot find Signature element");
        }

        // Create a DOMValidateContext and specify a KeySelector
        // and document context.
//        DOMValidateContext valContext = new DOMValidateContext(new X509KeySelector(), nl.item(0));
        KeyPair keyPair = keyPairProvider.getKeyPair();

        DOMValidateContext valContext = new DOMValidateContext(KeySelector.singletonKeySelector(keyPair.getPublic()), nl.item(0));

        // Unmarshal the XMLSignature.
        XMLSignatureFactory xmlSignatureFactory = XMLSignatureFactory.getInstance("DOM", providerName);
        XMLSignature signature = xmlSignatureFactory.unmarshalXMLSignature(valContext);

        // Validate the XMLSignature.
        boolean valid = signature.validate(valContext);

        // Check core validation status.
        if (valid) {
            return valid;
        } else {
            List<String> messages = new ArrayList<String>();
            messages.add("Signature failed core validation");
            boolean sv = signature.getSignatureValue().validate(valContext);
            messages.add("signature validation status: " + sv);
            if (sv == false) {
                // Check the validation status of each Reference.
                Iterator i = signature.getSignedInfo().getReferences().iterator();
                for (int j = 0; i.hasNext(); j++) {
                    boolean refValid = ((Reference) i.next()).validate(valContext);
                    messages.add("ref[" + j + "] validity status: " + refValid);
                }
            }
            throw new XMLSignerValidationException(messages);
        }
    }
}
