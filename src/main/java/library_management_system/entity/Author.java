package library_management_system.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "authors")
public class Author {

	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private boolean isActive;

	@ManyToMany(mappedBy = "authors")
	private Set<Book> books = new HashSet<>();

}
