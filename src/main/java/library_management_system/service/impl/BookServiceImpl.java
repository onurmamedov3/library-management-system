package library_management_system.service.impl;

import library_management_system.dto.request.BookRequest;
import library_management_system.dto.response.BookResponse;
import library_management_system.entity.Book;
import library_management_system.mapper.BookMapper;
import library_management_system.repository.BookRepository;
import library_management_system.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;

	private final BookMapper bookMapper;

	@Override
	public Page<BookResponse> getAll(Pageable pageable) {
		return bookRepository.findAll(pageable).map(bookMapper::toResponse);
	}

	@Override
	public BookResponse getById(UUID id) {
		Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

		return bookMapper.toResponse(book);
	}

	@Override
	public BookResponse create(BookRequest request) {

		if (bookRepository.existsByIsbn(request.isbn())) {
			throw new RuntimeException("Book with ISBN " + request.isbn() + " already exists");
		}

		Book book = bookMapper.toEntity(request);

		bookRepository.save(book);

		return bookMapper.toResponse(book);
	}

	@Override
	public BookResponse update(String isbn, BookRequest request) {
		Book book = bookRepository.findByIsbn(isbn)
				.orElseThrow(() -> new RuntimeException("Book with ISBN " + isbn + " does not exist"));

		bookMapper.updateEntity(request, book);

		bookRepository.save(book);

		return bookMapper.toResponse(book);
	}

	@Override
	public void delete(String isbn) {

	Book book = bookRepository.findByIsbn(isbn)
			.orElseThrow(() -> new RuntimeException("Book with ISBN " + isbn + " does not exist"));

	book.setActive(false);

	bookRepository.save(book);
	}
}
