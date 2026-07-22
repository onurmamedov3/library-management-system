package library_management_system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record MemberRequest(

		@NotBlank(message = "First name is required")
		@Size(max = 30, message = "First name must not exceed 30 characters")
		String firstName,

		@NotBlank(message = "Last name is required")
		@Size(max = 30, message = "Last name must not exceed 30 characters")
		String lastName,

		@NotBlank(message = "Email is required")
		@Email(message = "Email format is invalid")
		String email,

		@NotNull(message = "Date of birth is required")
		@Past(message = "Date of birth must be in the past")
		LocalDate dateOfBirth
) {}
