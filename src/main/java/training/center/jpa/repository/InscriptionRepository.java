package training.center.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import training.center.jpa.model.Inscription;
import training.center.jpa.model.Cours;
import training.center.jpa.model.Etudiant;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    Optional<Inscription> findByEtudiantAndCours(Etudiant etudiant, Cours cours);
    List<Inscription> findByEtudiant(Etudiant etudiant);
    List<Inscription> findByCours(Cours cours);
    List<Inscription> findByStatut(Inscription.InscriptionStatus statut);
}
