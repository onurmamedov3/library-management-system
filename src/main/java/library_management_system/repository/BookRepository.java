package library_management_system.repository;

import library_management_system.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {

	@EntityGraph(attributePaths = {"authors"})
	Page<Book> findAll(Pageable pageable);

	boolean existsByIsbn(String isbn);

	Optional<Book> findByIsbn(String isbn);
}
