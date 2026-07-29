package library_management_system.service.impl;

import library_management_system.dto.request.LoginRequest;
import library_management_system.dto.request.RegisterRequest;
import library_management_system.config.JwtProperties;
import library_management_system.dto.response.TokenResponse;
import library_management_system.entity.User;
import library_management_system.exception.AuthException;
import library_management_system.exception.ResourceAlreadyExistsException;
import library_management_system.exception.ResourceNotFoundException;
import library_management_system.repository.UserRepository;
import library_management_system.service.AuthService;
import library_management_system.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtProperties jwtProperties;
	private final StringRedisTemplate redisBlacklist;

	@Override
	public TokenResponse login(LoginRequest request){

		User user = userRepository.findByUsername(request.username())
						.orElseThrow(() ->  new ResourceNotFoundException("User with " + request.username() + " not found."));

		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));

		return createTokenPair(user, UUID.randomUUID());
	}

	@Override
	public void register(RegisterRequest request){

		if(userRepository.existsByUsername(request.username())){
			throw new ResourceAlreadyExistsException("User with " + request.username() + " already exists");
		}

		if(userRepository.existsByEmail(request.email())){
			throw new ResourceAlreadyExistsException("User with " + request.email() + " already exists");
		}

		User user = new User();
		user.setUsername(request.username());
		user.setEmail(request.email());
		user.setPassword(passwordEncoder.encode(request.password()));
		user.setFullName(request.fullName());
		user.setRole(request.role());
		user.setActive(true);
		userRepository.save(user);
	}

	@Override
	public void logout(String accessToken, String refreshToken) {
		String jti = jwtService.extractClaim(accessToken, "jti");

		long ttl = jwtService.extractExpiration(accessToken).getTime() - System.currentTimeMillis();

		if(ttl > 0){
			redisBlacklist.opsForValue().set("blacklist:" + jti,"revoked", ttl, TimeUnit.MILLISECONDS);
		}

		String familyId = jwtService.extractClaim(refreshToken, "fid");
		redisBlacklist.delete("refresh_family:" + familyId);
	}

	@Override
	public TokenResponse refresh(String refreshToken) {

		if(!jwtService.isTokenValid(refreshToken)) {
			throw new AuthException("Invalid token");
		}

		String typ = jwtService.extractClaim(refreshToken, "typ");
		if(!typ.equals("REFRESH")) {
			throw new AuthException("Invalid token type");
		}

		String familyId = jwtService.extractClaim(refreshToken, "fid");
		String jti = jwtService.extractClaim(refreshToken, "jti");

		if(!Boolean.TRUE.equals(redisBlacklist.hasKey("refresh_family:" + familyId))) {
			throw new AuthException("Session is expired. Please login");
		}
		String currentJti = (String) redisBlacklist.opsForHash().get("refresh_family:" + familyId, "currentJti");

		if(!jti.equals(currentJti)) {
			redisBlacklist.delete("refresh_family:" + familyId);
			throw new AuthException("Suspicious activity detected. Please login again");
		}

		String username = jwtService.extractUsername(refreshToken);

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User with " + username + " not found"));

		return createTokenPair(user, UUID.fromString(familyId));

	}


	private TokenResponse createTokenPair(User user, UUID familyId) {
		String accessToken = jwtService.generateAccessToken(user);
		String refreshToken = jwtService.generateRefreshToken(user, familyId);

		String refreshJti = jwtService.extractClaim(refreshToken, "jti");
		String redisKey = "refresh_family:" + familyId;

		redisBlacklist.opsForHash().put(redisKey, "currentJti", refreshJti);
		redisBlacklist.opsForHash().put(redisKey,"username", user.getUsername());
		redisBlacklist.expire(redisKey, jwtProperties.refreshTokenExpiration(), TimeUnit.MILLISECONDS);

		return new TokenResponse(accessToken,
				refreshToken,
				"Bearer",
				jwtService.extractExpiration(accessToken).getTime() / 1000);
	}
}
