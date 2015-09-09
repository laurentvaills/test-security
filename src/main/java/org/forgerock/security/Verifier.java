/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package org.forgerock.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.Certificate;

/**
 *
 * @author lvaills
 */
public class Verifier {

    public static void main(String[] args) throws Exception {
        Signature verifier = Signature.getInstance("SHA256withRSA");

        CertificateFactory fact = CertificateFactory.getInstance("X.509");
        FileInputStream is = new FileInputStream("src/main/resources/key.cert");
        Certificate cer = fact.generateCertificate(is);
        PublicKey key = cer.getPublicKey();
        verifier.initVerify(cer);

        byte[] data = readData();
        verifier.update(data);
        byte[] sign = readSignature();
        if (verifier.verify(sign)) {
            System.out.println("Signature OK");
        } else {
            System.out.println("Signature KO");
            System.exit(1);
        }

    }

    private static byte[] readData() throws IOException {
        byte[] result = new byte[1024];
        try (FileInputStream input = new FileInputStream("src/main/resources/data.txt")) {
            input.read(result);
        }
        return result;
    }

    private static byte[] readSignature() throws IOException {
        File file = new File("src/main/resources/data.sign");
        byte[] result = new byte[(int) file.length()];
        try (FileInputStream input = new FileInputStream(file)) {
            input.read(result);
        }
        return result;
    }


}
