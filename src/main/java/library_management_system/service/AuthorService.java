package library_management_system.service;

import library_management_system.dto.request.AuthorRequest;
import library_management_system.dto.response.AuthorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AuthorService {

	Page<AuthorResponse> getAll(Pageable pageable);

	AuthorResponse getById(UUID uuid);

	AuthorResponse create(AuthorRequest request);

	AuthorResponse update(String email, AuthorRequest request);

	void delete(String email);

}
