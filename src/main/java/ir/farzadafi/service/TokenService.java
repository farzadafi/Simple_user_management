package ir.farzadafi.service;

import ir.farzadafi.dto.JwtTokenRequest;
import ir.farzadafi.model.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    private final JwtEncoder encoder;
    private final UserService userService;

    public TokenService(JwtEncoder encoder, UserService userService) {
        this.encoder = encoder;
        this.userService = userService;
    }

    public String createToken(JwtTokenRequest request) {
        User user = userService.isLoginCheck(request.nationalCode(), request.password());
        Instant now = Instant.now();
        long expiry = 21600L;
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(user.getNationalCode())
                .claim("firstName", user.getFirstname())
                .claim("lastName", user.getLastname())
                .build();
        return this.encoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }
}