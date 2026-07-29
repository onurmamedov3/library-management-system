package library_management_system.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import library_management_system.dto.request.LoginRequest;
import library_management_system.dto.request.RegisterRequest;
import library_management_system.dto.response.TokenResponse;
import library_management_system.exception.AuthException;
import library_management_system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request){
		return ResponseEntity.ok(authService.login(request));
	}

	@PostMapping("/register")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
		authService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request, @RequestBody Map<String, String> body){
		String accessToken = request.getHeader("Authorization").substring(7);
		String refreshToken = body.get("refreshToken");

		if(refreshToken == null || refreshToken.isBlank()){
			throw new AuthException("Refresh token is missing");
		}

		authService.logout(accessToken, refreshToken);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PostMapping("/refresh")
	public ResponseEntity<TokenResponse> refresh(@RequestBody Map<String, String> body) {
		return ResponseEntity.ok(authService.refresh(body.get("refreshToken")));
	}


}
