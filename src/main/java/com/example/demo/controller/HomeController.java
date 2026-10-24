package com.example.demo.controller;

import com.example.demo.entity.Resident;
import com.example.demo.entity.Staff;
import com.example.demo.service.ResidentService;
import com.example.demo.service.StaffService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final ResidentService residentService;
    private final StaffService staffService;

    public HomeController(
            ResidentService residentService,
            StaffService staffService) {

        this.residentService = residentService;
        this.staffService = staffService;
    }

    @GetMapping("/")
    public String home(Model model) {

        // 1. Get all residents
        List<Resident> residents =
                residentService.getAllResidents();

        // 2. Get all staff
        List<Staff> staffList =
                staffService.getAllStaff();

        // 3. Calculate total counts
        long residentCount = residents.size();
        long staffCount = staffList.size();

        // 4. Get latest five residents by ID
        List<Resident> recentResidents =
                residents.stream()
                        .sorted(
                                Comparator.comparing(
                                        Resident::getId
                                ).reversed()
                        )
                        .limit(5)
                        .toList();

        // 5. Count staff department-wise
        Map<String, Long> departmentCounts =
                staffList.stream()
                        .filter(staff ->
                                staff.getDepartment() != null
                                        && !staff.getDepartment()
                                        .isBlank())
                        .collect(
                                Collectors.groupingBy(
                                        staff -> staff.getDepartment()
                                                .trim(),
                                        Collectors.counting()
                                )
                        );

        // 6. Send data to dashboard
        model.addAttribute(
                "residentCount", residentCount);

        model.addAttribute(
                "staffCount", staffCount);

        model.addAttribute(
                "recentResidents", recentResidents);

        model.addAttribute(
                "departmentCounts", departmentCounts);

        // 7. Open dashboard
        return "index";
    }
}

