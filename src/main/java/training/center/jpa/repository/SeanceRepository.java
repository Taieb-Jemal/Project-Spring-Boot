package training.center.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import training.center.jpa.model.Seance;
import training.center.jpa.model.Cours;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long> {
    List<Seance> findByCours(Cours cours);
    List<Seance> findByDateDebutBetween(LocalDateTime debut, LocalDateTime fin);
    List<Seance> findBySalle(String salle);
    List<Seance> findByActif(Boolean actif);
}
