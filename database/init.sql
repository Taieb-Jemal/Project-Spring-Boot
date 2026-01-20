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
-- TABLE: etudiants
-- =====================================================
CREATE TABLE IF NOT EXISTS etudiants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    matricule VARCHAR(50) NOT NULL UNIQUE,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    date_inscription TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_matricule (matricule),
    INDEX idx_email (email),
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
    specialite VARCHAR(100) NOT NULL,
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_id_formateur (id_formateur),
    INDEX idx_email (email),
    INDEX idx_specialite (specialite),
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
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (formateur_id) REFERENCES formateurs(id) ON DELETE RESTRICT,
    INDEX idx_code (code),
    INDEX idx_formateur (formateur_id),
    INDEX idx_actif (actif)
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
-- INSERT SAMPLE DATA
-- =====================================================

-- Insert Formateurs
INSERT INTO formateurs (id_formateur, nom, prenom, email, specialite, actif) VALUES
('FORM001', 'Zayani', 'Mohamed', 'zayani.m@iit.com', 'Informatique', TRUE),
('FORM002', 'Belhadj', 'Ahmed', 'belhadj.a@iit.com', 'Réseaux', TRUE),
('FORM003', 'Bendjedia', 'Fatima', 'bendjedia.f@iit.com', 'Intelligence Artificielle', TRUE);

-- Insert Cours
INSERT INTO cours (code, titre, description, credits, heures, formateur_id, actif) VALUES
('INF101', 'Architecture des Systèmes', 'Principes et design des systèmes informatiques', 4, 60, 1, TRUE),
('INF102', 'Programmation Avancée', 'Concepts avancés de programmation orientée objet', 4, 60, 1, TRUE),
('RES101', 'Fondamentaux des Réseaux', 'Introduction aux réseaux informatiques', 3, 45, 2, TRUE),
('AI101', 'Introduction au Machine Learning', 'Algorithmes et applications ML', 4, 60, 3, TRUE);

-- Insert Etudiants
INSERT INTO etudiants (matricule, nom, prenom, email, actif) VALUES
('MAT001', 'Dupont', 'Jean', 'jean.dupont@student.iit.com', TRUE),
('MAT002', 'Martin', 'Marie', 'marie.martin@student.iit.com', TRUE),
('MAT003', 'Bernard', 'Pierre', 'pierre.bernard@student.iit.com', TRUE),
('MAT004', 'Moreau', 'Luc', 'luc.moreau@student.iit.com', TRUE);

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

COMMIT;

-- =====================================================
-- INSERT SAMPLE DATA
-- =====================================================

-- Insert Formateurs
INSERT INTO formateurs (id_formateur, nom, prenom, email, specialite, actif) VALUES
('FORM001', 'Zayani', 'Mohamed', 'zayani.m@iit.com', 'Informatique', TRUE),
('FORM002', 'Belhadj', 'Ahmed', 'belhadj.a@iit.com', 'Réseaux', TRUE),
('FORM003', 'Bendjedia', 'Fatima', 'bendjedia.f@iit.com', 'Intelligence Artificielle', TRUE);

-- Insert Cours
INSERT INTO cours (code, titre, description, credits, heures, formateur_id, actif) VALUES
('INF101', 'Architecture des Systèmes', 'Principes et design des systèmes informatiques', 4, 60, 1, TRUE),
('INF102', 'Programmation Avancée', 'Concepts avancés de programmation orientée objet', 4, 60, 1, TRUE),
('RES101', 'Fondamentaux des Réseaux', 'Introduction aux réseaux informatiques', 3, 45, 2, TRUE),
('AI101', 'Introduction au Machine Learning', 'Algorithmes et applications ML', 4, 60, 3, TRUE);

-- Insert Etudiants
INSERT INTO etudiants (matricule, nom, prenom, email, actif) VALUES
('MAT001', 'Dupont', 'Jean', 'jean.dupont@student.iit.com', TRUE),
('MAT002', 'Martin', 'Marie', 'marie.martin@student.iit.com', TRUE),
('MAT003', 'Bernard', 'Pierre', 'pierre.bernard@student.iit.com', TRUE),
('MAT004', 'Moreau', 'Luc', 'luc.moreau@student.iit.com', TRUE);

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
('student01', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36MMrOXm', 'student01@iit.com', 'Jean', 'Dupont', 'ETUDIANT', TRUE),
('formateur01', '$2a$10$8R8dKL8F5O9xZ2L7F8d5e.aBc1234567890QWERTY', 'formateur01@iit.com', 'Mohamed', 'Zayani', 'FORMATEUR', TRUE);

COMMIT;
