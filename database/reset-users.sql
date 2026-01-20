-- Reset users table and insert new users
-- Password: admin123 (BCrypt)

DELETE FROM users;

INSERT INTO users (username, password, email, first_name, last_name, role, active) VALUES
('mohamed', '$2a$10$slYQmyNdGzin7olVN3p5aOYtkJcf4rHEleme9ajd04d2fPcpm5MUS', 'mohamed@iit.com', 'Mohamed', 'Zayani', 'ADMIN', TRUE),
('saleh', '$2a$10$slYQmyNdGzin7olVN3p5aOYtkJcf4rHEleme9ajd04d2fPcpm5MUS', 'saleh@iit.com', 'Saleh', 'Student', 'ETUDIANT', TRUE),
('ali', '$2a$10$slYQmyNdGzin7olVN3p5aOYtkJcf4rHEleme9ajd04d2fPcpm5MUS', 'ali@iit.com', 'Ali', 'Formateur', 'FORMATEUR', TRUE);
