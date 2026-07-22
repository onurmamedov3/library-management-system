package library_management_system.service;

import library_management_system.dto.request.MemberRequest;
import library_management_system.dto.response.MemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface MemberService {

	Page<MemberResponse> getAll(Pageable pageable);

	MemberResponse getById(UUID id);

	MemberResponse create(MemberRequest request);

	MemberResponse update(String email, MemberRequest request);

	void delete(String email);
}
