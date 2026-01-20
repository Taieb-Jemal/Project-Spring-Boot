package training.center.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.center.jpa.model.Seance;
import training.center.jpa.model.Cours;
import training.center.jpa.repository.SeanceRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SeanceService {

    private final SeanceRepository seanceRepository;

    public List<Seance> getAllSeances() {
        return seanceRepository.findAll();
    }

    public Optional<Seance> getSeanceById(Long id) {
        return seanceRepository.findById(id);
    }

    public Seance createSeance(Seance seance) {
        return seanceRepository.save(seance);
    }

    public Seance updateSeance(Seance seance) {
        return seanceRepository.save(seance);
    }

    public void deleteSeance(Long id) {
        seanceRepository.deleteById(id);
    }

    public List<Seance> getSeancesByCours(Cours cours) {
        return seanceRepository.findByCours(cours);
    }

    public List<Seance> getSeancesByDateRange(LocalDateTime debut, LocalDateTime fin) {
        return seanceRepository.findByDateDebutBetween(debut, fin);
    }

    public List<Seance> getSeancesBySalle(String salle) {
        return seanceRepository.findBySalle(salle);
    }

    public List<Seance> getActiveSeances() {
        return seanceRepository.findByActif(true);
    }

    public boolean checkConflict(Seance seance) {
        List<Seance> seances = getActiveSeances();
        return seances.stream()
                .filter(s -> !s.getId().equals(seance.getId()))
                .filter(s -> s.getSalle().equals(seance.getSalle()))
                .anyMatch(s -> isTimeConflict(s, seance));
    }

    private boolean isTimeConflict(Seance s1, Seance s2) {
        return !s1.getDateFin().isBefore(s2.getDateDebut()) &&
               !s2.getDateFin().isBefore(s1.getDateDebut());
    }
}
