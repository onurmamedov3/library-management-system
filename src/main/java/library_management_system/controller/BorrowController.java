package library_management_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import library_management_system.dto.request.BorrowRequest;
import library_management_system.dto.response.BorrowResponse;
import library_management_system.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/borrows")
@RequiredArgsConstructor
@Tag(name = "Borrows", description = "Endpoints for borrowing and returning books")
public class BorrowController {

	private final BorrowService borrowService;

	@GetMapping
	@Operation(summary = "Get all borrow records", description = "Returns a paginated list of all borrow records")
	@ApiResponse(responseCode = "200", description = "Borrow records retrieved successfully")
	public ResponseEntity<Page<BorrowResponse>> getAll(@PageableDefault(size = 20) Pageable pageable) {
		return ResponseEntity.ok(borrowService.getAll(pageable));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get borrow record by ID", description = "Returns a single borrow record by UUID")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Borrow record found"),
		@ApiResponse(responseCode = "404", description = "Borrow record not found")
	})
	public ResponseEntity<BorrowResponse> getById(@PathVariable UUID id) {
		return ResponseEntity.ok(borrowService.getById(id));
	}

	@PostMapping
	@Operation(summary = "Borrow a book", description = "Creates a new borrow record for a member and a book")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Book borrowed successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid request data"),
		@ApiResponse(responseCode = "404", description = "Member or book not found"),
		@ApiResponse(responseCode = "409", description = "Book is already borrowed")
	})
	public ResponseEntity<BorrowResponse> borrowBook(@Valid @RequestBody BorrowRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(borrowService.borrowBook(request));
	}

	@PatchMapping("/{id}/return")
	@Operation(summary = "Return a book", description = "Marks a borrow record as returned")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Book returned successfully"),
		@ApiResponse(responseCode = "404", description = "Borrow record not found"),
		@ApiResponse(responseCode = "409", description = "Book has already been returned")
	})
	public ResponseEntity<BorrowResponse> returnBook(@PathVariable UUID id) {
		return ResponseEntity.ok(borrowService.returnBook(id));
	}
}
