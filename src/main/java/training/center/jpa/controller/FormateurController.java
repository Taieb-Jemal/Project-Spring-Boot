package training.center.jpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import training.center.jpa.model.Formateur;
import training.center.jpa.service.FormateurService;

@Controller
@RequestMapping("/formateurs")
@RequiredArgsConstructor
public class FormateurController {

    private final FormateurService formateurService;

    @GetMapping
    public String listFormateurs(Model model) {
        model.addAttribute("formateurs", formateurService.getAllFormateurs());
        return "formateurs/list";
    }

    @GetMapping("/{id}")
    public String detailFormateur(@PathVariable Long id, Model model) {
        formateurService.getFormateurById(id).ifPresent(f -> 
            model.addAttribute("formateur", f)
        );
        return "formateurs/detail";
    }

    @GetMapping("/new")
    public String newFormateur(Model model) {
        model.addAttribute("formateur", new Formateur());
        return "formateurs/form";
    }

    @PostMapping
    public String saveFormateur(@ModelAttribute Formateur formateur) {
        formateurService.createFormateur(formateur);
        return "redirect:/formateurs";
    }

    @GetMapping("/edit/{id}")
    public String editFormateur(@PathVariable Long id, Model model) {
        formateurService.getFormateurById(id).ifPresent(f -> 
            model.addAttribute("formateur", f)
        );
        return "formateurs/form";
    }

    @PostMapping("/update")
    public String updateFormateur(@ModelAttribute Formateur formateur) {
        formateurService.updateFormateur(formateur);
        return "redirect:/formateurs";
    }

    @GetMapping("/delete/{id}")
    public String deleteFormateur(@PathVariable Long id) {
        formateurService.deleteFormateur(id);
        return "redirect:/formateurs";
    }
}
