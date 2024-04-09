package andersen.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class SecurityControllerAdvice {
    @ModelAttribute
    public UserPrincipal customPrincipal(Authentication auth) {
        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            return UserPrincipal.builder()
                    .email(jwt.getClaim("email"))
                    .build();
        }
        return null;
    }
}
