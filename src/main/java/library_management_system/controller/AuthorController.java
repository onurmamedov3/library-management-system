package library_management_system.controller;

import jakarta.validation.Valid;
import library_management_system.dto.request.AuthorRequest;
import library_management_system.dto.response.AuthorResponse;
import library_management_system.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

	private final AuthorService authorService;

	@GetMapping
	public ResponseEntity<Page<AuthorResponse>> getAllAuthors(Pageable pageable){
		return ResponseEntity.ok(authorService.getAll(pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<AuthorResponse> getById(@PathVariable UUID id){
		return ResponseEntity.ok(authorService.getById(id));
	}

	@PostMapping
	public ResponseEntity<AuthorResponse> createAuthor(@Valid @RequestBody AuthorRequest request){
		return ResponseEntity.status(HttpStatus.CREATED).body(authorService.create(request));
	}

	@PatchMapping("/{email}")
	public ResponseEntity<AuthorResponse> updateAuthor(@PathVariable String email,@Valid @RequestBody AuthorRequest request){
		return ResponseEntity.ok(authorService.update(email,request));
	}

	@DeleteMapping("/{email}")
	public ResponseEntity<Void> deleteAuthor(@PathVariable String email){
		authorService.delete(email);
		return ResponseEntity.noContent().build();
	}

}
