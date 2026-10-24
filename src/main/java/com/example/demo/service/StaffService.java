
package com.example.demo.service;

import com.example.demo.entity.Staff;
import com.example.demo.repository.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StaffService {

    private final StaffRepository staffRepository;

    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    // Add Staff
    public Staff saveStaff(Staff staff) {
        return staffRepository.save(staff);
    }

    // Get All Staff
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    // Get Staff By ID
    public Optional<Staff> getStaffById(Long id) {
        return staffRepository.findById(id);
    }

    // Update Staff
    public Staff updateStaff(Staff staff) {

        Staff existingStaff = staffRepository
                .findById(staff.getId())
                .orElse(null);

        if (existingStaff == null) {
            return null;
        }

        existingStaff.setName(staff.getName());
        existingStaff.setEmail(staff.getEmail());
        existingStaff.setPhone(staff.getPhone());
        existingStaff.setRole(staff.getRole());
        existingStaff.setDepartment(staff.getDepartment());
        existingStaff.setSalary(staff.getSalary());

        return staffRepository.save(existingStaff);
    }

    // Delete Staff By ID
    public void deleteStaff(Long id) {
        staffRepository.deleteById(id);
    }

    // Delete All Staff
    public void deleteAllStaff() {
        staffRepository.deleteAll();
    }

    // Search Staff By Name
    public List<Staff> searchByName(String name) {
        return staffRepository.findByNameContainingIgnoreCase(name);
    }

    // Search Staff By Email
    public List<Staff> searchByEmail(String email) {
        return staffRepository.findByEmailContainingIgnoreCase(email);
    }

    // Search Staff By Role
    public List<Staff> searchByRole(String role) {
        return staffRepository.findByRoleContainingIgnoreCase(role);
    }

    // Search Staff By Department
    public List<Staff> searchByDepartment(String department) {
        return staffRepository
                .findByDepartmentContainingIgnoreCase(department);
    }

    // Search Staff By Name, Email, Role or Department
    public List<Staff> searchStaff(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return getAllStaff();
        }

        String value = keyword.trim();

        List<Staff> results = new ArrayList<>();

        results.addAll(searchByName(value));
        results.addAll(searchByEmail(value));
        results.addAll(searchByRole(value));
        results.addAll(searchByDepartment(value));

        return results.stream().distinct().toList();
    }

    // Check Duplicate Email During Add or Update
    public boolean isEmailAlreadyUsed(String email, Long id) {

        if (email == null || email.isBlank()) {
            return false;
        }

        if (id == null) {
            return staffRepository.existsByEmailIgnoreCase(email);
        }

        return staffRepository
                .existsByEmailIgnoreCaseAndIdNot(email, id);
    }

    // Check Whether Email Exists
    public boolean emailExists(String email) {
        return staffRepository.existsByEmailIgnoreCase(email);
    }
}
