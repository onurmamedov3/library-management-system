package library_management_system.controller;

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
public class MemberController {

	private final MemberService memberService;

	@GetMapping
	public ResponseEntity<Page<MemberResponse>> getAll(@PageableDefault(size = 20) Pageable pageable) {
		return ResponseEntity.ok(memberService.getAll(pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<MemberResponse> getById(@PathVariable UUID id) {
		return ResponseEntity.ok(memberService.getById(id));
	}

	@PostMapping
	public ResponseEntity<MemberResponse> create(@Valid @RequestBody MemberRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(memberService.create(request));
	}

	@PatchMapping("/{email}")
	public ResponseEntity<MemberResponse> update(@PathVariable String email, @Valid @RequestBody MemberRequest request) {
		return ResponseEntity.ok(memberService.update(email, request));
	}

	@DeleteMapping("/{email}")
	public ResponseEntity<Void> delete(@PathVariable String email) {
		memberService.delete(email);
		return ResponseEntity.noContent().build();
	}
}
