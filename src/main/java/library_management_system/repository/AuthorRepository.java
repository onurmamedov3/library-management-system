package library_management_system.repository;

import library_management_system.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<Author, UUID> {

	boolean existsByEmail(String email);

	Optional<Author> findByEmail(String email);

}
