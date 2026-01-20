package training.center.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import training.center.jpa.model.Cours;
import training.center.jpa.model.Formateur;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {
    Optional<Cours> findByCode(String code);
    List<Cours> findByFormateur(Formateur formateur);
    List<Cours> findByActif(Boolean actif);
}
