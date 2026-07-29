package library_management_system.config;

import library_management_system.entity.User;
import library_management_system.entity.UserRole;
import library_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) {
		if (userRepository.existsByUsername("admin")) {
			log.info("Admin user already exists, skipping seeding.");
			return;
		}

		User admin = new User();
		admin.setUsername("admin");
		admin.setEmail("admin@library.com");
		admin.setPassword(passwordEncoder.encode("Admin@1234"));
		admin.setFullName("System Administrator");
		admin.setRole(UserRole.ADMIN);
		admin.setActive(true);

		userRepository.save(admin);
		log.info("Admin user seeded successfully.");
	}
}
