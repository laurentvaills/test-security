This a Maven project to play with the javax.security package.

Generate a pair of keys, that is stored into a single entry into a keystore
```
keytool -genkeypair -alias "foo" -dname CN=a -keystore src/main/resources/keystore.jks -storepass password -keypass password -keyalg RSA -sigalg SHA256withRSA
```

Export the public-key as a certificate in PEM format (-rfc) otherwise it is binary (DER format ?)
```
keytool -exportcert -alias "foo" -file src/main/resources/key.cert -rfc -keystore src/main/resources/keystore.jks -storepass password -keypass password
```

Run the `Signer` program to create the signature file :
```
mvn exec:java -Dexec.mainClass="org.forgerock.security.Signer"
```
That will generate a file `src/main/resources/data.sign`, that is the signature of the file `src/main/resources/data.txt` with the previous generated key.

Run the `Verifier` program to verify the signature :
```
mvn exec:java -Dexec.mainClass="org.forgerock.security.Verifier"
```
You should see in the output the following line :
```
Signature : OK
```

If the file `src/main/resources/data.txt` is modified, then the output will be :
```
Signature : KO
```
