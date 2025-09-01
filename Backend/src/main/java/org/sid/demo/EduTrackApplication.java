package org.sid.demo;

import org.sid.demo.entities.Project;
import org.sid.demo.entities.Role;
import org.sid.demo.entities.User;
import org.sid.demo.enums.UserRole;
import org.sid.demo.repositories.ProjectRepository;
import org.sid.demo.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class EduTrackApplication {

	public static void main(String[] args) {

		SpringApplication.run(EduTrackApplication.class, args);
	}

	@Bean
	CommandLineRunner start(UserRepository userRepository,
							ProjectRepository projectRepository) {
		return args -> {
			/*Stream.of("Student 1", "Student 2").forEach(name -> {
				User user = new User();
				user.setId(String.valueOf(UUID.randomUUID()));
				user.setName(name);
				user.setRole(UserRole.STUDENT);
				user.setEmail(name +etu.fh-aachen.de");
				userRepository.save(user);
			});*/
			if (projectRepository.count() == 0) {
				Project project1 = new Project(null,"EduTrack Platform", "An online education management system", LocalDateTime.now(), new ArrayList<>());
				Project project2 = new Project(null, "AI Research", "A project exploring AI in education", LocalDateTime.now(), new ArrayList<>());

				projectRepository.saveAll(List.of(project1, project2));
				System.out.println("=== TEST PROJECTS CREATED ===");

				// --- Create users and assign them to projects ---
				if (userRepository.count() == 0) {

					User admin = new User(UUID.randomUUID().toString(), "Admin User", "admin@school.com", Role.PROFESSOR, project1);
					User student1 = new User(UUID.randomUUID().toString(), "Alice Johnson", "hamza.iraamane0@gmail.com", Role.STUDENT, null);
					User student2 = new User(UUID.randomUUID().toString(), "Bob Smith", "amineyassir2001@gmail.com", Role.STUDENT, project2);
					User student3 = new User(UUID.randomUUID().toString(), "sssss", "yassinradiz10@gmail.com", Role.STUDENT, project2);

					userRepository.saveAll(List.of(admin, student1, student2, student3));
					System.out.println("=== TEST USERS CREATED ===");
				}



			}


			System.out.println("✅ Utilisateurs initiaux chargés.");
		};

		};
	}





