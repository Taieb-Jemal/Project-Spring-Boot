package training.center.jpa.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import training.center.jpa.model.*;
import training.center.jpa.repository.*;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FormateurRepository formateurRepository;
    private final EtudiantRepository etudiantRepository;
    private final CoursRepository coursRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Initialize users if not already present
        if (userRepository.count() == 0) {
            initializeUsers();
        }
        
        // Initialize formateurs if not already present
        if (formateurRepository.count() == 0) {
            initializeFormateurs();
        }
        
        // Initialize etudiants if not already present
        if (etudiantRepository.count() == 0) {
            initializeEtudiants();
        }
        
        // Initialize cours if not already present
        if (coursRepository.count() == 0) {
            initializeCours();
        }
    }

    private void initializeUsers() {
        User admin = User.builder()
                .username("mohamed")
                .password(passwordEncoder.encode("admin123"))
                .email("mohamed@iit.com")
                .firstName("Mohamed")
                .lastName("Zayani")
                .role(User.UserRole.ADMIN)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User student = User.builder()
                .username("saleh")
                .password(passwordEncoder.encode("student123"))
                .email("saleh@iit.com")
                .firstName("Saleh")
                .lastName("Student")
                .role(User.UserRole.ETUDIANT)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User trainer = User.builder()
                .username("ali")
                .password(passwordEncoder.encode("trainer123"))
                .email("ali@iit.com")
                .firstName("Ali")
                .lastName("Formateur")
                .role(User.UserRole.FORMATEUR)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(admin);
        userRepository.save(student);
        userRepository.save(trainer);

        System.out.println("✓ Users initialized successfully");
    }

    private void initializeFormateurs() {
        Formateur f1 = Formateur.builder()
                .idFormateur("F001")
                .nom("Zayani")
                .prenom("Mohamed")
                .email("mohamed@iit.com")
                .specialite("Java Programming")
                .actif(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Formateur f2 = Formateur.builder()
                .idFormateur("F002")
                .nom("Ben Ali")
                .prenom("Karim")
                .email("karim@iit.com")
                .specialite("Web Development")
                .actif(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        formateurRepository.save(f1);
        formateurRepository.save(f2);

        System.out.println("✓ Formateurs initialized successfully");
    }

    private void initializeEtudiants() {
        Etudiant e1 = Etudiant.builder()
                .matricule("E001")
                .nom("Student")
                .prenom("Saleh")
                .email("saleh@iit.com")
                .dateInscription(LocalDateTime.now())
                .actif(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Etudiant e2 = Etudiant.builder()
                .matricule("E002")
                .nom("Ben Saad")
                .prenom("Ahmed")
                .email("ahmed@iit.com")
                .dateInscription(LocalDateTime.now())
                .actif(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Etudiant e3 = Etudiant.builder()
                .matricule("E003")
                .nom("Marzouki")
                .prenom("Fatima")
                .email("fatima@iit.com")
                .dateInscription(LocalDateTime.now())
                .actif(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        etudiantRepository.save(e1);
        etudiantRepository.save(e2);
        etudiantRepository.save(e3);

        System.out.println("✓ Etudiants initialized successfully");
    }

    private void initializeCours() {
        // Get formateurs from database
        Formateur f1 = formateurRepository.findByIdFormateur("F001").orElse(null);
        Formateur f2 = formateurRepository.findByIdFormateur("F002").orElse(null);

        if (f1 != null) {
            Cours c1 = Cours.builder()
                    .code("JAVA101")
                    .titre("Java Fundamentals")
                    .description("Introduction to Java programming")
                    .credits(3)
                    .heures(40)
                    .formateur(f1)
                    .actif(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            coursRepository.save(c1);
        }

        if (f2 != null) {
            Cours c2 = Cours.builder()
                    .code("WEB101")
                    .titre("Web Development Basics")
                    .description("HTML, CSS, JavaScript basics")
                    .credits(3)
                    .heures(45)
                    .formateur(f2)
                    .actif(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            coursRepository.save(c2);
        }

        if (f1 != null) {
            Cours c3 = Cours.builder()
                    .code("DB101")
                    .titre("Database Design")
                    .description("SQL and database design principles")
                    .credits(3)
                    .heures(35)
                    .formateur(f1)
                    .actif(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            coursRepository.save(c3);
        }

        System.out.println("✓ Cours initialized successfully");
    }
}
