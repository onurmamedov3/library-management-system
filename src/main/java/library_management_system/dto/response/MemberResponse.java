package library_management_system.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record MemberResponse(
		UUID id,
		String firstName,
		String lastName,
		String email,
		LocalDate dateOfBirth
) {}
