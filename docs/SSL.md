---
title: docs/SSL.md
desc: SSL 
---

# SSL 관련

> [뒤로](../)

---

To enable SSL for your Spring Boot project using a .crt file in IntelliJ IDEA, you need to follow these steps:

### Convert the `.crt` file to a `.p12` or `.jks` keystore

Spring Boot supports PKCS12 and JKS keystores. If you only have the `.crt` file, you need to create a keystore file from it. You will also need the private key that matches the `.crt` file.

First, convert your `.crt` and private key into a `.p12` file. You can use the following `openssl` command:

```sh
openssl pkcs12 -export -in rootca.crt -inkey your_private_key.key -out keystore.p12 -name alias
```

Replace `rootca.crt`, `your_private_key.key`, `keystore.p12`, and alias with your actual filenames and alias.

The `.key` file is a private key associated with your `.crt` (certificate) file. 

The private key is crucial for setting up SSL/TLS as it works in conjunction with your certificate to encrypt and decrypt information securely. 

Without the private key, you won't be able to create the keystore required for SSL configuration in your Spring Boot application.

Here's what you can do if you don't have the private key:

### Check with the Certificate Provider

If you received the `.crt` file from a certificate authority (CA), check if they also provided the private key. Sometimes, the private key is generated and kept on the server from which the certificate request was made.

### Generate a Self-Signed Certificate

If you're setting up SSL for local development and testing, you can generate a self-signed certificate along with a private key using openssl. 

Here are the steps:

```sh
# Generate a private key
openssl genrsa -out private.key 2048

# Generate a self-signed certificate using the private key
openssl req -new -x509 -key private.key -out certificate.crt -days 365
```

### Create a Keystore from Self-Signed Certificate and Private Key:

Once you have both the private key (`private.key`) and the certificate (`rootca.crt`), create the keystore:

```sh
openssl pkcs12 -export -in rootca.crt -inkey private.key -out keystore.p12 -name alias
```

### Configure Spring Boot with the Generated Keystore:

- Place the `keystore.p12` in the `src/main/resources` directory of your Spring Boot project.
- Edit `application.properties` or `application.yml` to configure Spring Boot to use SSL, as mentioned previously:

```properties
server.port=8443
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=your_keystore_password
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=alias
```

### Run Your Application:

- Start your Spring Boot application in IntelliJ IDEA.
- Access it via https://localhost:8443.

By following these steps, you'll be able to generate a self-signed certificate and private key for use with your Spring Boot application. This approach is suitable for local development and testing purposes. For production, you should obtain a certificate from a trusted CA along with the corresponding private key.


