package training.center.jpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import training.center.jpa.model.Cours;
import training.center.jpa.service.CoursService;
import training.center.jpa.service.FormateurService;


@Controller
@RequestMapping("/cours")
@RequiredArgsConstructor
public class CoursController {

    private final CoursService coursService;
    private final FormateurService formateurService;

    @GetMapping
    public String listCours(Model model) {
        model.addAttribute("cours", coursService.getAllCours());
        return "cours/list";
    }

    @GetMapping("/{id}")
    public String detailCours(@PathVariable Long id, Model model) {
        coursService.getCoursById(id).ifPresent(c -> 
            model.addAttribute("cours", c)
        );
        return "cours/detail";
    }

    @GetMapping("/new")
    public String newCours(Model model) {
        model.addAttribute("cours", new Cours());
        model.addAttribute("formateurs", formateurService.getAllFormateurs());
        return "cours/form";
    }

    @PostMapping
    public String saveCours(@ModelAttribute Cours cours, @RequestParam("formateurId") Long formateurId) {
        if (formateurId != null) {
            formateurService.getFormateurById(formateurId).ifPresent(cours::setFormateur);
        }
        coursService.createCours(cours);
        return "redirect:/cours";
    }

    @GetMapping("/edit/{id}")
    public String editCours(@PathVariable Long id, Model model) {
        coursService.getCoursById(id).ifPresent(c -> {
            model.addAttribute("cours", c);
        });
        model.addAttribute("formateurs", formateurService.getAllFormateurs());
        return "cours/form";
    }

    @PostMapping("/update")
    public String updateCours(@ModelAttribute Cours cours, @RequestParam("formateurId") Long formateurId) {
        if (formateurId != null) {
            formateurService.getFormateurById(formateurId).ifPresent(cours::setFormateur);
        }
        coursService.updateCours(cours);
        return "redirect:/cours";
    }

    @GetMapping("/delete/{id}")
    public String deleteCours(@PathVariable Long id) {
        coursService.deleteCours(id);
        return "redirect:/cours";
    }
}
