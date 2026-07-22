package library_management_system.dto.response;

import java.util.UUID;

public record AuthorResponse(
		UUID id,
		String firstName,
		String lastName
) {}
