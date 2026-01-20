package training.center.jpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import training.center.jpa.model.Etudiant;
import training.center.jpa.service.EtudiantService;


@Controller
@RequestMapping("/etudiants")
@RequiredArgsConstructor
public class EtudiantController {

    private final EtudiantService etudiantService;

    @GetMapping
    public String listEtudiants(Model model) {
        model.addAttribute("etudiants", etudiantService.getAllEtudiants());
        return "etudiants/list";
    }

    @GetMapping("/{id}")
    public String detailEtudiant(@PathVariable Long id, Model model) {
        etudiantService.getEtudiantById(id).ifPresent(e -> 
            model.addAttribute("etudiant", e)
        );
        return "etudiants/detail";
    }

    @GetMapping("/new")
    public String newEtudiant(Model model) {
        model.addAttribute("etudiant", new Etudiant());
        return "etudiants/form";
    }

    @PostMapping
    public String saveEtudiant(@ModelAttribute Etudiant etudiant) {
        etudiantService.createEtudiant(etudiant);
        return "redirect:/etudiants";
    }

    @GetMapping("/edit/{id}")
    public String editEtudiant(@PathVariable Long id, Model model) {
        etudiantService.getEtudiantById(id).ifPresent(e -> {
            model.addAttribute("etudiant", e);
        });
        return "etudiants/form";
    }

    @PostMapping("/update")
    public String updateEtudiant(@ModelAttribute Etudiant etudiant) {
        etudiantService.updateEtudiant(etudiant);
        return "redirect:/etudiants";
    }

    @GetMapping("/delete/{id}")
    public String deleteEtudiant(@PathVariable Long id) {
        etudiantService.deleteEtudiant(id);
        return "redirect:/etudiants";
    }
}
