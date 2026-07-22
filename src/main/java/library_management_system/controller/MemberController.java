package library_management_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import library_management_system.dto.request.MemberRequest;
import library_management_system.dto.response.MemberResponse;
import library_management_system.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Members", description = "Endpoints for managing library members")
public class MemberController {

	private final MemberService memberService;

	@GetMapping
	@Operation(summary = "Get all members", description = "Returns a paginated list of all members")
	@ApiResponse(responseCode = "200", description = "Members retrieved successfully")
	public ResponseEntity<Page<MemberResponse>> getAll(@PageableDefault(size = 20) Pageable pageable) {
		return ResponseEntity.ok(memberService.getAll(pageable));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get member by ID", description = "Returns a single member by UUID")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Member found"),
		@ApiResponse(responseCode = "404", description = "Member not found")
	})
	public ResponseEntity<MemberResponse> getById(@PathVariable UUID id) {
		return ResponseEntity.ok(memberService.getById(id));
	}

	@PostMapping
	@Operation(summary = "Register a new member", description = "Creates a new library member with a unique email")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Member created successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid request data"),
		@ApiResponse(responseCode = "409", description = "Member with this email already exists")
	})
	public ResponseEntity<MemberResponse> create(@Valid @RequestBody MemberRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(memberService.create(request));
	}

	@PatchMapping("/{email}")
	@Operation(summary = "Update a member", description = "Updates an existing member identified by email")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Member updated successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid request data"),
		@ApiResponse(responseCode = "404", description = "Member not found")
	})
	public ResponseEntity<MemberResponse> update(@PathVariable String email, @Valid @RequestBody MemberRequest request) {
		return ResponseEntity.ok(memberService.update(email, request));
	}

	@DeleteMapping("/{email}")
	@Operation(summary = "Delete a member", description = "Soft-deletes a member by setting them as inactive")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Member deleted successfully"),
		@ApiResponse(responseCode = "404", description = "Member not found")
	})
	public ResponseEntity<Void> delete(@PathVariable String email) {
		memberService.delete(email);
		return ResponseEntity.noContent().build();
	}
}
