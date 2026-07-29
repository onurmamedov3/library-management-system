package library_management_system.service;

import library_management_system.dto.request.LoginRequest;
import library_management_system.dto.request.RegisterRequest;
import library_management_system.dto.response.TokenResponse;

public interface AuthService {

	TokenResponse login(LoginRequest request);

	void register(RegisterRequest request);

	void logout(String accessToken, String refreshToken);

	TokenResponse refresh(String refreshToken);
}
