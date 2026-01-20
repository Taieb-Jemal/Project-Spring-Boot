package training.center.jpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import training.center.jpa.model.Formateur;
import training.center.jpa.service.FormateurService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/formateurs")
@RequiredArgsConstructor
public class FormateurRestController {

    private final FormateurService formateurService;

    @GetMapping
    public ResponseEntity<List<Formateur>> getAllFormateurs() {
        return ResponseEntity.ok(formateurService.getAllFormateurs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Formateur> getFormateurById(@PathVariable Long id) {
        Optional<Formateur> formateur = formateurService.getFormateurById(id);
        return formateur.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/id/{idFormateur}")
    public ResponseEntity<Formateur> getFormateurByIdFormateur(@PathVariable String idFormateur) {
        Optional<Formateur> formateur = formateurService.getFormateurByIdFormateur(idFormateur);
        return formateur.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Formateur> createFormateur(@RequestBody Formateur formateur) {
        Formateur saved = formateurService.createFormateur(formateur);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Formateur> updateFormateur(@PathVariable Long id, @RequestBody Formateur formateur) {
        Optional<Formateur> existing = formateurService.getFormateurById(id);
        if (existing.isPresent()) {
            formateur.setId(id);
            return ResponseEntity.ok(formateurService.updateFormateur(formateur));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormateur(@PathVariable Long id) {
        formateurService.deleteFormateur(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/specialite/{specialite}")
    public ResponseEntity<List<Formateur>> getFormateursBySpecialite(@PathVariable String specialite) {
        return ResponseEntity.ok(formateurService.getFormateursBySpecialite(specialite));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Formateur>> getActiveFormateurs() {
        return ResponseEntity.ok(formateurService.getActiveFormateurs());
    }
}
