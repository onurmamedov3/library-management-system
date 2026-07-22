package library_management_system.service;

import library_management_system.dto.request.BookRequest;
import library_management_system.dto.response.BookResponse;
import library_management_system.entity.Book;
import library_management_system.mapper.BookMapper;
import library_management_system.repository.BookRepository;
import library_management_system.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceUnitTest {

	@Mock
	private BookRepository bookRepository;

	@Mock
	private BookMapper bookMapper;

	@InjectMocks
	private BookServiceImpl bookService;

	private Book sampleBook;
	private BookResponse sampleResponse;
	private BookRequest sampleRequest;

	@BeforeEach
	void setUp() {
		sampleBook = new Book();
		sampleBook.setId(UUID.randomUUID());
		sampleBook.setTitle("Clean Code");
		sampleBook.setIsbn("978-0132350884");
		sampleBook.setDescription("A handbook of agile software craftsmanship");
		sampleBook.setPublicationDate(LocalDate.of(2008, 8, 1));
		sampleBook.setActive(true);

		sampleResponse = new BookResponse(
				sampleBook.getId(),
				sampleBook.getTitle(),
				sampleBook.getIsbn()
		);

		sampleRequest = new BookRequest(
				"Clean Code",
				"978-0132350884",
				"A handbook of agile software craftsmanship",
				LocalDate.of(2008, 8, 1),
				List.of()
		);
	}

	@Nested
	@DisplayName("getAll")
	class GetAllTests {

		@Test
		@DisplayName("should return a page of book responses")
		void shouldReturnPageOfBooks() {
			Pageable pageable = PageRequest.of(0, 20);
			Page<Book> bookPage = new PageImpl<>(List.of(sampleBook));

			when(bookRepository.findAll(pageable)).thenReturn(bookPage);
			when(bookMapper.toResponse(sampleBook)).thenReturn(sampleResponse);

			Page<BookResponse> result = bookService.getAll(pageable);

			assertNotNull(result);
			assertEquals(1, result.getTotalElements());
			assertEquals("Clean Code", result.getContent().get(0).title());

			verify(bookRepository).findAll(pageable);
			verify(bookMapper).toResponse(sampleBook);
		}

		@Test
		@DisplayName("should return empty page when no books exist")
		void shouldReturnEmptyPage() {
			Pageable pageable = PageRequest.of(0, 20);
			Page<Book> emptyPage = Page.empty();

			when(bookRepository.findAll(pageable)).thenReturn(emptyPage);

			Page<BookResponse> result = bookService.getAll(pageable);

			assertNotNull(result);
			assertEquals(0, result.getTotalElements());
		}
	}

	@Nested
	@DisplayName("getById")
	class GetByIdTests {

		@Test
		@DisplayName("should return book when found by ID")
		void shouldReturnBookWhenFound() {
			UUID id = sampleBook.getId();

			when(bookRepository.findById(id)).thenReturn(Optional.of(sampleBook));
			when(bookMapper.toResponse(sampleBook)).thenReturn(sampleResponse);

			BookResponse result = bookService.getById(id);

			assertNotNull(result);
			assertEquals("Clean Code", result.title());
			assertEquals("978-0132350884", result.isbn());

			verify(bookRepository).findById(id);
		}

		@Test
		@DisplayName("should throw exception when book not found by ID")
		void shouldThrowWhenNotFound() {
			UUID id = UUID.randomUUID();

			when(bookRepository.findById(id)).thenReturn(Optional.empty());

			RuntimeException exception = assertThrows(RuntimeException.class,
					() -> bookService.getById(id));

			assertTrue(exception.getMessage().contains("Book not found with id:"));

			verify(bookMapper, never()).toResponse(any());
		}
	}

	@Nested
	@DisplayName("create")
	class CreateTests {

		@Test
		@DisplayName("should create book successfully when ISBN is unique")
		void shouldCreateBookWhenIsbnUnique() {
			when(bookRepository.existsByIsbn(sampleRequest.isbn())).thenReturn(false);
			when(bookMapper.toEntity(sampleRequest)).thenReturn(sampleBook);
			when(bookRepository.save(sampleBook)).thenReturn(sampleBook);
			when(bookMapper.toResponse(sampleBook)).thenReturn(sampleResponse);

			BookResponse result = bookService.create(sampleRequest);

			assertNotNull(result);
			assertEquals("Clean Code", result.title());

			verify(bookRepository).existsByIsbn(sampleRequest.isbn());
			verify(bookRepository).save(sampleBook);
		}

		@Test
		@DisplayName("should throw exception when ISBN already exists")
		void shouldThrowWhenIsbnDuplicate() {
			when(bookRepository.existsByIsbn(sampleRequest.isbn())).thenReturn(true);

			RuntimeException exception = assertThrows(RuntimeException.class,
					() -> bookService.create(sampleRequest));

			assertTrue(exception.getMessage().contains("already exists"));

			verify(bookRepository, never()).save(any());
		}
	}

	@Nested
	@DisplayName("update")
	class UpdateTests {

		@Test
		@DisplayName("should update book when found by ISBN")
		void shouldUpdateBookWhenFound() {
			String isbn = "978-0132350884";

			when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(sampleBook));
			when(bookRepository.save(sampleBook)).thenReturn(sampleBook);
			when(bookMapper.toResponse(sampleBook)).thenReturn(sampleResponse);

			BookResponse result = bookService.update(isbn, sampleRequest);

			assertNotNull(result);

			verify(bookMapper).updateEntity(sampleRequest, sampleBook);
			verify(bookRepository).save(sampleBook);
		}

		@Test
		@DisplayName("should throw exception when book not found by ISBN for update")
		void shouldThrowWhenNotFoundForUpdate() {
			String isbn = "nonexistent-isbn";

			when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

			RuntimeException exception = assertThrows(RuntimeException.class,
					() -> bookService.update(isbn, sampleRequest));

			assertTrue(exception.getMessage().contains("does not exist"));

			verify(bookRepository, never()).save(any());
		}
	}

	@Nested
	@DisplayName("delete")
	class DeleteTests {

		@Test
		@DisplayName("should soft-delete book by setting isActive to false")
		void shouldSoftDeleteBook() {
			String isbn = "978-0132350884";
			sampleBook.setActive(true);

			when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(sampleBook));

			bookService.delete(isbn);

			assertFalse(sampleBook.isActive());

			verify(bookRepository).save(sampleBook);
		}

		@Test
		@DisplayName("should throw exception when book not found by ISBN for delete")
		void shouldThrowWhenNotFoundForDelete() {
			String isbn = "nonexistent-isbn";

			when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

			RuntimeException exception = assertThrows(RuntimeException.class,
					() -> bookService.delete(isbn));

			assertTrue(exception.getMessage().contains("does not exist"));

			verify(bookRepository, never()).save(any());
		}
	}
}
