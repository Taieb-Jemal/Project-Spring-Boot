# 👨‍💻 Guide du Développeur

## 📋 Table des matières
1. [Architecture](#architecture)
2. [Convention de code](#convention-de-code)
3. [Ajouter une nouvelle fonctionnalité](#ajouter-une-nouvelle-fonctionnalité)
4. [Tester](#tester)
5. [Déboguer](#déboguer)
6. [Git workflow](#git-workflow)

---

## 🏗️ Architecture

### Pattern Layered Architecture

L'application suit l'architecture en couches :

```
┌─────────────────────────────────┐
│   Presentation Layer            │ (Controller)
│  (HTTP requests/responses)      │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│   Business Logic Layer          │ (Service)
│  (Business rules & validation)  │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│   Data Access Layer             │ (Repository)
│  (Database operations)          │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│   Database                      │ (MySQL/H2)
└─────────────────────────────────┘
```

### Flux d'une requête

```
1. HTTP Request arrive → Controller
2. Controller appelle Service
3. Service valide et exécute la logique
4. Service appelle Repository
5. Repository interagit avec la DB
6. Résultat remonte → HTTP Response
```

---

## 📝 Convention de code

### Packages
```
training.center.jpa.
├── controller/      # Controllers et RestControllers
├── service/         # Services métier
├── repository/      # Interfaces JPA Repository
└── model/           # Entités JPA
```

### Naming Conventions

| Type | Convention | Exemple |
|------|-----------|---------|
| **Classes** | PascalCase | `EtudiantService` |
| **Interfaces** | PascalCase | `EtudiantRepository` |
| **Méthodes** | camelCase | `getAllEtudiants()` |
| **Variables** | camelCase | `etudiantId` |
| **Constantes** | UPPER_SNAKE_CASE | `MAX_STUDENTS` |
| **URLs** | kebab-case | `/api/etudiants` |

### Annotations obligatoires

```java
// Service
@Service
@RequiredArgsConstructor
@Transactional
public class EtudiantService { }

// Controller
@RestController
@RequestMapping("/api/etudiants")
@RequiredArgsConstructor
public class EtudiantRestController { }

// Repository
@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> { }
```

---

## ➕ Ajouter une nouvelle fonctionnalité

### Cas d'usage : Ajouter une nouvelle entité "Evaluation"

#### Étape 1 : Créer l'entité

**Fichier** : `src/main/java/training/center/jpa/model/Evaluation.java`

```java
package training.center.jpa.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "cours_id", nullable = false)
    private Cours cours;

    @Column(nullable = false)
    private LocalDateTime dateEvaluation;

    @Column(nullable = false)
    private Boolean actif = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```

#### Étape 2 : Créer le Repository

**Fichier** : `src/main/java/training/center/jpa/repository/EvaluationRepository.java`

```java
package training.center.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import training.center.jpa.model.Evaluation;
import training.center.jpa.model.Cours;
import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByCours(Cours cours);
    List<Evaluation> findByActif(Boolean actif);
}
```

#### Étape 3 : Créer le Service

**Fichier** : `src/main/java/training/center/jpa/service/EvaluationService.java`

```java
package training.center.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.center.jpa.model.Evaluation;
import training.center.jpa.model.Cours;
import training.center.jpa.repository.EvaluationRepository;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;

    public List<Evaluation> getAllEvaluations() {
        return evaluationRepository.findAll();
    }

    public Optional<Evaluation> getEvaluationById(Long id) {
        return evaluationRepository.findById(id);
    }

    public Evaluation createEvaluation(Evaluation evaluation) {
        return evaluationRepository.save(evaluation);
    }

    public Evaluation updateEvaluation(Evaluation evaluation) {
        return evaluationRepository.save(evaluation);
    }

    public void deleteEvaluation(Long id) {
        evaluationRepository.deleteById(id);
    }

    public List<Evaluation> getEvaluationsByCours(Cours cours) {
        return evaluationRepository.findByCours(cours);
    }

    public List<Evaluation> getActiveEvaluations() {
        return evaluationRepository.findByActif(true);
    }
}
```

#### Étape 4 : Créer le RestController

**Fichier** : `src/main/java/training/center/jpa/controller/EvaluationRestController.java`

```java
package training.center.jpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import training.center.jpa.model.Evaluation;
import training.center.jpa.service.EvaluationService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/evaluations")
@RequiredArgsConstructor
public class EvaluationRestController {

    private final EvaluationService evaluationService;

    @GetMapping
    public ResponseEntity<List<Evaluation>> getAllEvaluations() {
        return ResponseEntity.ok(evaluationService.getAllEvaluations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evaluation> getEvaluationById(@PathVariable Long id) {
        Optional<Evaluation> evaluation = evaluationService.getEvaluationById(id);
        return evaluation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Evaluation> createEvaluation(@RequestBody Evaluation evaluation) {
        Evaluation saved = evaluationService.createEvaluation(evaluation);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evaluation> updateEvaluation(@PathVariable Long id, 
                                                      @RequestBody Evaluation evaluation) {
        Optional<Evaluation> existing = evaluationService.getEvaluationById(id);
        if (existing.isPresent()) {
            evaluation.setId(id);
            return ResponseEntity.ok(evaluationService.updateEvaluation(evaluation));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long id) {
        evaluationService.deleteEvaluation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<Evaluation>> getActiveEvaluations() {
        return ResponseEntity.ok(evaluationService.getActiveEvaluations());
    }
}
```

#### Étape 5 : Mettre à jour la base de données

Ajouter dans `database/init.sql` :

```sql
CREATE TABLE IF NOT EXISTS evaluations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    cours_id BIGINT NOT NULL,
    date_evaluation TIMESTAMP NOT NULL,
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cours_id) REFERENCES cours(id) ON DELETE CASCADE,
    INDEX idx_cours (cours_id),
    INDEX idx_actif (actif)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

---

## 🧪 Tester

### Tests Unitaires

**Fichier** : `src/test/java/training/center/jpa/service/EtudiantServiceTest.java`

```java
package training.center.jpa.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import training.center.jpa.model.Etudiant;
import training.center.jpa.repository.EtudiantRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EtudiantServiceTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantService etudiantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEtudiantById() {
        // Arrange
        Long id = 1L;
        Etudiant etudiant = Etudiant.builder()
                .id(id)
                .matricule("MAT001")
                .nom("Dupont")
                .prenom("Jean")
                .build();
        
        when(etudiantRepository.findById(id)).thenReturn(java.util.Optional.of(etudiant));

        // Act
        var result = etudiantService.getEtudiantById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Dupont", result.get().getNom());
        verify(etudiantRepository, times(1)).findById(id);
    }
}
```

### Exécuter les tests

```bash
# Tous les tests
mvn test

# Test spécifique
mvn test -Dtest=EtudiantServiceTest

# Avec couverture
mvn clean test jacoco:report
```

---

## 🔍 Déboguer

### Mode Debug Maven

```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
```

Puis connectez votre IDE au port 5005.

### Logs

- **Dev** : Console (stderr)
- **Prod** : `logs/application.log`

### Activer les logs détaillés

Ajouter dans `application-dev.properties` :

```properties
logging.level.training.center=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

---

## 🔄 Git Workflow

### Créer une nouvelle branche

```bash
git checkout -b feature/nom-feature
```

### Commits

```bash
# Ajouter les changements
git add .

# Commit avec un message descriptif
git commit -m "feat: ajouter nouvelle entité Evaluation"
```

### Push et Pull Request

```bash
git push origin feature/nom-feature
# Créer une PR sur GitHub/GitLab
```

### Format du message de commit

```
type(scope): description

feat: nouvelle fonctionnalité
fix: correction de bug
refactor: refactoring de code
test: ajout/modification de tests
docs: documentation
chore: tâches de maintenance
```

---

## 🔗 Ressources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Hibernate Documentation](https://hibernate.org/)
- [Thymeleaf](https://www.thymeleaf.org/)
- [Lombok](https://projectlombok.org/)

---

**Dernière mise à jour** : Janvier 2026
