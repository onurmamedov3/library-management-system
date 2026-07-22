package library_management_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Books", description = "Endpoints for managing books in the library")
public class BookController {

	private final BookService bookService;

	@GetMapping
	@Operation(summary = "Get all books", description = "Returns a paginated list of all active books")
	@ApiResponse(responseCode = "200", description = "Books retrieved successfully")
	public ResponseEntity<Page<BookResponse>> getAll(@PageableDefault(size = 20) Pageable pageable) {
		return ResponseEntity.ok(bookService.getAll(pageable));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get book by ID", description = "Returns a single book by its UUID")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Book found"),
		@ApiResponse(responseCode = "404", description = "Book not found")
	})
	public ResponseEntity<BookResponse> getById(@PathVariable UUID id) {
		return ResponseEntity.ok(bookService.getById(id));
	}

	@PostMapping
	@Operation(summary = "Create a new book", description = "Creates a new book with a unique ISBN")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Book created successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid request data"),
		@ApiResponse(responseCode = "409", description = "Book with this ISBN already exists")
	})
	public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(request));
	}

	@DeleteMapping("{isbn}")
	@Operation(summary = "Delete a book", description = "Soft-deletes a book by setting it as inactive")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Book deleted successfully"),
		@ApiResponse(responseCode = "404", description = "Book not found")
	})
	public ResponseEntity<Void> delete(@PathVariable String isbn) {
		bookService.delete(isbn);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PatchMapping("/{isbn}")
	@Operation(summary = "Update a book", description = "Updates an existing book identified by its ISBN")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Book updated successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid request data"),
		@ApiResponse(responseCode = "404", description = "Book not found")
	})
	public ResponseEntity<BookResponse> update(@PathVariable String isbn, @Valid @RequestBody BookRequest request) {
		return ResponseEntity.ok(bookService.update(isbn, request));
	}
}
