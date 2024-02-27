package hr;

import hr.enums.UserRole;
import hr.entity.User;
import hr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class HrApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

	@Override
	public void run(String... args) {
		//checks if this username exists, add it if it doesn't
		if(userRepository.findByUsername("aleks1").isEmpty()){
			User user = new User();
			user.setPassword("aleks1");
			user.setUsername("aleks1");
			user.setCreatedAt(LocalDateTime.now());
			user.setCreatedBy("aleks1");
			user.setModifiedAt(LocalDateTime.now());
			user.setModifiedBy("aleks1");
			user.setRole(UserRole.MANAGER);
			user.setFirstName("Aleks");
			user.setLastName("Aleks");
			userRepository.save(user);
		}
	}
}
