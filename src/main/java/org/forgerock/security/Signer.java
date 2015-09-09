/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package org.forgerock.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;

/**
 *
 * @author lvaills
 */
public class Signer {

    public static void main(String[] args) throws Exception {
        Signature signer = Signature.getInstance("SHA256withRSA");
        // Init signer
        PrivateKey privateKey = readPrivateKeyFromKeyStore("src/main/resources/keystore.jks", "foo", "password");
        signer.initSign(privateKey);

        byte[] data = readData();
        signer.update(data);
        byte[] sign = signer.sign();
        writeSignature(sign);
    }

    private static PrivateKey readPrivateKeyFromKeyStore(String filename, String alias, String password) throws Exception {
        File file = new File(filename);
        if (!file.exists()) {
            throw new IllegalArgumentException(file.getAbsolutePath() + " does not exist.");
        }

        KeyStore store;
        try (FileInputStream fis = new FileInputStream(file)) {
            store = KeyStore.getInstance("jceks");
            store.load(fis, password.toCharArray());
        }

        KeyStore.ProtectionParameter params = new KeyStore.PasswordProtection(password.toCharArray());
        KeyStore.PrivateKeyEntry keyentry = (KeyStore.PrivateKeyEntry) store.getEntry(alias, params);
        if (keyentry != null) {
            return keyentry.getPrivateKey();
        } else {
            throw new IllegalArgumentException(alias + " : alias not found.");
        }
    }


    private static byte[] readData() throws IOException {
        byte[] result = new byte[1024];
        try (FileInputStream input = new FileInputStream("src/main/resources/data.txt")) {
            input.read(result);
        }
        return result;
    }

    private static void writeSignature(byte[] sign) throws IOException {
        try (OutputStream output = new FileOutputStream("src/main/resources/data.sign")) {
            output.write(sign);
        }
    }
}
