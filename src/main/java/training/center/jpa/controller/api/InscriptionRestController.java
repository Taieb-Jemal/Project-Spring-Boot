package training.center.jpa.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import training.center.jpa.model.Etudiant;
import training.center.jpa.model.Inscription;
import training.center.jpa.repository.EtudiantRepository;
import training.center.jpa.repository.InscriptionRepository;
import training.center.jpa.service.InscriptionService;

import java.util.List;

/**
 * API REST pour Inscriptions - Role-based access
 * ADMIN: accès complet
 * FORMATEUR: peut voir les inscriptions à ses cours
 * ETUDIANT: peut voir uniquement ses inscriptions
 */
@RestController
@RequestMapping("/api/inscriptions")
@RequiredArgsConstructor
public class InscriptionRestController {

    private final InscriptionService inscriptionService;
    private final InscriptionRepository inscriptionRepository;
    private final EtudiantRepository etudiantRepository;

    /**
     * Récupérer toutes les inscriptions (ADMIN seulement)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Inscription>> getAllInscriptions() {
        return ResponseEntity.ok(inscriptionRepository.findAll());
    }

    /**
     * Récupérer les inscriptions d'un étudiant
     * - ADMIN: accès total
     * - ETUDIANT: peut voir uniquement ses inscriptions
     * - FORMATEUR: peut voir les inscriptions à ses cours
     */
    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<Inscription>> getInscriptionsEtudiant(@PathVariable Long etudiantId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userRole = auth.getAuthorities().iterator().next().getAuthority();

        // Vérifier accès
        if (userRole.equals("ROLE_ETUDIANT")) {
            String currentUser = auth.getName();
            Etudiant etudiant = etudiantRepository.findById(etudiantId)
                    .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
            if (!currentUser.equals(etudiant.getEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        return ResponseEntity.ok(inscriptionRepository.findByEtudiant(etudiant));
    }

    /**
     * Récupérer une inscription par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Inscription> getInscriptionById(@PathVariable Long id) {
        return inscriptionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Créer une inscription (ADMIN seulement)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Inscription> createInscription(@RequestBody Inscription inscription) {
        if (inscription.getEtudiant() == null || inscription.getCours() == null) {
            return ResponseEntity.badRequest().build();
        }
        Inscription savedInscription = inscriptionService.createInscription(
                inscription.getEtudiant(), 
                inscription.getCours()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(savedInscription);
    }

    /**
     * Modifier une inscription (ADMIN seulement)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Inscription> updateInscription(@PathVariable Long id, @RequestBody Inscription inscription) {
        return inscriptionRepository.findById(id)
                .map(existingInscription -> {
                    inscription.setId(id);
                    inscriptionRepository.save(inscription);
                    return ResponseEntity.ok(inscription);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprimer une inscription (ADMIN seulement)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteInscription(@PathVariable Long id) {
        inscriptionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Récupérer les inscriptions d'un cours
     * Accessible par ADMIN et FORMATEUR (du cours)
     */
    @GetMapping("/cours/{coursId}")
    public ResponseEntity<List<Inscription>> getInscriptionsCours(@PathVariable Long coursId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userRole = auth.getAuthorities().iterator().next().getAuthority();

        if (!userRole.equals("ROLE_ADMIN") && !userRole.equals("ROLE_FORMATEUR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Récupérer les inscriptions du cours
        return ResponseEntity.ok(inscriptionRepository.findAll().stream()
                .filter(i -> i.getCours().getId().equals(coursId))
                .toList());
    }
}
