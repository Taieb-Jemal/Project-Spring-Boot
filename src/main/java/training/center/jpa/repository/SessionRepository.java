package training.center.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import training.center.jpa.model.Session;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByCode(String code);
    List<Session> findByActif(Boolean actif);
    List<Session> findByAnnee(Integer annee);
    List<Session> findBySemestre(Session.Semestre semestre);
}
