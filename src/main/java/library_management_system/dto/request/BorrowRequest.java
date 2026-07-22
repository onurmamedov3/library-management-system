package library_management_system.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BorrowRequest(

		@NotNull(message = "Member ID is required")
		UUID memberId,

		@NotNull(message = "Book ID is required")
		UUID bookId
) {}
