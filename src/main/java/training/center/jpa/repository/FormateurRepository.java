package training.center.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import training.center.jpa.model.Formateur;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormateurRepository extends JpaRepository<Formateur, Long> {
    Optional<Formateur> findByIdFormateur(String idFormateur);
    Optional<Formateur> findByEmail(String email);
    List<Formateur> findBySpecialite(String specialite);
    List<Formateur> findByActif(Boolean actif);
}
