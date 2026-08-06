package library_management_system.dto.request;

import java.time.LocalDate;

public record BookFilterRequest(
		String title,
		String authorName,
		String isbn,
		LocalDate publishedFrom,
		LocalDate publishedTo
) {
}
