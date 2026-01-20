# Structure du Projet Training Center Management

```
TrainingCenterManagement/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── training/center/jpa/
│   │   │       ├── TrainingCenterManagementApplication.java      # Point d'entrée Spring Boot
│   │   │       │
│   │   │       ├── controller/                                    # Couche Contrôleur
│   │   │       │   ├── HomeController.java                       # Accueil
│   │   │       │   ├── EtudiantController.java                   # MVC Étudiants
│   │   │       │   ├── EtudiantRestController.java               # API REST Étudiants
│   │   │       │   ├── FormateurController.java                  # MVC Formateurs
│   │   │       │   ├── FormateurRestController.java              # API REST Formateurs
│   │   │       │   ├── CoursRestController.java                  # API REST Cours
│   │   │       │   └── InscriptionRestController.java            # API REST Inscriptions
│   │   │       │
│   │   │       ├── service/                                       # Couche Service (Métier)
│   │   │       │   ├── EtudiantService.java
│   │   │       │   ├── FormateurService.java
│   │   │       │   ├── CoursService.java
│   │   │       │   ├── InscriptionService.java
│   │   │       │   ├── NoteService.java
│   │   │       │   ├── SpecialiteService.java
│   │   │       │   ├── GroupeService.java
│   │   │       │   ├── SessionService.java
│   │   │       │   └── SeanceService.java
│   │   │       │
│   │   │       ├── repository/                                    # Couche Accès aux données
│   │   │       │   ├── UserRepository.java
│   │   │       │   ├── EtudiantRepository.java
│   │   │       │   ├── FormateurRepository.java
│   │   │       │   ├── CoursRepository.java
│   │   │       │   ├── InscriptionRepository.java
│   │   │       │   ├── NoteRepository.java
│   │   │       │   ├── SpecialiteRepository.java
│   │   │       │   ├── GroupeRepository.java
│   │   │       │   ├── SessionRepository.java
│   │   │       │   └── SeanceRepository.java
│   │   │       │
│   │   │       └── model/                                         # Entités JPA
│   │   │           ├── User.java
│   │   │           ├── Etudiant.java
│   │   │           ├── Formateur.java
│   │   │           ├── Cours.java
│   │   │           ├── Inscription.java
│   │   │           ├── Note.java
│   │   │           ├── Specialite.java
│   │   │           ├── Groupe.java
│   │   │           ├── Session.java
│   │   │           └── Seance.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties                # Configuration générale
│   │       ├── application-dev.properties            # Config profil DEV
│   │       ├── application-prod.properties           # Config profil PROD
│   │       ├── logback.xml                           # Configuration logs
│   │       │
│   │       ├── templates/                            # Templates Thymeleaf
│   │       │   ├── index.html                        # Page d'accueil
│   │       │   ├── about.html                        # À propos
│   │       │   ├── admin/
│   │       │   │   └── dashboard.html               # Dashboard administrateur
│   │       │   ├── etudiants/
│   │       │   │   ├── list.html                    # Liste étudiants
│   │       │   │   ├── form.html                    # Formulaire étudiant
│   │       │   │   └── detail.html                  # Détail étudiant
│   │       │   ├── formateurs/
│   │       │   │   ├── list.html                    # Liste formateurs
│   │       │   │   ├── form.html                    # Formulaire formateur
│   │       │   │   └── detail.html                  # Détail formateur
│   │       │   ├── cours/
│   │       │   │   ├── list.html
│   │       │   │   └── form.html
│   │       │   └── layout.html                       # Layout principal
│   │       │
│   │       └── static/
│   │           ├── css/
│   │           │   ├── bootstrap.min.css             # Bootstrap 5.3
│   │           │   └── style.css                     # Styles custom
│   │           ├── js/
│   │           │   └── app.js                        # Scripts JS
│   │           └── images/                           # Images/icônes
│   │
│   └── test/
│       └── java/
│           └── training/center/jpa/
│               └── TrainingCenterManagementApplicationTests.java
│
├── database/
│   └── init.sql                                      # Script création BD MySQL
│
├── pom.xml                                           # Configuration Maven
├── Dockerfile                                        # Image Docker
├── docker-compose.yml                                # Orchestration Docker
├── README.md                                         # Documentation complète
├── .gitignore                                        # Git ignore
├── STRUCTURE.md                                      # Ce fichier
│
└── target/                                           # Build Maven (généré)
    ├── classes/
    ├── test-classes/
    └── *.jar                                         # JAR exécutable
```

## 📊 Couches d'Architecture

### 1. **Couche Présentation (Controller)**
- Controllers MVC pour interface web Thymeleaf
- REST Controllers pour API
- Gestion des requêtes HTTP

### 2. **Couche Métier (Service)**
- Logique applicative
- Transactions
- Validations métier
- Calculs (moyennes, statistiques)

### 3. **Couche Persistance (Repository)**
- Spring Data JPA
- Requêtes de base de données
- Opérations CRUD

### 4. **Couche Modèle (Entity)**
- Entités JPA/Hibernate
- Mapping objet-relationnel
- Annotations JPA

## 🔄 Flux de données

```
Requête HTTP
    ↓
Controller/RestController
    ↓
Service (Logique métier)
    ↓
Repository (Accès BD)
    ↓
Database (MySQL/H2)
```

## 🗂️ Organisation par domaine

Chaque entité métier dispose de :
- **Model** : Classe JPA avec annotations
- **Repository** : Interface Spring Data JPA
- **Service** : Logique métier
- **Controller (MVC)** : Interface web
- **RestController** : API REST
- **Templates** : Pages Thymeleaf

## 📦 Dépendances principales

```xml
- Spring Boot 3.5.8
- Spring Data JPA
- Spring Web MVC
- Spring Security
- Thymeleaf
- MySQL Connector
- H2 Database
- Lombok
- Bootstrap 5.3
```

## 🚀 Points d'entrée

1. **Web MVC** : http://localhost:8080/
2. **API REST** : http://localhost:8080/api/*
3. **Dashboard** : http://localhost:8080/dashboard
4. **Console H2** : http://localhost:8080/h2-console (dev uniquement)

## 💾 Modèle de données - Relations

```
User (1) ←─→ (*) Etudiant
       ←─→ (*) Formateur

Etudiant (N) ←─→ (N) Groupe
         (N) ←─→ (N) Cours
         (N) ←─→ (N) Specialite
         (1) ←─→ (N) Inscription
         (1) ←─→ (N) Note

Formateur (1) ←─→ (N) Cours

Cours (N) ←─→ (N) Groupe
    (N) ←─→ (N) Etudiant
    (1) ←─→ (N) Seance
    (N) ←─→ (1) Session
    (1) ← (N) Inscription
    (1) ← (N) Note

Session (1) ←─→ (N) Cours

Specialite (1) ←─→ (N) Groupe
         (1) ←─→ (N) Etudiant
```

## 🔐 Sécurité

- Spring Security (authentification/autorisation)
- Rôles : ADMIN, ETUDIANT, FORMATEUR
- Protection CSRF
- Validation des inputs

## 📝 Conventions de nommage

- **Classes** : PascalCase (EtudiantController)
- **Méthodes** : camelCase (getAllEtudiants)
- **Variables** : camelCase (idEtudiant)
- **Constantes** : UPPER_CASE
- **URLs** : kebab-case (/api/etudiants)

---

**Dernière mise à jour** : Janvier 2026
