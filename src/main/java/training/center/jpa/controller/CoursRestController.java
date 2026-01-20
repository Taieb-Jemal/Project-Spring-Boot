package training.center.jpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import training.center.jpa.model.Cours;
import training.center.jpa.service.CoursService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cours")
@RequiredArgsConstructor
public class CoursRestController {

    private final CoursService coursService;

    @GetMapping
    public ResponseEntity<List<Cours>> getAllCours() {
        return ResponseEntity.ok(coursService.getAllCours());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cours> getCoursById(@PathVariable Long id) {
        Optional<Cours> cours = coursService.getCoursById(id);
        return cours.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Cours> getCoursByCode(@PathVariable String code) {
        Optional<Cours> cours = coursService.getCoursByCode(code);
        return cours.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cours> createCours(@RequestBody Cours cours) {
        Cours saved = coursService.createCours(cours);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cours> updateCours(@PathVariable Long id, @RequestBody Cours cours) {
        Optional<Cours> existing = coursService.getCoursById(id);
        if (existing.isPresent()) {
            cours.setId(id);
            return ResponseEntity.ok(coursService.updateCours(cours));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCours(@PathVariable Long id) {
        coursService.deleteCours(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<Cours>> getActiveCours() {
        return ResponseEntity.ok(coursService.getActiveCours());
    }
}
