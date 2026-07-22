package library_management_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "members")
public class Member {

	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private LocalDate dateOfBirth;

	@Column(nullable = false)
	private boolean isActive;
}
