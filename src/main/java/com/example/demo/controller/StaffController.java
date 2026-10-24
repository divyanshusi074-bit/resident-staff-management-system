
package com.example.demo.controller;

import com.example.demo.entity.Staff;
import com.example.demo.service.StaffService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    // Check login session
    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loggedInUser") != null;
    }

    // List and search staff
    @GetMapping
    public String listStaff(
            @RequestParam(required = false) String keyword,
            HttpSession session,
            Model model) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        if (keyword == null || keyword.isBlank()) {
            model.addAttribute(
                    "staffList",
                    staffService.getAllStaff());
        } else {
            model.addAttribute(
                    "staffList",
                    staffService.searchStaff(keyword));
        }

        model.addAttribute("keyword", keyword);
        model.addAttribute("staff", new Staff());
        model.addAttribute("editMode", false);

        return "staff";
    }

    // Add or update staff
    @PostMapping("/save")
    public String saveStaff(
            @Valid @ModelAttribute("staff") Staff staff,
            BindingResult result,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        // Display validation errors on the same page
        if (result.hasErrors()) {
            model.addAttribute(
                    "staffList",
                    staffService.getAllStaff());

            model.addAttribute(
                    "editMode",
                    staff.getId() != null);

            return "staff";
        }

        // Prevent duplicate email for another staff member
        if (staffService.isEmailAlreadyUsed(
                staff.getEmail(), staff.getId())) {

            model.addAttribute(
                    "emailError",
                    "This email is already registered. Please use another email.");

            model.addAttribute(
                    "staffList",
                    staffService.getAllStaff());

            model.addAttribute(
                    "editMode",
                    staff.getId() != null);

            return "staff";
        }

        // Keep the existing service method for saving.
        // Confirm its update/password behaviour before changing it.
        staffService.saveStaff(staff);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                staff.getId() == null
                        ? "Staff added successfully!"
                        : "Staff saved successfully!");

        return "redirect:/staff";
    }

    // Edit staff
    @GetMapping("/edit/{id}")
    public String editStaff(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        Staff staff = staffService.getStaffById(id)
                .orElse(null);

        if (staff == null) {
            return "redirect:/staff";
        }

        model.addAttribute("staff", staff);
        model.addAttribute(
                "staffList",
                staffService.getAllStaff());

        model.addAttribute("editMode", true);

        return "staff";
    }

    // View staff details
    @GetMapping("/view/{id}")
    public String viewStaff(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        Staff staff = staffService.getStaffById(id)
                .orElse(null);

        if (staff == null) {
            return "redirect:/staff";
        }

        model.addAttribute("staff", staff);

        return "staff-details";
    }

    // Delete one staff record
    @GetMapping("/delete/{id}")
    public String deleteStaff(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        staffService.deleteStaff(id);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Staff member deleted successfully!");

        return "redirect:/staff";
    }

    // Delete all staff records
    @PostMapping("/delete-all")
    public String deleteAllStaff(
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        staffService.deleteAllStaff();

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "All staff records deleted successfully!");

        return "redirect:/staff";
    }
}
