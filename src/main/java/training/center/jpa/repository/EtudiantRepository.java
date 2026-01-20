package training.center.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import training.center.jpa.model.Etudiant;
import training.center.jpa.model.Groupe;
import training.center.jpa.model.Specialite;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Optional<Etudiant> findByMatricule(String matricule);
    Optional<Etudiant> findByEmail(String email);
    List<Etudiant> findByGroupe(Groupe groupe);
    List<Etudiant> findBySpecialite(Specialite specialite);
    List<Etudiant> findByActif(Boolean actif);
}
