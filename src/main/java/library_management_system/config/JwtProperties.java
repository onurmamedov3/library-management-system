package library_management_system.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "library-management-system.security.jwt")
public record JwtProperties(
		String secretKey,
		long accessTokenExpiration,
		long refreshTokenExpiration
) {
}
