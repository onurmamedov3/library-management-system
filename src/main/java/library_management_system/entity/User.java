package library_management_system.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User implements UserDetails {

	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	private String username;

	private String email;

	private String password;

	@Enumerated(EnumType.STRING)
	private UserRole role;

	private String fullName;

	private boolean isActive;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
	}
}
