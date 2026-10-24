
package com.example.demo.controller;

import com.example.demo.entity.Resident;
import com.example.demo.service.ResidentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/residents")
public class ResidentController {

    private final ResidentService residentService;

    public ResidentController(ResidentService residentService) {
        this.residentService = residentService;
    }

    // Check login session
    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loggedInUser") != null;
    }

    // List and search residents
    @GetMapping
    public String listResidents(
            @RequestParam(required = false) String keyword,
            HttpSession session,
            Model model) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        if (keyword == null || keyword.isBlank()) {
            model.addAttribute(
                    "residents",
                    residentService.getAllResidents());
        } else {
            model.addAttribute(
                    "residents",
                    residentService.searchResidents(keyword));
        }

        model.addAttribute("keyword", keyword);
        model.addAttribute("resident", new Resident());
        model.addAttribute("editMode", false);

        return "residents";
    }

    // Add or update resident
    @PostMapping("/save")
    public String saveResident(
            @Valid @ModelAttribute("resident") Resident resident,
            BindingResult result,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        // Load the list again if the form has errors
        if (result.hasErrors()) {
            model.addAttribute(
                    "residents",
                    residentService.getAllResidents());

            model.addAttribute(
                    "editMode",
                    resident.getId() != null);

            return "residents";
        }

        // Prevent duplicate email for a different resident
        if (residentService.isEmailAlreadyUsed(
                resident.getEmail(), resident.getId())) {

            model.addAttribute(
                    "emailError",
                    "This email is already registered. Please use another email.");

            model.addAttribute(
                    "residents",
                    residentService.getAllResidents());

            model.addAttribute(
                    "editMode",
                    resident.getId() != null);

            return "residents";
        }

        // Add a new resident
        if (resident.getId() == null) {
            residentService.saveResident(resident);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Resident added successfully!");

        } else {
            // Update an existing resident
            Resident updated =
                    residentService.updateResident(resident);

            if (updated == null) {
                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        "Resident could not be found.");

                return "redirect:/residents";
            }

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Resident updated successfully!");
        }

        return "redirect:/residents";
    }

    // Edit resident
    @GetMapping("/edit/{id}")
    public String editResident(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        Resident resident =
                residentService.getResidentById(id);

        if (resident == null) {
            return "redirect:/residents";
        }

        model.addAttribute("resident", resident);
        model.addAttribute(
                "residents",
                residentService.getAllResidents());

        model.addAttribute("editMode", true);

        return "residents";
    }

    // View resident details
    @GetMapping("/view/{id}")
    public String viewResident(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        Resident resident =
                residentService.getResidentById(id);

        if (resident == null) {
            return "redirect:/residents";
        }

        model.addAttribute("resident", resident);

        return "resident-details";
    }

    // Delete one resident
    @GetMapping("/delete/{id}")
    public String deleteResident(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        residentService.deleteResident(id);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Resident deleted successfully!");

        return "redirect:/residents";
    }

    // Delete all residents
    @PostMapping("/delete-all")
    public String deleteAllResidents(
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        residentService.deleteAllResidents();

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "All residents deleted successfully!");

        return "redirect:/residents";
    }
}
