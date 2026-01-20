# CLASS DIAGRAM & UML DOCUMENTATION
## Training Center Management System

---

## TABLE OF CONTENTS
1. [Class Diagram (ASCII)](#class-diagram-ascii)
2. [Entity Classes](#entity-classes)
3. [Controller Classes](#controller-classes)
4. [Service Classes](#service-classes)
5. [Repository Classes](#repository-classes)
6. [Security Classes](#security-classes)
7. [Sequence Diagrams](#sequence-diagrams)

---

## CLASS DIAGRAM (ASCII)

### Complete Class Structure

```
┌────────────────────────────────────────────────────────────────────────────────────┐
│                    TRAINING CENTER MANAGEMENT SYSTEM                              │
│                            CLASS HIERARCHY & RELATIONSHIPS                        │
└────────────────────────────────────────────────────────────────────────────────────┘

═══════════════════════════════════════════════════════════════════════════════════════
                                 ENTITY CLASSES
═══════════════════════════════════════════════════════════════════════════════════════

┌─────────────────────────────────┐
│           User                  │
├─────────────────────────────────┤
│ - id: Long                      │
│ - username: String (UQ)         │
│ - password: String              │
│ - email: String                 │
│ - firstName: String             │
│ - lastName: String              │
│ - role: UserRole                │
│ - active: Boolean               │
│ - createdAt: LocalDateTime      │
│ - updatedAt: LocalDateTime      │
├─────────────────────────────────┤
│ + getId()                       │
│ + getUsername()                 │
│ + getPassword()                 │
│ + getRole()                     │
│ + isActive()                    │
└─────────────────────────────────┘
           △
           │ enum
           │
     ┌─────┴──────┐
     │  UserRole  │
     ├────────────┤
     │ ADMIN      │
     │ ETUDIANT   │
     │ FORMATEUR  │
     └────────────┘

┌──────────────────────────────────┐
│         Etudiant                 │
├──────────────────────────────────┤
│ - id: Long                       │
│ - matricule: String (UQ)         │
│ - nom: String                    │
│ - prenom: String                 │
│ - email: String (UQ)             │
│ - dateInscription: LocalDateTime │
│ - actif: Boolean                 │
│ - createdAt: LocalDateTime       │
│ - updatedAt: LocalDateTime       │
├──────────────────────────────────┤
│ - inscriptions: Set<Inscription> │
│ - notes: Set<Note>               │
├──────────────────────────────────┤
│ + getId()                        │
│ + getMatricule()                 │
│ + getNom()                       │
│ + getInscriptions()              │
│ + getNotes()                     │
│ + isActif()                      │
└──────────────────────────────────┘
           △
           │ 1..*
           │ uses
           │
     ┌─────┴──────────────┐
     │                    │
     │ ┌──────────────────┴───┐
     │ │ Inscription          │
     │ ├──────────────────────┤
     │ │ - id: Long           │
     │ │ - etudiant: Etudiant │ ◄─── @ManyToOne
     │ │ - cours: Cours       │ ◄─── @ManyToOne
     │ │ - dateInscription:   │
     │ │   LocalDateTime      │
     │ │ - statut: StatutEnum │
     │ │ - createdAt:         │
     │ │   LocalDateTime      │
     │ │ - updatedAt:         │
     │ │   LocalDateTime      │
     │ ├──────────────────────┤
     │ │ @UniqueConstraint    │
     │ │ (etudiant, cours)    │
     │ └──────────────────────┘
     │ ┌──────────────────────────────┐
     │ │ Note                         │
     │ ├──────────────────────────────┤
     │ │ - id: Long                   │
     │ │ - etudiant: Etudiant         │ ◄─── @ManyToOne
     │ │ - cours: Cours               │ ◄─── @ManyToOne
     │ │ - valeur: Double (0-20)      │
     │ │ - remarques: String          │
     │ │ - dateAttribution:           │
     │ │   LocalDateTime              │
     │ │ - createdAt: LocalDateTime   │
     │ │ - updatedAt: LocalDateTime   │
     │ ├──────────────────────────────┤
     │ │ @UniqueConstraint            │
     │ │ (etudiant, cours)            │
     │ └──────────────────────────────┘

┌──────────────────────────────────┐
│       Formateur                  │
├──────────────────────────────────┤
│ - id: Long                       │
│ - idFormateur: String (UQ)       │
│ - nom: String                    │
│ - prenom: String                 │
│ - email: String (UQ)             │
│ - specialite: String             │
│ - actif: Boolean                 │
│ - createdAt: LocalDateTime       │
│ - updatedAt: LocalDateTime       │
├──────────────────────────────────┤
│ - cours: Set<Cours>              │
├──────────────────────────────────┤
│ + getId()                        │
│ + getIdFormateur()               │
│ + getNom()                       │
│ + getSpecialite()                │
│ + getCours()                     │
│ + isActif()                      │
└──────────────────────────────────┘
           △
           │ 1..*
           │ teaches
           │
           │ ┌────────────────────────────┐
           │ │      Cours                 │
           │ ├────────────────────────────┤
           │ │ - id: Long                 │
           │ │ - code: String (UQ)        │
           │ │ - titre: String            │
           │ │ - description: String      │
           │ │ - credits: Integer         │
           │ │ - heures: Integer          │
           │ │ - formateur: Formateur     │ ◄─── @ManyToOne
           │ │ - actif: Boolean           │
           │ │ - createdAt: LocalDateTime │
           │ │ - updatedAt: LocalDateTime │
           │ ├────────────────────────────┤
           │ │ - inscriptions:            │
           │ │   Set<Inscription>         │
           │ │ - notes: Set<Note>         │
           │ ├────────────────────────────┤
           │ │ + getId()                  │
           │ │ + getCode()                │
           │ │ + getTitre()               │
           │ │ + getFormateur()           │
           │ │ + getInscriptions()        │
           │ │ + getNotes()               │
           │ └────────────────────────────┘


═══════════════════════════════════════════════════════════════════════════════════════
                              REPOSITORY PATTERN
═══════════════════════════════════════════════════════════════════════════════════════

┌─────────────────────────────────────────────┐
│   <<interface>>                             │
│    JpaRepository<T, ID>                     │
│  (from Spring Data JPA)                     │
└─────────────────────────────────────────────┘
           △       △       △       △       △
           │       │       │       │       │
    ┌──────┴───┐   │       │       │       │
    │           │   │       │       │       │
    │           │   │       │       │       │
    │ ┌─────────┴───┤       │       │       │
    │ │             │       │       │       │
    │ │ ┌───────────┴───┐   │       │       │
    │ │ │               │   │       │       │
    │ │ │ ┌─────────────┴───┤       │       │
    │ │ │ │                 │       │       │
    │ │ │ │ ┌───────────────┴───┐   │
    │ │ │ │ │                   │   │
    │ │ │ │ │ ┌─────────────────┴──┐
    │ │ │ │ │ │                    │

┌──────────────────────────┐
│ <<interface>>            │
│ UserRepository           │
├──────────────────────────┤
│ extends:                 │
│ JpaRepository<User, Long>│
├──────────────────────────┤
│ + findByUsername()       │
│ + existsByUsername()     │
└──────────────────────────┘

┌────────────────────────────────┐
│ <<interface>>                  │
│ EtudiantRepository             │
├────────────────────────────────┤
│ extends:                       │
│ JpaRepository<Etudiant, Long>  │
├────────────────────────────────┤
│ + findByMatricule()            │
│ + findByEmail()                │
│ + findByActifTrue()            │
└────────────────────────────────┘

┌────────────────────────────────────┐
│ <<interface>>                      │
│ FormateurRepository                │
├────────────────────────────────────┤
│ extends:                           │
│ JpaRepository<Formateur, Long>     │
├────────────────────────────────────┤
│ + findByIdFormateur()              │
│ + findByActifTrue()                │
└────────────────────────────────────┘

┌────────────────────────────────┐
│ <<interface>>                  │
│ CoursRepository                │
├────────────────────────────────┤
│ extends:                       │
│ JpaRepository<Cours, Long>     │
├────────────────────────────────┤
│ + findByCode()                 │
│ + findByFormateur()            │
│ + findByActifTrue()            │
└────────────────────────────────┘

┌────────────────────────────────────┐
│ <<interface>>                      │
│ InscriptionRepository              │
├────────────────────────────────────┤
│ extends:                           │
│ JpaRepository<Inscription, Long>   │
├────────────────────────────────────┤
│ + findByEtudiant()                 │
│ + findByCours()                    │
│ + findByStatut()                   │
└────────────────────────────────────┘

┌────────────────────────────────┐
│ <<interface>>                  │
│ NoteRepository                 │
├────────────────────────────────┤
│ extends:                       │
│ JpaRepository<Note, Long>      │
├────────────────────────────────┤
│ + findByEtudiant()             │
│ + findByCours()                │
│ + findByValeurGreaterThan()    │
└────────────────────────────────┘


═══════════════════════════════════════════════════════════════════════════════════════
                              SERVICE LAYER
═══════════════════════════════════════════════════════════════════════════════════════

┌───────────────────────────────────────────┐
│ EtudiantService                           │
├───────────────────────────────────────────┤
│ - etudiantRepository: EtudiantRepository  │
├───────────────────────────────────────────┤
│ + getAll()                                │
│ + getById(id: Long)                       │
│ + create(etudiant: Etudiant)              │
│ + update(etudiant: Etudiant)              │
│ + delete(id: Long)                        │
│ + findByMatricule(matricule: String)      │
│ + getAllActive()                          │
├───────────────────────────────────────────┤
│ Uses: EtudiantRepository (injected)       │
└───────────────────────────────────────────┘

┌───────────────────────────────────────────┐
│ FormateurService                          │
├───────────────────────────────────────────┤
│ - formateurRepository:                    │
│   FormateurRepository                     │
├───────────────────────────────────────────┤
│ + getAll()                                │
│ + getById(id: Long)                       │
│ + create(formateur: Formateur)            │
│ + update(formateur: Formateur)            │
│ + delete(id: Long)                        │
│ + findByIdFormateur(id: String)           │
│ + getAllActive()                          │
├───────────────────────────────────────────┤
│ Uses: FormateurRepository (injected)      │
└───────────────────────────────────────────┘

┌───────────────────────────────────────────┐
│ CoursService                              │
├───────────────────────────────────────────┤
│ - coursRepository: CoursRepository        │
├───────────────────────────────────────────┤
│ + getAll()                                │
│ + getById(id: Long)                       │
│ + create(cours: Cours)                    │
│ + update(cours: Cours)                    │
│ + delete(id: Long)                        │
│ + findByCode(code: String)                │
│ + getByFormateur(formateurId: Long)       │
├───────────────────────────────────────────┤
│ Uses: CoursRepository (injected)          │
└───────────────────────────────────────────┘

┌────────────────────────────────────────────┐
│ InscriptionService                         │
├────────────────────────────────────────────┤
│ - inscriptionRepository:                  │
│   InscriptionRepository                   │
│ - etudiantRepository: EtudiantRepository  │
│ - coursRepository: CoursRepository        │
├────────────────────────────────────────────┤
│ + getAll()                                 │
│ + getById(id: Long)                        │
│ + register(inscription: Inscription)      │
│ + update(inscription: Inscription)        │
│ + cancel(id: Long)                        │
│ + getByEtudiant(etudiantId: Long)         │
│ + getByCours(coursId: Long)               │
├────────────────────────────────────────────┤
│ Uses:                                      │
│  - InscriptionRepository (injected)       │
│  - EtudiantRepository (injected)          │
│  - CoursRepository (injected)             │
└────────────────────────────────────────────┘

┌────────────────────────────────────────────┐
│ NoteService                                │
├────────────────────────────────────────────┤
│ - noteRepository: NoteRepository           │
│ - etudiantRepository: EtudiantRepository   │
│ - coursRepository: CoursRepository         │
├────────────────────────────────────────────┤
│ + getAll()                                 │
│ + getById(id: Long)                        │
│ + assignGrade(note: Note)                  │
│ + updateGrade(note: Note)                  │
│ + deleteGrade(id: Long)                    │
│ + getByEtudiant(etudiantId: Long)          │
│ + getByCours(coursId: Long)                │
│ + getAverageGrade(etudiantId: Long)        │
├────────────────────────────────────────────┤
│ Uses:                                      │
│  - NoteRepository (injected)               │
│  - EtudiantRepository (injected)           │
│  - CoursRepository (injected)              │
└────────────────────────────────────────────┘


═══════════════════════════════════════════════════════════════════════════════════════
                              CONTROLLER LAYER
═══════════════════════════════════════════════════════════════════════════════════════

┌─────────────────────────────────────────────┐
│ LoginController                             │
├─────────────────────────────────────────────┤
│ + login(): String                           │
│   (Renders: login.html)                     │
├─────────────────────────────────────────────┤
│ Handles:                                    │
│  - GET /login                               │
└─────────────────────────────────────────────┘

┌─────────────────────────────────────────────┐
│ HomeController                              │
├─────────────────────────────────────────────┤
│ + home(): String                            │
│   (Renders: index.html)                     │
│ + dashboard(): String                       │
│   (Renders: index.html dashboard view)      │
├─────────────────────────────────────────────┤
│ Handles:                                    │
│  - GET / (root path)                        │
│  - GET /dashboard                           │
│  - GET /index                               │
└─────────────────────────────────────────────┘

┌──────────────────────────────────────────────┐
│ EtudiantController (MVC)                     │
├──────────────────────────────────────────────┤
│ - etudiantService: EtudiantService           │
├──────────────────────────────────────────────┤
│ + list(model: Model): String                 │
│   (Renders: etudiants/list.html)             │
│ + detail(id: Long, model: Model): String    │
│   (Renders: etudiants/detail.html)           │
│ + form(model: Model): String                 │
│   (Renders: etudiants/form.html)             │
│ + create(etudiant: Etudiant,                 │
│          result): String                     │
│ + update(id: Long, etudiant: Etudiant,      │
│          result): String                     │
│ + delete(id: Long): String                   │
├──────────────────────────────────────────────┤
│ Handles:                                     │
│  - GET /etudiants                            │
│  - GET /etudiants/{id}                       │
│  - GET /etudiants/form                       │
│  - POST /etudiants                           │
│  - PUT /etudiants/{id}                       │
│  - DELETE /etudiants/{id}                    │
└──────────────────────────────────────────────┘

┌───────────────────────────────────────────────┐
│ EtudiantRestController (REST API)             │
├───────────────────────────────────────────────┤
│ - etudiantService: EtudiantService            │
├───────────────────────────────────────────────┤
│ + getAll(): ResponseEntity<List<Etudiant>>   │
│ + getById(id: Long):                         │
│   ResponseEntity<Etudiant>                   │
│ + create(etudiant: Etudiant):                │
│   ResponseEntity<Etudiant>                   │
│ + update(id: Long, etudiant: Etudiant):     │
│   ResponseEntity<Etudiant>                   │
│ + delete(id: Long):                         │
│   ResponseEntity<Void>                       │
├───────────────────────────────────────────────┤
│ Handles:                                      │
│  - GET /api/etudiants                        │
│  - GET /api/etudiants/{id}                   │
│  - POST /api/etudiants                       │
│  - PUT /api/etudiants/{id}                   │
│  - DELETE /api/etudiants/{id}                │
└───────────────────────────────────────────────┘

┌──────────────────────────────────────────────┐
│ FormateurRestController (REST API)           │
├──────────────────────────────────────────────┤
│ - formateurService: FormateurService         │
├──────────────────────────────────────────────┤
│ + getAll(): ResponseEntity<...>              │
│ + getById(id: Long):                        │
│   ResponseEntity<Formateur>                  │
│ + create(formateur: Formateur):             │
│   ResponseEntity<Formateur>                  │
│ + update(id: Long, ...): ResponseEntity<...>│
│ + delete(id: Long): ResponseEntity<Void>    │
├──────────────────────────────────────────────┤
│ Handles:                                     │
│  - GET /api/formateurs                       │
│  - GET /api/formateurs/{id}                  │
│  - POST /api/formateurs                      │
│  - PUT /api/formateurs/{id}                  │
│  - DELETE /api/formateurs/{id}               │
└──────────────────────────────────────────────┘

┌──────────────────────────────────────────────┐
│ CoursRestController (REST API)               │
├──────────────────────────────────────────────┤
│ - coursService: CoursService                 │
├──────────────────────────────────────────────┤
│ + getAll(): ResponseEntity<...>              │
│ + getById(id: Long):                        │
│   ResponseEntity<Cours>                      │
│ + create(cours: Cours):                      │
│   ResponseEntity<Cours>                      │
│ + update(id: Long, ...): ResponseEntity<...>│
│ + delete(id: Long): ResponseEntity<Void>    │
├──────────────────────────────────────────────┤
│ Handles:                                     │
│  - GET /api/cours                            │
│  - GET /api/cours/{id}                       │
│  - POST /api/cours                           │
│  - PUT /api/cours/{id}                       │
│  - DELETE /api/cours/{id}                    │
└──────────────────────────────────────────────┘

┌────────────────────────────────────────────────┐
│ InscriptionRestController (REST API)          │
├────────────────────────────────────────────────┤
│ - inscriptionService:                        │
│   InscriptionService                         │
├────────────────────────────────────────────────┤
│ + getAll(): ResponseEntity<...>               │
│ + getById(id: Long):                         │
│   ResponseEntity<Inscription>                 │
│ + create(inscription: Inscription):          │
│   ResponseEntity<Inscription>                 │
│ + update(id: Long, ...): ResponseEntity<...> │
│ + delete(id: Long): ResponseEntity<Void>     │
├────────────────────────────────────────────────┤
│ Handles:                                       │
│  - GET /api/inscriptions                      │
│  - GET /api/inscriptions/{id}                 │
│  - POST /api/inscriptions                     │
│  - PUT /api/inscriptions/{id}                 │
│  - DELETE /api/inscriptions/{id}              │
└────────────────────────────────────────────────┘


═══════════════════════════════════════════════════════════════════════════════════════
                            SECURITY CONFIGURATION
═══════════════════════════════════════════════════════════════════════════════════════

┌─────────────────────────────────────┐
│  SecurityConfig                     │
├─────────────────────────────────────┤
│ - userDetailsService:               │
│   UserDetailsService                │
├─────────────────────────────────────┤
│ + passwordEncoder():                │
│   PasswordEncoder                   │
│ + authenticationProvider():         │
│   DaoAuthenticationProvider         │
│ + authenticationManager(...):       │
│   AuthenticationManager             │
│ + filterChain(http):                │
│   SecurityFilterChain               │
├─────────────────────────────────────┤
│ Dependencies:                       │
│ - CustomUserDetailsService          │
│   (injected)                        │
└─────────────────────────────────────┘

┌──────────────────────────────────────────┐
│ CustomUserDetailsService                 │
├──────────────────────────────────────────┤
│ - userRepository: UserRepository         │
├──────────────────────────────────────────┤
│ + loadUserByUsername(username: String):  │
│   UserDetails                            │
│   throws UsernameNotFoundException       │
├──────────────────────────────────────────┤
│ Process:                                 │
│  1. Load User from UserRepository        │
│  2. Create UserDetails with authorities  │
│  3. Map UserRole to ROLE_<ROLE>          │
│  4. Return for authentication            │
├──────────────────────────────────────────┤
│ Dependencies:                            │
│ - UserRepository (injected)              │
└──────────────────────────────────────────┘

┌──────────────────────────────────────┐
│ SecurityFilterChain                  │
├──────────────────────────────────────┤
│ Filters (in order):                  │
│  1. DisableEncodeUrlFilter           │
│  2. WebAsyncManagerIntegrationFilter │
│  3. SecurityContextHolderFilter      │
│  4. HeaderWriterFilter               │
│  5. LogoutFilter                     │
│  6. UsernamePasswordAuthenticationFilter
│  7. RequestCacheAwareFilter          │
│  8. SecurityContextHolderAwareRequest│
│     Filter                           │
│  9. AnonymousAuthenticationFilter    │
│  10. ExceptionTranslationFilter      │
│  11. AuthorizationFilter             │
├──────────────────────────────────────┤
│ Configuration:                       │
│  - CSRF: disabled                    │
│  - Form Login: enabled               │
│  - Logout: enabled                   │
│  - H2 Console: frames allowed        │
└──────────────────────────────────────┘
```

---

## ENTITY CLASSES

### User Entity
```java
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String password;  // BCrypt encoded
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;  // ADMIN, ETUDIANT, FORMATEUR
    
    @Column(nullable = false)
    private Boolean active;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    public enum UserRole {
        ADMIN, ETUDIANT, FORMATEUR
    }
}
```

### Etudiant Entity
```java
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Etudiant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String matricule;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private String prenom;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private LocalDateTime dateInscription;
    
    @Column(nullable = false)
    private Boolean actif = true;
    
    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL)
    private Set<Inscription> inscriptions;
    
    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL)
    private Set<Note> notes;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
```

### Formateur Entity
```java
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Formateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String idFormateur;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private String prenom;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String specialite;
    
    @Column(nullable = false)
    private Boolean actif = true;
    
    @OneToMany(mappedBy = "formateur", cascade = CascadeType.ALL)
    private Set<Cours> cours;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
```

### Cours Entity
```java
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String code;
    
    @Column(nullable = false)
    private String titre;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    private Integer credits;
    
    @Column(nullable = false)
    private Integer heures;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "formateur_id", nullable = false)
    private Formateur formateur;
    
    @Column(nullable = false)
    private Boolean actif = true;
    
    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL)
    private Set<Inscription> inscriptions;
    
    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL)
    private Set<Note> notes;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
```

### Inscription Entity
```java
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"etudiant_id", "cours_id"})
})
public class Inscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;
    
    @ManyToOne
    @JoinColumn(name = "cours_id", nullable = false)
    private Cours cours;
    
    @Column(nullable = false)
    private LocalDateTime dateInscription;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutEnum statut;
    
    public enum StatutEnum {
        ACTIVE, ANNULEE, COMPLETEE
    }
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    public void prePersist() {
        if (statut == null) {
            statut = StatutEnum.ACTIVE;
        }
        if (dateInscription == null) {
            dateInscription = LocalDateTime.now();
        }
    }
}
```

### Note Entity
```java
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"etudiant_id", "cours_id"})
})
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;
    
    @ManyToOne
    @JoinColumn(name = "cours_id", nullable = false)
    private Cours cours;
    
    @Column(nullable = false)
    private Double valeur;  // 0-20
    
    @Column(length = 500)
    private String remarques;
    
    @Column(nullable = false)
    private LocalDateTime dateAttribution;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    public void prePersist() {
        if (dateAttribution == null) {
            dateAttribution = LocalDateTime.now();
        }
    }
}
```

---

## SEQUENCE DIAGRAMS

### Authentication Sequence

```
┌──────────┐              ┌────────────┐       ┌──────────────┐       ┌──────────────┐
│  Browser │              │  Spring    │       │  Security    │       │   Database   │
│          │              │  MVC       │       │  Provider    │       │   (User)     │
└────┬─────┘              └─────┬──────┘       └──────┬───────┘       └──────┬───────┘
     │                          │                     │                      │
     │  POST /login             │                     │                      │
     │ (username, password)     │                     │                      │
     ├──────────────────────────>                     │                      │
     │                          │                     │                      │
     │                          │  authenticate()     │                      │
     │                          ├────────────────────>                       │
     │                          │                     │                      │
     │                          │  loadUserByUsername │                      │
     │                          │                     ├─────────────────────>
     │                          │                     │  SELECT * FROM users│
     │                          │                     │  WHERE username=?   │
     │                          │                     │<─────────────────────┤
     │                          │                     │  User object        │
     │                          │                     │                      │
     │                          │  matches(password)  │                      │
     │                          │ (BCrypt validation) │                      │
     │                          │                     │                      │
     │                          │  Authentication OK  │                      │
     │                          │<────────────────────┤                      │
     │                          │                     │                      │
     │   Session created        │                     │                      │
     │   JSESSIONID cookie      │                     │                      │
     │<──────────────────────────                     │                      │
     │   302 Redirect /         │                     │                      │
     │<──────────────────────────                     │                      │
     │                          │                     │                      │
     │  GET / (with JSESSIONID) │                     │                      │
     ├──────────────────────────>                     │                      │
     │                          │  SecurityContext    │                      │
     │                          │  restored from      │                      │
     │                          │  session            │                      │
     │                          │                     │                      │
     │   200 OK (Dashboard)     │                     │                      │
     │<──────────────────────────                     │                      │
     │                          │                     │                      │

```

### Create Student Sequence

```
┌──────────┐     ┌────────────┐      ┌──────────┐      ┌──────────┐      ┌───────────┐
│  User    │     │ Controller │      │ Service  │      │Repository│      │ Database  │
│          │     │            │      │          │      │          │      │           │
└────┬─────┘     └─────┬──────┘      └────┬─────┘      └────┬─────┘      └─────┬─────┘
     │                 │                   │                │                   │
     │ POST /api/etudiants               │                │                   │
     │ {matricule, nom, prenom, email}   │                │                   │
     ├────────────────────────────────────>               │                   │
     │                 │                   │                │                   │
     │                 │ Check @Valid      │                │                   │
     │                 │ Validation        │                │                   │
     │                 │                   │                │                   │
     │                 │ create(etudiant)  │                │                   │
     │                 ├───────────────────>               │                   │
     │                 │                   │                │                   │
     │                 │                   │ save(etudiant) │                   │
     │                 │                   ├───────────────>                   │
     │                 │                   │                │                   │
     │                 │                   │                │ INSERT INTO       │
     │                 │                   │                │ etudiants         │
     │                 │                   │                ├──────────────────>
     │                 │                   │                │                   │
     │                 │                   │                │ id = 1            │
     │                 │                   │                │ (generated)       │
     │                 │                   │                │<──────────────────┤
     │                 │                   │ etudiant       │                   │
     │                 │                   │<───────────────┤                   │
     │                 │ etudiant          │                │                   │
     │                 │<───────────────────                │                   │
     │                 │                   │                │                   │
     │ 201 Created     │                   │                │                   │
     │ {etudiant}      │                   │                │                   │
     │<────────────────┤                   │                │                   │
     │                 │                   │                │                   │

```

---

**Document Version:** 1.0  
**Last Updated:** January 20, 2026  
**Total Classes:** 11 (6 Entities + 5 Services + 7 Controllers + Security Config)
