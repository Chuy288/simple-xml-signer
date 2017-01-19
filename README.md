# simple-xml-signer
This project tries to solve a basic problem: How to sign an XML file with a certain Signer (HSM, X509, etc).

The problem I've found using the java.xml.crypto.dsig package is when I tried to sign the XML file using a
SHA256 for message digest (see warning https://www.w3.org/TR/xmldsig-core1/#sec-MessageDigests),
but the only one that I could see was SHA1.

The intention is to create a library that should work like this:

xmlSigner.sign(outputSreamSignedXML, inputStreamUnsignedXML, signer, nodeToSignString <optional> );

The project is construted using:
- Maven 2
- JDK8 (I'll try to have this compiling fo JDK6+)
- Netbeans 8.2

This project is under Apache Licence 2.0, feel free to use it and contribute.
