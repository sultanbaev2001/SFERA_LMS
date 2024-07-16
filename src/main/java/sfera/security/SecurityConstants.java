package sfera.security;

public class SecurityConstants {
    public static final String[] WHITE_LIST = {
            "/region/**",
            "/auth/forgot-password",
            "/auth/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/swagger-config",
            "/swagger-resources/**",
            "/webjars/**",
            "/ws/**",
    };
}
