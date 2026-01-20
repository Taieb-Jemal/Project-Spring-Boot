package training.center.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.center.jpa.model.Formateur;
import training.center.jpa.repository.FormateurRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FormateurService {

    private final FormateurRepository formateurRepository;

    public List<Formateur> getAllFormateurs() {
        return formateurRepository.findAll();
    }

    public Optional<Formateur> getFormateurById(Long id) {
        return formateurRepository.findById(id);
    }

    public Optional<Formateur> getFormateurByIdFormateur(String idFormateur) {
        return formateurRepository.findByIdFormateur(idFormateur);
    }

    public Optional<Formateur> getFormateurByEmail(String email) {
        return formateurRepository.findByEmail(email);
    }

    public Formateur createFormateur(Formateur formateur) {
        return formateurRepository.save(formateur);
    }

    public Formateur updateFormateur(Formateur formateur) {
        return formateurRepository.save(formateur);
    }

    public void deleteFormateur(Long id) {
        formateurRepository.deleteById(id);
    }

    public List<Formateur> getFormateursBySpecialite(String specialite) {
        return formateurRepository.findBySpecialite(specialite);
    }

    public List<Formateur> getActiveFormateurs() {
        return formateurRepository.findByActif(true);
    }
}
