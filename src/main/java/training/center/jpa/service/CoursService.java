package training.center.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.center.jpa.model.Cours;
import training.center.jpa.model.Formateur;
import training.center.jpa.model.Session;
import training.center.jpa.repository.CoursRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CoursService {

    private final CoursRepository coursRepository;

    public List<Cours> getAllCours() {
        return coursRepository.findAll();
    }

    public Optional<Cours> getCoursById(Long id) {
        return coursRepository.findById(id);
    }

    public Optional<Cours> getCoursByCode(String code) {
        return coursRepository.findByCode(code);
    }

    public Cours createCours(Cours cours) {
        return coursRepository.save(cours);
    }

    public Cours updateCours(Cours cours) {
        return coursRepository.save(cours);
    }

    public void deleteCours(Long id) {
        coursRepository.deleteById(id);
    }

    public List<Cours> getCoursByFormateur(Formateur formateur) {
        return coursRepository.findByFormateur(formateur);
    }

    public List<Cours> getCoursBySession(Session session) {
        return coursRepository.findBySession(session);
    }

    public List<Cours> getActiveCours() {
        return coursRepository.findByActif(true);
    }

    public void addEtudiantToCours(Cours cours, training.center.jpa.model.Etudiant etudiant) {
        cours.getEtudiants().add(etudiant);
        coursRepository.save(cours);
    }

    public void removeEtudiantFromCours(Cours cours, training.center.jpa.model.Etudiant etudiant) {
        cours.getEtudiants().remove(etudiant);
        coursRepository.save(cours);
    }
}
