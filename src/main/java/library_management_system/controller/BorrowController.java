package library_management_system.controller;

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
public class BorrowController {

	private final BorrowService borrowService;

	@GetMapping
	public ResponseEntity<Page<BorrowResponse>> getAll(@PageableDefault(size = 20) Pageable pageable) {
		return ResponseEntity.ok(borrowService.getAll(pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<BorrowResponse> getById(@PathVariable UUID id) {
		return ResponseEntity.ok(borrowService.getById(id));
	}

	@PostMapping
	public ResponseEntity<BorrowResponse> borrowBook(@Valid @RequestBody BorrowRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(borrowService.borrowBook(request));
	}

	@PatchMapping("/{id}/return")
	public ResponseEntity<BorrowResponse> returnBook(@PathVariable UUID id) {
		return ResponseEntity.ok(borrowService.returnBook(id));
	}
}
