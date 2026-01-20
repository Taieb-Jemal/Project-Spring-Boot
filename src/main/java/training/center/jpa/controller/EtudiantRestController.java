package training.center.jpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import training.center.jpa.model.Etudiant;
import training.center.jpa.service.EtudiantService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/etudiants")
@RequiredArgsConstructor
public class EtudiantRestController {

    private final EtudiantService etudiantService;

    @GetMapping
    public ResponseEntity<List<Etudiant>> getAllEtudiants() {
        return ResponseEntity.ok(etudiantService.getAllEtudiants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Etudiant> getEtudiantById(@PathVariable Long id) {
        Optional<Etudiant> etudiant = etudiantService.getEtudiantById(id);
        return etudiant.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<Etudiant> getEtudiantByMatricule(@PathVariable String matricule) {
        Optional<Etudiant> etudiant = etudiantService.getEtudiantByMatricule(matricule);
        return etudiant.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Etudiant> createEtudiant(@RequestBody Etudiant etudiant) {
        Etudiant saved = etudiantService.createEtudiant(etudiant);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Etudiant> updateEtudiant(@PathVariable Long id, @RequestBody Etudiant etudiant) {
        Optional<Etudiant> existing = etudiantService.getEtudiantById(id);
        if (existing.isPresent()) {
            etudiant.setId(id);
            return ResponseEntity.ok(etudiantService.updateEtudiant(etudiant));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtudiant(@PathVariable Long id) {
        etudiantService.deleteEtudiant(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<Etudiant>> getActiveEtudiants() {
        return ResponseEntity.ok(etudiantService.getActiveEtudiants());
    }
}
