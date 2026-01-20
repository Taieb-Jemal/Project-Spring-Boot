package training.center.jpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import training.center.jpa.model.Etudiant;
import training.center.jpa.service.EtudiantService;
import training.center.jpa.service.GroupeService;
import training.center.jpa.service.SpecialiteService;

@Controller
@RequestMapping("/etudiants")
@RequiredArgsConstructor
public class EtudiantController {

    private final EtudiantService etudiantService;
    private final GroupeService groupeService;
    private final SpecialiteService specialiteService;

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
        model.addAttribute("groupes", groupeService.getAllGroupes());
        model.addAttribute("specialites", specialiteService.getAllSpecialites());
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
            model.addAttribute("groupes", groupeService.getAllGroupes());
            model.addAttribute("specialites", specialiteService.getAllSpecialites());
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
