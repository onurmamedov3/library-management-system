package library_management_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book {

	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	@Column(nullable = false)
	private String title;

	@Column(unique = true, nullable = false)
	private String isbn;

	@Column(length = 1000)
	private String description;

	@Column(nullable = false)
	private boolean IsActive;

	@Column(nullable = false)
	private LocalDate publicationDate;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "book_authors",
		joinColumns = @JoinColumn(name = "book_id"),
		inverseJoinColumns = @JoinColumn(name = "author_id")
	)
	private List<Author> authors = new ArrayList<>();
}
