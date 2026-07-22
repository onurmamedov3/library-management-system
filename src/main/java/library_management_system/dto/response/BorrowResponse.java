package library_management_system.dto.response;

import library_management_system.entity.BorrowStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record BorrowResponse(
		UUID id,
		UUID memberId,
		String memberName,
		UUID bookId,
		String bookTitle,
		LocalDateTime borrowDate,
		LocalDateTime returnDate,
		BorrowStatus status
) {}
