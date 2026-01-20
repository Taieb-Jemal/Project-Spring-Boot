package training.center.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.center.jpa.model.Note;
import training.center.jpa.model.Cours;
import training.center.jpa.model.Etudiant;
import training.center.jpa.repository.NoteRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoteService {

    private final NoteRepository noteRepository;

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }

    public Note createNote(Etudiant etudiant, Cours cours, Double valeur, String remarques) {
        Note note = Note.builder()
                .etudiant(etudiant)
                .cours(cours)
                .valeur(valeur)
                .remarques(remarques)
                .dateAttribution(LocalDateTime.now())
                .build();
        return noteRepository.save(note);
    }

    public Note updateNote(Note note) {
        return noteRepository.save(note);
    }

    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    public Optional<Note> getNote(Etudiant etudiant, Cours cours) {
        return noteRepository.findByEtudiantAndCours(etudiant, cours);
    }

    public List<Note> getNotesByEtudiant(Etudiant etudiant) {
        return noteRepository.findByEtudiant(etudiant);
    }

    public List<Note> getNotesByCours(Cours cours) {
        return noteRepository.findByCours(cours);
    }

    public Double calculateMoyenne(Etudiant etudiant) {
        List<Note> notes = getNotesByEtudiant(etudiant);
        if (notes.isEmpty()) {
            return 0.0;
        }
        return notes.stream()
                .mapToDouble(Note::getValeur)
                .average()
                .orElse(0.0);
    }

    public Double calculateMoyenneCours(Cours cours) {
        List<Note> notes = getNotesByCours(cours);
        if (notes.isEmpty()) {
            return 0.0;
        }
        return notes.stream()
                .mapToDouble(Note::getValeur)
                .average()
                .orElse(0.0);
    }
}
