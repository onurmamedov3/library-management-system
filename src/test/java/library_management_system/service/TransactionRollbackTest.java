package library_management_system.service;

import library_management_system.dto.request.BookRequest;
import library_management_system.entity.Book;
import library_management_system.exception.ResourceAlreadyExistsException;
import library_management_system.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test that verifies @Transactional rollback behavior.
 */
@SpringBootTest
@ActiveProfiles("test")
class TransactionRollbackTest {

	@Autowired
	private BookService bookService;

	@Autowired
	private BookRepository bookRepository;

	@BeforeEach
	void setUp() {
		bookRepository.deleteAll();
	}

	@Test
	@DisplayName("should roll back book creation when duplicate ISBN throws exception")
	void shouldRollBackWhenDuplicateIsbnThrows() {
		BookRequest firstBook = new BookRequest(
				"Clean Code",
				"978-0132350884",
				"A handbook of agile software craftsmanship",
				LocalDate.of(2008, 8, 1),
				List.of()
		);
		bookService.create(firstBook);

		long countBefore = bookRepository.count();

		BookRequest duplicateIsbnBook = new BookRequest(
				"Duplicate Book",
				"978-0132350884",   // same ISBN → exception
				"This should not be persisted",
				LocalDate.of(2023, 1, 1),
				List.of()
		);

		assertThrows(ResourceAlreadyExistsException.class,
				() -> bookService.create(duplicateIsbnBook));

		long countAfter = bookRepository.count();
		assertEquals(countBefore, countAfter,
				"Book count should remain unchanged after a failed create — transaction must roll back");

		List<Book> allBooks = bookRepository.findAll();
		assertEquals(1, allBooks.size());
		assertEquals("Clean Code", allBooks.getFirst().getTitle());
	}

	@Test
	@DisplayName("should roll back update when ISBN collision throws exception")
	void shouldRollBackUpdateOnIsbnCollision() {
		bookService.create(new BookRequest(
				"Book A", "111-1111111111", "desc", LocalDate.of(2020, 1, 1), List.of()
		));
		bookService.create(new BookRequest(
				"Book B", "222-2222222222", "desc", LocalDate.of(2021, 1, 1), List.of()
		));

		BookRequest conflictingUpdate = new BookRequest(
				"Updated Title",
				"222-2222222222",   // collides with Book B
				"updated desc",
				LocalDate.of(2020, 1, 1),
				List.of()
		);

		assertThrows(ResourceAlreadyExistsException.class,
				() -> bookService.update("111-1111111111", conflictingUpdate));

		Book bookA = bookRepository.findByIsbn("111-1111111111").orElseThrow();
		assertEquals("Book A", bookA.getTitle(),
				"Book A's title should remain unchanged — transaction must roll back");
	}
}
