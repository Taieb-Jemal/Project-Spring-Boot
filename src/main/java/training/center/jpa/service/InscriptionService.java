package training.center.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.center.jpa.model.Inscription;
import training.center.jpa.model.Cours;
import training.center.jpa.model.Etudiant;
import training.center.jpa.repository.InscriptionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final CoursService coursService;

    public List<Inscription> getAllInscriptions() {
        return inscriptionRepository.findAll();
    }

    public Optional<Inscription> getInscriptionById(Long id) {
        return inscriptionRepository.findById(id);
    }

    public Inscription createInscription(Etudiant etudiant, Cours cours) {
        Inscription inscription = Inscription.builder()
                .etudiant(etudiant)
                .cours(cours)
                .dateInscription(LocalDateTime.now())
                .statut(Inscription.InscriptionStatus.ACTIVE)
                .build();
        
        Inscription savedInscription = inscriptionRepository.save(inscription);
        return savedInscription;
    }

    public void deleteInscription(Long id) {
        Optional<Inscription> inscription = inscriptionRepository.findById(id);
        if (inscription.isPresent()) {
            Inscription ins = inscription.get();
            inscriptionRepository.deleteById(id);
        }
    }

    public Optional<Inscription> getInscription(Etudiant etudiant, Cours cours) {
        return inscriptionRepository.findByEtudiantAndCours(etudiant, cours);
    }

    public List<Inscription> getInscriptionsByEtudiant(Etudiant etudiant) {
        return inscriptionRepository.findByEtudiant(etudiant);
    }

    public List<Inscription> getInscriptionsByCours(Cours cours) {
        return inscriptionRepository.findByCours(cours);
    }

    public List<Inscription> getActiveInscriptions() {
        return inscriptionRepository.findByStatut(Inscription.InscriptionStatus.ACTIVE);
    }

    public void cancelInscription(Long inscriptionId) {
        Optional<Inscription> inscription = inscriptionRepository.findById(inscriptionId);
        if (inscription.isPresent()) {
            Inscription ins = inscription.get();
            ins.setStatut(Inscription.InscriptionStatus.ANNULEE);
            inscriptionRepository.save(ins);
        }
    }
}
