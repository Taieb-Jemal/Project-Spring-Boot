package training.center.jpa.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import training.center.jpa.model.Etudiant;
import training.center.jpa.model.Note;
import training.center.jpa.repository.EtudiantRepository;
import training.center.jpa.repository.NoteRepository;
import training.center.jpa.service.NoteService;

import java.util.List;

/**
 * API REST pour Notes - Role-based access
 * ADMIN: accès complet
 * FORMATEUR: accès lecture/création pour ses cours
 * ETUDIANT: accès lecture pour ses notes
 */
@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteRestController {

    private final NoteService noteService;
    private final NoteRepository noteRepository;
    private final EtudiantRepository etudiantRepository;

    /**
     * Récupérer toutes les notes (ADMIN seulement)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Note>> getAllNotes() {
        return ResponseEntity.ok(noteRepository.findAll());
    }

    /**
     * Récupérer les notes d'un étudiant
     * - ADMIN: accès total
     * - ETUDIANT: peut voir uniquement ses notes
     * - FORMATEUR: peut voir les notes des cours qu'il enseigne
     */
    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<Note>> getNotesEtudiant(@PathVariable Long etudiantId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userRole = auth.getAuthorities().iterator().next().getAuthority();

        // Vérifier l'accès
        if (userRole.equals("ROLE_ETUDIANT")) {
            String currentUser = auth.getName();
            Etudiant etudiant = etudiantRepository.findById(etudiantId)
                    .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
            if (!currentUser.equals(etudiant.getEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        // Récupérer les notes
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        return ResponseEntity.ok(noteRepository.findByEtudiant(etudiant));
    }

    /**
     * Récupérer une note par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        return noteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Créer une note (ADMIN et FORMATEUR)
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        if (note.getEtudiant() == null || note.getCours() == null) {
            return ResponseEntity.badRequest().build();
        }
        Note savedNote = noteService.createNote(note.getEtudiant(), note.getCours(), 
                                               note.getValeur(), note.getRemarques());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNote);
    }

    /**
     * Modifier une note (ADMIN et FORMATEUR)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note note) {
        return noteRepository.findById(id)
                .map(existingNote -> {
                    note.setId(id);
                    return ResponseEntity.ok(noteService.updateNote(note));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprimer une note (ADMIN seulement)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteNote(@PathVariable Long id) {
        noteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
