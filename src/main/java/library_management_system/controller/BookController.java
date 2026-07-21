package library_management_system.controller;

import jakarta.validation.Valid;
import library_management_system.dto.request.BookRequest;
import library_management_system.dto.response.BookResponse;
import library_management_system.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

	private final BookService bookService;

	@GetMapping
	public ResponseEntity<Page<BookResponse>> getAll(@PageableDefault(size = 20) Pageable pageable) {
		return ResponseEntity.ok(bookService.getAll(pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<BookResponse> getById(@PathVariable UUID id) {
		return ResponseEntity.ok(bookService.getById(id));
	}

	@PostMapping
	public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(request));
	}

	@DeleteMapping("{isbn}")
	public ResponseEntity<Void> delete(@PathVariable String isbn) {
		bookService.delete(isbn);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PatchMapping("/{isbn}")
	public ResponseEntity<BookResponse> update(@PathVariable String isbn, @Valid @RequestBody BookRequest request) {
		return ResponseEntity.ok(bookService.update(isbn, request));
	}
}
