package training.center.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import training.center.jpa.model.Note;
import training.center.jpa.model.Cours;
import training.center.jpa.model.Etudiant;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Optional<Note> findByEtudiantAndCours(Etudiant etudiant, Cours cours);
    List<Note> findByEtudiant(Etudiant etudiant);
    List<Note> findByCours(Cours cours);
}
