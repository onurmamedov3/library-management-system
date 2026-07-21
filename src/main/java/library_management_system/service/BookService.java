package library_management_system.service;

import library_management_system.dto.request.BookRequest;
import library_management_system.dto.response.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BookService {
	Page<BookResponse> getAll(Pageable pageable);

	BookResponse getById(UUID id);

	BookResponse create(BookRequest request);

	BookResponse update(String isbn, BookRequest request);

	void delete(String isbn);
}
