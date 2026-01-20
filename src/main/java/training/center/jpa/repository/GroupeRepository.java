package training.center.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import training.center.jpa.model.Groupe;
import training.center.jpa.model.Specialite;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupeRepository extends JpaRepository<Groupe, Long> {
    Optional<Groupe> findByCode(String code);
    List<Groupe> findBySpecialite(Specialite specialite);
    List<Groupe> findByActif(Boolean actif);
}
