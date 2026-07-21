package library_management_system.dto.response;

import java.util.UUID;

public record BookResponse(
		UUID id,
		String title,
		String isbn
) {
}
