package library_management_system.service;

import library_management_system.entity.User;

import java.util.Date;
import java.util.UUID;

public interface JwtService {

	String generateAccessToken(User user);

	String generateRefreshToken(User user, UUID familyId);

	String extractUsername(String token);

	String extractClaim(String token, String claimName);

	boolean isTokenValid(String token);

	Date extractExpiration(String token);

}
