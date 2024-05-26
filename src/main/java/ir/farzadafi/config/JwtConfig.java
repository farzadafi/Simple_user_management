package ir.farzadafi.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Configuration
@Slf4j
public class JwtConfig {

    @Value("${jwt.public.key}")
    RSAPublicKey publicKey;

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.publicKey).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        PrivateKey privateKey = getPrivateKey();
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(privateKey).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    private PrivateKey getPrivateKey() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("private.key");
        String privateKeyString = convertInputStreamToString(inputStream);
        String pkcs8Pem = removeHeaderAndFooterKey(privateKeyString);
        byte[] pkcs8EncodedBytes = convertStringToByte(pkcs8Pem);
        return createPrivateKeyFromByte(pkcs8EncodedBytes);
    }

    private PrivateKey createPrivateKeyFromByte(byte[] pkcs8EncodedBytes) {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        }catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("algorithm or key invalid exception :-(");
            return null;
        }
    }

    private byte[] convertStringToByte(String pkcs8Pem) {
        return Base64.getDecoder().decode(pkcs8Pem);
    }

    private String removeHeaderAndFooterKey(String privateKeyString) {
        privateKeyString = privateKeyString.replace("-----BEGIN PRIVATE KEY-----", "");
        privateKeyString = privateKeyString.replace("-----END PRIVATE KEY-----", "");
        privateKeyString = privateKeyString.replaceAll("\\s+","");
        return privateKeyString;
    }

    private String convertInputStreamToString(InputStream inputStream) {
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (inputStream, StandardCharsets.UTF_8))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }catch (IOException | NullPointerException e) {
            log.error("Can't read private key file");
        }
        return textBuilder.toString();
    }
}