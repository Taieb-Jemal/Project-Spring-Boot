-- =====================================================
-- TRAINING CENTER MANAGEMENT DATABASE
-- =====================================================

-- Create Database
CREATE DATABASE IF NOT EXISTS trainingcenter;
USE trainingcenter;

-- =====================================================
-- TABLE: users
-- =====================================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'ETUDIANT', 'FORMATEUR') NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLE: specialites
-- =====================================================
CREATE TABLE IF NOT EXISTS specialites (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(1000),
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_nom (nom),
    INDEX idx_actif (actif)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLE: groupes
-- =====================================================
CREATE TABLE IF NOT EXISTS groupes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    nom VARCHAR(100) NOT NULL,
    description VARCHAR(1000),
    specialite_id BIGINT,
    niveau_etude INT NOT NULL,
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (specialite_id) REFERENCES specialites(id) ON DELETE SET NULL,
    INDEX idx_code (code),
    INDEX idx_specialite (specialite_id),
    INDEX idx_actif (actif)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLE: etudiants
-- =====================================================
CREATE TABLE IF NOT EXISTS etudiants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    matricule VARCHAR(50) NOT NULL UNIQUE,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telephone VARCHAR(20) NOT NULL,
    date_inscription TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    groupe_id BIGINT,
    specialite_id BIGINT,
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (groupe_id) REFERENCES groupes(id) ON DELETE SET NULL,
    FOREIGN KEY (specialite_id) REFERENCES specialites(id) ON DELETE SET NULL,
    INDEX idx_matricule (matricule),
    INDEX idx_email (email),
    INDEX idx_groupe (groupe_id),
    INDEX idx_specialite (specialite_id),
    INDEX idx_actif (actif)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLE: formateurs
-- =====================================================
CREATE TABLE IF NOT EXISTS formateurs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_formateur VARCHAR(50) NOT NULL UNIQUE,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telephone VARCHAR(20) NOT NULL,
    specialite VARCHAR(100) NOT NULL,
    biographie VARCHAR(1000),
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_id_formateur (id_formateur),
    INDEX idx_email (email),
    INDEX idx_specialite (specialite),
    INDEX idx_actif (actif)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLE: sessions
-- =====================================================
CREATE TABLE IF NOT EXISTS sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    libelle VARCHAR(100) NOT NULL,
    annee INT NOT NULL,
    semestre ENUM('S1', 'S2', 'S3', 'S4', 'S5', 'S6') NOT NULL,
    date_debut TIMESTAMP NOT NULL,
    date_fin TIMESTAMP NOT NULL,
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_code (code),
    INDEX idx_annee (annee),
    INDEX idx_semestre (semestre),
    INDEX idx_actif (actif)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLE: cours
-- =====================================================
CREATE TABLE IF NOT EXISTS cours (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    titre VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    credits INT NOT NULL,
    heures INT NOT NULL,
    formateur_id BIGINT NOT NULL,
    session_id BIGINT,
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (formateur_id) REFERENCES formateurs(id) ON DELETE RESTRICT,
    FOREIGN KEY (session_id) REFERENCES sessions(id) ON DELETE SET NULL,
    INDEX idx_code (code),
    INDEX idx_formateur (formateur_id),
    INDEX idx_session (session_id),
    INDEX idx_actif (actif)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLE: cours_groupes (Many-to-Many)
-- =====================================================
CREATE TABLE IF NOT EXISTS cours_groupes (
    cours_id BIGINT NOT NULL,
    groupe_id BIGINT NOT NULL,
    PRIMARY KEY (cours_id, groupe_id),
    FOREIGN KEY (cours_id) REFERENCES cours(id) ON DELETE CASCADE,
    FOREIGN KEY (groupe_id) REFERENCES groupes(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLE: cours_etudiants (Many-to-Many)
-- =====================================================
CREATE TABLE IF NOT EXISTS cours_etudiants (
    cours_id BIGINT NOT NULL,
    etudiant_id BIGINT NOT NULL,
    PRIMARY KEY (cours_id, etudiant_id),
    FOREIGN KEY (cours_id) REFERENCES cours(id) ON DELETE CASCADE,
    FOREIGN KEY (etudiant_id) REFERENCES etudiants(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLE: inscriptions
-- =====================================================
CREATE TABLE IF NOT EXISTS inscriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    etudiant_id BIGINT NOT NULL,
    cours_id BIGINT NOT NULL,
    date_inscription TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    statut ENUM('ACTIVE', 'ANNULEE', 'COMPLETEE') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_inscription (etudiant_id, cours_id),
    FOREIGN KEY (etudiant_id) REFERENCES etudiants(id) ON DELETE CASCADE,
    FOREIGN KEY (cours_id) REFERENCES cours(id) ON DELETE CASCADE,
    INDEX idx_etudiant (etudiant_id),
    INDEX idx_cours (cours_id),
    INDEX idx_statut (statut)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLE: notes
-- =====================================================
CREATE TABLE IF NOT EXISTS notes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    etudiant_id BIGINT NOT NULL,
    cours_id BIGINT NOT NULL,
    valeur DOUBLE NOT NULL,
    remarques VARCHAR(500),
    date_attribution TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_note (etudiant_id, cours_id),
    FOREIGN KEY (etudiant_id) REFERENCES etudiants(id) ON DELETE CASCADE,
    FOREIGN KEY (cours_id) REFERENCES cours(id) ON DELETE CASCADE,
    INDEX idx_etudiant (etudiant_id),
    INDEX idx_cours (cours_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLE: seances
-- =====================================================
CREATE TABLE IF NOT EXISTS seances (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cours_id BIGINT NOT NULL,
    date_debut TIMESTAMP NOT NULL,
    date_fin TIMESTAMP NOT NULL,
    salle VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cours_id) REFERENCES cours(id) ON DELETE CASCADE,
    INDEX idx_cours (cours_id),
    INDEX idx_salle (salle),
    INDEX idx_date (date_debut),
    INDEX idx_actif (actif)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- INSERT SAMPLE DATA
-- =====================================================

-- Insert Specialites
INSERT INTO specialites (nom, description, actif) VALUES
('Informatique', 'Ingénierie informatique et développement logiciel', TRUE),
('Réseaux', 'Administration réseaux et télécommunications', TRUE),
('Intelligence Artificielle', 'Data Science et Machine Learning', TRUE),
('Sécurité', 'Cybersécurité et sécurité informatique', TRUE);

-- Insert Groupes
INSERT INTO groupes (code, nom, specialite_id, niveau_etude, actif) VALUES
('ING-INF-1A', 'Ingénierie Informatique 1A', 1, 1, TRUE),
('ING-INF-2A', 'Ingénierie Informatique 2A', 1, 2, TRUE),
('RES-1A', 'Réseaux 1A', 2, 1, TRUE),
('AI-2A', 'IA et Data Science 2A', 3, 2, TRUE);

-- Insert Formateurs
INSERT INTO formateurs (id_formateur, nom, prenom, email, telephone, specialite, biographie, actif) VALUES
('FORM001', 'Zayani', 'Mohamed', 'zayani.m@iit.com', '+212612345678', 'Informatique', 'Professeur en Architecture logicielle', TRUE),
('FORM002', 'Belhadj', 'Ahmed', 'belhadj.a@iit.com', '+212612345679', 'Réseaux', 'Expert en Administration Réseaux', TRUE),
('FORM003', 'Bendjedia', 'Fatima', 'bendjedia.f@iit.com', '+212612345680', 'Intelligence Artificielle', 'Chercheur en Machine Learning', TRUE);

-- Insert Sessions
INSERT INTO sessions (code, libelle, annee, semestre, date_debut, date_fin, actif) VALUES
('S2025-S1', 'Session 2025 Semestre 1', 2025, 'S1', '2025-01-15', '2025-05-15', TRUE),
('S2025-S2', 'Session 2025 Semestre 2', 2025, 'S2', '2025-05-16', '2025-09-15', TRUE);

-- Insert Cours
INSERT INTO cours (code, titre, description, credits, heures, formateur_id, session_id, actif) VALUES
('INF101', 'Architecture des Systèmes', 'Principes et design des systèmes informatiques', 4, 60, 1, 1, TRUE),
('INF102', 'Programmation Avancée', 'Concepts avancés de programmation orientée objet', 4, 60, 1, 1, TRUE),
('RES101', 'Fondamentaux des Réseaux', 'Introduction aux réseaux informatiques', 3, 45, 2, 1, TRUE),
('AI101', 'Introduction au Machine Learning', 'Algorithmes et applications ML', 4, 60, 3, 2, TRUE);

-- Insert Etudiants
INSERT INTO etudiants (matricule, nom, prenom, email, telephone, groupe_id, specialite_id, actif) VALUES
('MAT001', 'Dupont', 'Jean', 'jean.dupont@student.iit.com', '+212612345681', 1, 1, TRUE),
('MAT002', 'Martin', 'Marie', 'marie.martin@student.iit.com', '+212612345682', 1, 1, TRUE),
('MAT003', 'Bernard', 'Pierre', 'pierre.bernard@student.iit.com', '+212612345683', 3, 2, TRUE),
('MAT004', 'Moreau', 'Luc', 'luc.moreau@student.iit.com', '+212612345684', 4, 3, TRUE);

-- Insert Inscriptions
INSERT INTO inscriptions (etudiant_id, cours_id, statut) VALUES
(1, 1, 'ACTIVE'),
(1, 2, 'ACTIVE'),
(2, 1, 'ACTIVE'),
(2, 2, 'ACTIVE'),
(3, 3, 'ACTIVE'),
(4, 4, 'ACTIVE');

-- Insert Notes
INSERT INTO notes (etudiant_id, cours_id, valeur, remarques) VALUES
(1, 1, 16.5, 'Excellent travail'),
(1, 2, 15.0, 'Bon'),
(2, 1, 14.5, 'Très bien'),
(2, 2, 13.0, 'Bien'),
(3, 3, 17.0, 'Excellent'),
(4, 4, 15.5, 'Très bien');

-- Insert Default Users
-- Password: admin123 (BCrypt encoded)
INSERT INTO users (username, password, email, first_name, last_name, role, active) VALUES
('admin', '$2a$10$slYQmyNdGzin7olVN3p5aOYtkJcf4rHEleme9ajd04d2fPcpm5MUS', 'admin@iit.com', 'Admin', 'System', 'ADMIN', TRUE),
('student01', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36MMrOXm', 'jean.dupont@student.iit.com', 'Jean', 'Dupont', 'ETUDIANT', TRUE),
('formateur01', '$2a$10$8R8dKL8F5O9xZ2L7F8d5e.aBc1234567890QWERTY', 'zayani.m@iit.com', 'Mohamed', 'Zayani', 'FORMATEUR', TRUE);

-- Create Indexes for performance
CREATE INDEX idx_seance_date ON seances(date_debut, date_fin);
CREATE INDEX idx_inscription_date ON inscriptions(date_inscription);

COMMIT;
