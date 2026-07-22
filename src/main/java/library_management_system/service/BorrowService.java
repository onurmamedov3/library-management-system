package library_management_system.service;

import library_management_system.dto.request.BorrowRequest;
import library_management_system.dto.response.BorrowResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BorrowService {

	Page<BorrowResponse> getAll(Pageable pageable);

	BorrowResponse getById(UUID id);

	BorrowResponse borrowBook(BorrowRequest request);

	BorrowResponse returnBook(UUID borrowId);
}
