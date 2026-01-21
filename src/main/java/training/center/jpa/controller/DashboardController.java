package training.center.jpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import training.center.jpa.model.Etudiant;
import training.center.jpa.model.Formateur;
import training.center.jpa.repository.CoursRepository;
import training.center.jpa.repository.EtudiantRepository;
import training.center.jpa.repository.FormateurRepository;
import training.center.jpa.repository.NoteRepository;
import training.center.jpa.repository.InscriptionRepository;

import java.util.Optional;

/**
 * Dashboard controller - Role-based pages
 * ADMIN: /admin/dashboard
 * FORMATEUR: /formateur/dashboard
 * ETUDIANT: /etudiant/dashboard
 */
@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final EtudiantRepository etudiantRepository;
    private final FormateurRepository formateurRepository;
    private final CoursRepository coursRepository;
    private final NoteRepository noteRepository;
    private final InscriptionRepository inscriptionRepository;

    @GetMapping
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();
        String userRole = auth.getAuthorities().iterator().next().getAuthority();

        if (userRole.equals("ROLE_ADMIN")) {
            return "redirect:/dashboard/admin";
        } else if (userRole.equals("ROLE_FORMATEUR")) {
            return "redirect:/dashboard/formateur";
        } else if (userRole.equals("ROLE_ETUDIANT")) {
            return "redirect:/dashboard/etudiant";
        }

        return "redirect:/";
    }

    /**
     * Dashboard Formateur - Gestion des cours et notes
     */
    @GetMapping("/formateur")
    public String formateurDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();

        // Chercher par email d'abord, sinon par username
        Optional<Formateur> formateur = formateurRepository.findAll().stream()
                .filter(f -> f.getEmail().equals(userEmail) || f.getIdFormateur().equals(userEmail))
                .findFirst();

        if (formateur.isPresent()) {
            var f = formateur.get();
            var mesCours = coursRepository.findByFormateur(f);
            
            model.addAttribute("formateur", f);
            model.addAttribute("mesCours", mesCours);
            model.addAttribute("totalCours", mesCours.size());
            
            // Calculer le nombre total d'étudiants inscrits
            long totalEtudiants = mesCours.stream()
                    .flatMap(c -> inscriptionRepository.findAll().stream()
                            .filter(i -> i.getCours().getId().equals(c.getId())))
                    .count();
            
            model.addAttribute("totalEtudiants", totalEtudiants);
        }

        model.addAttribute("role", "FORMATEUR");
        return "dashboard/formateur-dashboard";
    }

    /**
     * Dashboard Étudiant - Consultation des cours et notes
     */
    @GetMapping("/etudiant")
    public String etudiantDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();

        // Chercher par email d'abord, sinon par username
        Optional<Etudiant> etudiant = etudiantRepository.findByEmail(userEmail);
        if (etudiant.isEmpty()) {
            // Si pas trouvé par email, chercher dans tous les étudiants
            etudiant = etudiantRepository.findAll().stream()
                    .filter(e -> e.getMatricule().equals(userEmail))
                    .findFirst();
        }

        if (etudiant.isPresent()) {
            var e = etudiant.get();
            var mesInscriptions = inscriptionRepository.findByEtudiant(e);
            var mesNotes = noteRepository.findByEtudiant(e);
            
            // Calculer la moyenne
            double moyenne = mesNotes.isEmpty() ? 0 :
                    mesNotes.stream()
                            .mapToDouble(note -> note.getValeur())
                            .average()
                            .orElse(0.0);
            
            model.addAttribute("etudiant", e);
            model.addAttribute("mesInscriptions", mesInscriptions);
            model.addAttribute("mesNotes", mesNotes);
            model.addAttribute("totalCours", mesInscriptions.size());
            model.addAttribute("totalNotes", mesNotes.size());
            model.addAttribute("moyenne", String.format("%.2f", moyenne));
        }

        model.addAttribute("role", "ETUDIANT");
        return "dashboard/etudiant-dashboard";
    }
}
