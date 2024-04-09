package andersen.configuration;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserPrincipal {
    private String email;
    private UserRole role;
}
