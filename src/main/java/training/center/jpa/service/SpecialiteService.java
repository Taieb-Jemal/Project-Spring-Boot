package training.center.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.center.jpa.model.Specialite;
import training.center.jpa.repository.SpecialiteRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SpecialiteService {

    private final SpecialiteRepository specialiteRepository;

    public List<Specialite> getAllSpecialites() {
        return specialiteRepository.findAll();
    }

    public Optional<Specialite> getSpecialiteById(Long id) {
        return specialiteRepository.findById(id);
    }

    public Optional<Specialite> getSpecialiteByNom(String nom) {
        return specialiteRepository.findByNom(nom);
    }

    public Specialite createSpecialite(Specialite specialite) {
        return specialiteRepository.save(specialite);
    }

    public Specialite updateSpecialite(Specialite specialite) {
        return specialiteRepository.save(specialite);
    }

    public void deleteSpecialite(Long id) {
        specialiteRepository.deleteById(id);
    }

    public List<Specialite> getActiveSpecialites() {
        return specialiteRepository.findByActif(true);
    }
}
