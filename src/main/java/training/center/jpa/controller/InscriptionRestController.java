package training.center.jpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import training.center.jpa.model.Inscription;
import training.center.jpa.model.Cours;
import training.center.jpa.model.Etudiant;
import training.center.jpa.service.InscriptionService;
import training.center.jpa.service.CoursService;
import training.center.jpa.service.EtudiantService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inscriptions")
@RequiredArgsConstructor
public class InscriptionRestController {

    private final InscriptionService inscriptionService;
    private final CoursService coursService;
    private final EtudiantService etudiantService;

    @GetMapping
    public ResponseEntity<List<Inscription>> getAllInscriptions() {
        return ResponseEntity.ok(inscriptionService.getAllInscriptions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inscription> getInscriptionById(@PathVariable Long id) {
        Optional<Inscription> inscription = inscriptionService.getInscriptionById(id);
        return inscription.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/etudiant/{etudiantId}/cours/{coursId}")
    public ResponseEntity<Inscription> createInscription(
            @PathVariable Long etudiantId,
            @PathVariable Long coursId) {
        Optional<Etudiant> etudiant = etudiantService.getEtudiantById(etudiantId);
        Optional<Cours> cours = coursService.getCoursById(coursId);

        if (etudiant.isPresent() && cours.isPresent()) {
            Optional<Inscription> existing = inscriptionService.getInscription(etudiant.get(), cours.get());
            if (existing.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            Inscription inscription = inscriptionService.createInscription(etudiant.get(), cours.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(inscription);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInscription(@PathVariable Long id) {
        inscriptionService.deleteInscription(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelInscription(@PathVariable Long id) {
        inscriptionService.cancelInscription(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<Inscription>> getInscriptionsByEtudiant(@PathVariable Long etudiantId) {
        Optional<Etudiant> etudiant = etudiantService.getEtudiantById(etudiantId);
        if (etudiant.isPresent()) {
            return ResponseEntity.ok(inscriptionService.getInscriptionsByEtudiant(etudiant.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/cours/{coursId}")
    public ResponseEntity<List<Inscription>> getInscriptionsByCours(@PathVariable Long coursId) {
        Optional<Cours> cours = coursService.getCoursById(coursId);
        if (cours.isPresent()) {
            return ResponseEntity.ok(inscriptionService.getInscriptionsByCours(cours.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<Inscription>> getActiveInscriptions() {
        return ResponseEntity.ok(inscriptionService.getActiveInscriptions());
    }
}
