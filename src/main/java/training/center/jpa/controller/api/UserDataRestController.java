package training.center.jpa.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import training.center.jpa.model.Etudiant;
import training.center.jpa.model.Inscription;
import training.center.jpa.model.Note;
import training.center.jpa.repository.EtudiantRepository;
import training.center.jpa.repository.InscriptionRepository;
import training.center.jpa.repository.NoteRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API REST pour les données utilisateur-spécifiques
 * Retourne les données basées sur l'utilisateur authentifié
 */
@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class UserDataRestController {

    private final EtudiantRepository etudiantRepository;
    private final InscriptionRepository inscriptionRepository;
    private final NoteRepository noteRepository;

    /**
     * Récupérer les informations de l'utilisateur connecté
     * ETUDIANT: voir ses infos
     * FORMATEUR: voir ses infos
     * ADMIN: voir ses infos
     */
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        String role = auth.getAuthorities().iterator().next().getAuthority();

        Map<String, Object> profile = new HashMap<>();
        profile.put("email", email);
        profile.put("role", role);
        profile.put("authenticated", auth.isAuthenticated());

        return ResponseEntity.ok(profile);
    }

    /**
     * ETUDIANT: Récupérer ses cours et inscriptions
     */
    @GetMapping("/courses")
    @PreAuthorize("hasRole('ETUDIANT')")
    public ResponseEntity<List<Inscription>> getMyCoursesAsEtudiant() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Etudiant etudiant = etudiantRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        return ResponseEntity.ok(inscriptionRepository.findByEtudiant(etudiant));
    }

    /**
     * ETUDIANT: Récupérer ses notes
     */
    @GetMapping("/grades")
    @PreAuthorize("hasRole('ETUDIANT')")
    public ResponseEntity<List<Note>> getMyGradesAsEtudiant() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Etudiant etudiant = etudiantRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        return ResponseEntity.ok(noteRepository.findByEtudiant(etudiant));
    }

    /**
     * ETUDIANT: Récupérer le résumé académique (courses + grades)
     */
    @GetMapping("/academic-summary")
    @PreAuthorize("hasRole('ETUDIANT')")
    public ResponseEntity<Map<String, Object>> getAcademicSummary() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Etudiant etudiant = etudiantRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        List<Inscription> inscriptions = inscriptionRepository.findByEtudiant(etudiant);
        List<Note> notes = noteRepository.findByEtudiant(etudiant);

        // Calculer la moyenne
        double moyenne = notes.isEmpty() ? 0 :
                notes.stream()
                        .mapToDouble(Note::getValeur)
                        .average()
                        .orElse(0.0);

        Map<String, Object> summary = new HashMap<>();
        summary.put("etudiant", etudiant);
        summary.put("inscriptions", inscriptions);
        summary.put("notes", notes);
        summary.put("nombreCours", inscriptions.size());
        summary.put("nombreNotes", notes.size());
        summary.put("moyenneGenerale", String.format("%.2f", moyenne));

        return ResponseEntity.ok(summary);
    }
}
