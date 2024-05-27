package ir.farzadafi.controller.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
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
            this.jwtDecoder.decode(tokenWithoutBearer);
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
}