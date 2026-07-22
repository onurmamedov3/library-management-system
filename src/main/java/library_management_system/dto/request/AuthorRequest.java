package library_management_system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record AuthorRequest(

		@NotBlank(message = "First name is required")
		@Size(max = 30, message = "First name must not exceed 30 characters")
		String firstName,

		@NotBlank(message = "Last name is required")
		@Size(max = 30, message = "Last name must not exceed 30 characters")
		String lastName,

		@NotBlank(message = "Email is required")
		@Email(message = "Email format is wrong")
		String email,

		List<UUID> bookIds
) {}
