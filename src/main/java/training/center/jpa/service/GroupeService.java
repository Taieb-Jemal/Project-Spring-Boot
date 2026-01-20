package training.center.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.center.jpa.model.Groupe;
import training.center.jpa.model.Specialite;
import training.center.jpa.repository.GroupeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupeService {

    private final GroupeRepository groupeRepository;

    public List<Groupe> getAllGroupes() {
        return groupeRepository.findAll();
    }

    public Optional<Groupe> getGroupeById(Long id) {
        return groupeRepository.findById(id);
    }

    public Optional<Groupe> getGroupeByCode(String code) {
        return groupeRepository.findByCode(code);
    }

    public Groupe createGroupe(Groupe groupe) {
        return groupeRepository.save(groupe);
    }

    public Groupe updateGroupe(Groupe groupe) {
        return groupeRepository.save(groupe);
    }

    public void deleteGroupe(Long id) {
        groupeRepository.deleteById(id);
    }

    public List<Groupe> getGroupesBySpecialite(Specialite specialite) {
        return groupeRepository.findBySpecialite(specialite);
    }

    public List<Groupe> getActiveGroupes() {
        return groupeRepository.findByActif(true);
    }
}
