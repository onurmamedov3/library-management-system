package library_management_system.service.impl;

import library_management_system.dto.request.BorrowRequest;
import library_management_system.dto.response.BorrowResponse;
import library_management_system.entity.Book;
import library_management_system.entity.Borrow;
import library_management_system.entity.BorrowStatus;
import library_management_system.entity.Member;
import library_management_system.mapper.BorrowMapper;
import library_management_system.repository.BookRepository;
import library_management_system.repository.BorrowRepository;
import library_management_system.repository.MemberRepository;
import library_management_system.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {

	private final BorrowRepository borrowRepository;

	private final BookRepository bookRepository;

	private final MemberRepository memberRepository;

	private final BorrowMapper borrowMapper;

	@Override
	public Page<BorrowResponse> getAll(Pageable pageable) {
		return borrowRepository.findAll(pageable).map(borrowMapper::toResponse);
	}

	@Override
	public BorrowResponse getById(UUID id) {
		Borrow borrow = borrowRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Borrow record not found with id: " + id));

		return borrowMapper.toResponse(borrow);
	}

	@Override
	public BorrowResponse borrowBook(BorrowRequest request) {

		Member member = memberRepository.findById(request.memberId())
				.orElseThrow(() -> new RuntimeException("Member not found with id: " + request.memberId()));

		Book book = bookRepository.findById(request.bookId())
				.orElseThrow(() -> new RuntimeException("Book not found with id: " + request.bookId()));

		if (!book.isActive()) {
			throw new RuntimeException("Book is not available (inactive)");
		}

		if (borrowRepository.existsByBookIdAndStatus(book.getId(), BorrowStatus.ACTIVE)) {
			throw new RuntimeException("Book is already borrowed by another member");
		}

		Borrow borrow = new Borrow();
		borrow.setMember(member);
		borrow.setBook(book);
		borrow.setBorrowDate(LocalDateTime.now());
		borrow.setStatus(BorrowStatus.ACTIVE);

		borrowRepository.save(borrow);

		return borrowMapper.toResponse(borrow);
	}

	@Override
	public BorrowResponse returnBook(UUID borrowId) {
		Borrow borrow = borrowRepository.findById(borrowId)
				.orElseThrow(() -> new RuntimeException("Borrow record not found with id: " + borrowId));

		if (borrow.getStatus() != BorrowStatus.ACTIVE) {
			throw new RuntimeException("This book has already been returned");
		}

		borrow.setReturnDate(LocalDateTime.now());
		borrow.setStatus(BorrowStatus.RETURNED);

		borrowRepository.save(borrow);

		return borrowMapper.toResponse(borrow);
	}
}
