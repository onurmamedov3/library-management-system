package library_management_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record BookRequest(
		@NotBlank(message = "Title is required")
		 String title,

		@NotBlank(message = "ISBN is required")
		 String isbn,

		@Size(max = 1000, message = "Description must not exceed 1000 characters")
		 String description,

		@NotNull(message = "Publication date is required")
		@PastOrPresent(message = "Publication date cannot be in the future")
		 LocalDate publicationDate,

		 List<UUID> authorIds
) {}
