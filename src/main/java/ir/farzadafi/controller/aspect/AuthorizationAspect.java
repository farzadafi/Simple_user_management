package ir.farzadafi.controller.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {

    private final JwtDecoder jwtDecoder;

    @Before("@annotation(ir.farzadafi.controller.aspect.CheckAuthorize)")
    public void checkAuthorization() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String tokenWithoutBearer = getJwtToken(request);
        try {
            Jwt decode = this.jwtDecoder.decode(tokenWithoutBearer);
            setContextSecurity(decode.getSubject(), decode.getClaim("password"));
        } catch (Exception e) {
            throw new AccessDeniedException("Please send valid Authorization Header!");
        }
    }

    private String getJwtToken(HttpServletRequest httpServletRequest) {
        String tokenWithBearer = httpServletRequest.getHeader("Authorization");
        if (tokenWithBearer == null || tokenWithBearer.isEmpty() || tokenWithBearer.length() < 10)
            throw new AccessDeniedException("Please send valid Authorization Header!");
        return tokenWithBearer.substring(7);
    }

    public void setContextSecurity(String nationalCode, String password) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(nationalCode, password);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}