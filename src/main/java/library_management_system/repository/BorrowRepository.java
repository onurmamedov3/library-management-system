package library_management_system.repository;

import library_management_system.entity.Borrow;
import library_management_system.entity.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, UUID> {

	boolean existsByBookIdAndStatus(UUID bookId, BorrowStatus status);

	boolean existsByMemberIdAndBookIdAndStatus(UUID memberId, UUID bookId, BorrowStatus status);
}
