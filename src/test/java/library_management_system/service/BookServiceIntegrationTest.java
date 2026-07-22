package library_management_system.service;

import library_management_system.dto.request.BookRequest;
import library_management_system.dto.response.BookResponse;
import library_management_system.entity.Book;
import library_management_system.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class BookServiceIntegrationTest {

	@Autowired
	private BookService bookService;

	@Autowired
	private BookRepository bookRepository;

	@BeforeEach
	void setUp() {
		bookRepository.deleteAll();
	}

	private BookRequest createBookRequest(String title, String isbn) {
		return new BookRequest(
				title,
				isbn,
				"A test book description",
				LocalDate.of(2020, 1, 1),
				List.of()
		);
	}

	@Nested
	@DisplayName("create and getAll")
	class CreateAndGetAllTests {

		@Test
		@DisplayName("should create a book and persist it in the database")
		void shouldCreateAndPersistBook() {
			BookRequest request = createBookRequest("Clean Code", "978-0132350884");

			BookResponse response = bookService.create(request);

			assertNotNull(response);
			assertNotNull(response.id());
			assertEquals("Clean Code", response.title());
			assertEquals("978-0132350884", response.isbn());

			// Verify it's actually in the database
			assertTrue(bookRepository.existsByIsbn("978-0132350884"));
		}

		@Test
		@DisplayName("should return all books from the database with pagination")
		void shouldReturnAllBooksWithPagination() {
			bookService.create(createBookRequest("Book One", "111-1111111111"));
			bookService.create(createBookRequest("Book Two", "222-2222222222"));
			bookService.create(createBookRequest("Book Three", "333-3333333333"));

			Page<BookResponse> page = bookService.getAll(PageRequest.of(0, 2));

			assertEquals(2, page.getContent().size());
			assertEquals(3, page.getTotalElements());
			assertEquals(2, page.getTotalPages());
		}

		@Test
		@DisplayName("should throw exception when creating book with duplicate ISBN")
		void shouldRejectDuplicateIsbn() {
			bookService.create(createBookRequest("Original Book", "978-0132350884"));

			RuntimeException exception = assertThrows(RuntimeException.class,
					() -> bookService.create(createBookRequest("Another Book", "978-0132350884")));

			assertTrue(exception.getMessage().contains("already exists"));
		}
	}

	@Nested
	@DisplayName("getById")
	class GetByIdTests {

		@Test
		@DisplayName("should retrieve a book by its UUID from the database")
		void shouldFindBookById() {
			BookResponse created = bookService.create(createBookRequest("Clean Code", "978-0132350884"));

			BookResponse found = bookService.getById(created.id());

			assertEquals(created.id(), found.id());
			assertEquals("Clean Code", found.title());
		}

		@Test
		@DisplayName("should throw exception when ID does not exist in database")
		void shouldThrowForNonExistentId() {
			UUID fakeId = UUID.randomUUID();

			RuntimeException exception = assertThrows(RuntimeException.class,
					() -> bookService.getById(fakeId));

			assertTrue(exception.getMessage().contains("Book not found with id:"));
		}
	}

	@Nested
	@DisplayName("update")
	class UpdateTests {

		@Test
		@DisplayName("should update book fields in the database")
		void shouldUpdateBookInDatabase() {
			bookService.create(createBookRequest("Old Title", "978-0132350884"));

			BookRequest updateRequest = createBookRequest("New Title", "978-0132350884");
			BookResponse updated = bookService.update("978-0132350884", updateRequest);

			assertEquals("New Title", updated.title());

			// Verify the update persisted
			Book fromDb = bookRepository.findByIsbn("978-0132350884").orElseThrow();
			assertEquals("New Title", fromDb.getTitle());
		}

		@Test
		@DisplayName("should throw exception when updating non-existent ISBN")
		void shouldThrowForNonExistentIsbn() {
			RuntimeException exception = assertThrows(RuntimeException.class,
					() -> bookService.update("fake-isbn", createBookRequest("Title", "fake-isbn")));

			assertTrue(exception.getMessage().contains("does not exist"));
		}
	}

	@Nested
	@DisplayName("delete (soft delete)")
	class DeleteTests {

		@Test
		@DisplayName("should soft-delete a book by setting isActive to false in database")
		void shouldSoftDeleteInDatabase() {
			bookService.create(createBookRequest("To Be Deleted", "978-0132350884"));

			bookService.delete("978-0132350884");

			Book fromDb = bookRepository.findByIsbn("978-0132350884").orElseThrow();
			assertFalse(fromDb.isActive());
		}

		@Test
		@DisplayName("should throw exception when deleting non-existent ISBN")
		void shouldThrowForNonExistentIsbn() {
			RuntimeException exception = assertThrows(RuntimeException.class,
					() -> bookService.delete("fake-isbn"));

			assertTrue(exception.getMessage().contains("does not exist"));
		}
	}
}
