package library_management_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import library_management_system.dto.request.AuthorRequest;
import library_management_system.dto.response.AuthorResponse;
import library_management_system.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
@Tag(name = "Authors", description = "Endpoints for managing authors")
public class AuthorController {

	private final AuthorService authorService;

	@GetMapping
	@Operation(summary = "Get all authors", description = "Returns a paginated list of all authors")
	@ApiResponse(responseCode = "200", description = "Authors retrieved successfully")
	public ResponseEntity<Page<AuthorResponse>> getAllAuthors(@PageableDefault(size = 20) Pageable pageable) {
		return ResponseEntity.ok(authorService.getAll(pageable));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get author by ID", description = "Returns a single author by UUID")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Author found"),
		@ApiResponse(responseCode = "404", description = "Author not found")
	})
	public ResponseEntity<AuthorResponse> getById(@PathVariable UUID id) {
		return ResponseEntity.ok(authorService.getById(id));
	}

	@PostMapping
	@Operation(summary = "Create a new author", description = "Creates a new author with a unique email")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Author created successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid request data"),
		@ApiResponse(responseCode = "409", description = "Author with this email already exists")
	})
	public ResponseEntity<AuthorResponse> createAuthor(@Valid @RequestBody AuthorRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(authorService.create(request));
	}

	@PatchMapping("/{email}")
	@Operation(summary = "Update an author", description = "Updates an existing author identified by email")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Author updated successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid request data"),
		@ApiResponse(responseCode = "404", description = "Author not found")
	})
	public ResponseEntity<AuthorResponse> updateAuthor(@PathVariable String email, @Valid @RequestBody AuthorRequest request) {
		return ResponseEntity.ok(authorService.update(email, request));
	}

	@DeleteMapping("/{email}")
	@Operation(summary = "Delete an author", description = "Soft-deletes an author by setting them as inactive")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Author deleted successfully"),
		@ApiResponse(responseCode = "404", description = "Author not found")
	})
	public ResponseEntity<Void> deleteAuthor(@PathVariable String email) {
		authorService.delete(email);
		return ResponseEntity.noContent().build();
	}
}
