
package com.example.demo.service;

import com.example.demo.entity.Resident;
import com.example.demo.repository.ResidentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResidentService {

    private final ResidentRepository residentRepository;
    private final PasswordEncoder passwordEncoder;

    public ResidentService(
            ResidentRepository residentRepository,
            PasswordEncoder passwordEncoder) {

        this.residentRepository = residentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Add Resident
    public Resident saveResident(Resident resident) {

        if (resident.getPassword() != null
                && !resident.getPassword().isBlank()) {

            resident.setPassword(
                    passwordEncoder.encode(resident.getPassword()));
        }

        return residentRepository.save(resident);
    }

    // Get All Residents
    public List<Resident> getAllResidents() {
        return residentRepository.findAll();
    }

    // Get Resident By ID
    public Resident getResidentById(Long id) {
        return residentRepository.findById(id).orElse(null);
    }

    // Update Resident
    public Resident updateResident(Resident resident) {

        if (resident.getId() == null) {
            return null;
        }

        Resident existingResident = residentRepository
                .findById(resident.getId())
                .orElse(null);

        if (existingResident == null) {
            return null;
        }

        existingResident.setName(resident.getName());
        existingResident.setEmail(resident.getEmail());
        existingResident.setPhone(resident.getPhone());
        existingResident.setFlatNumber(resident.getFlatNumber());

        // Change password only when a new password is entered
        if (resident.getPassword() != null
                && !resident.getPassword().isBlank()) {

            existingResident.setPassword(
                    passwordEncoder.encode(resident.getPassword()));
        }

        return residentRepository.save(existingResident);
    }

    // Delete One Resident
    public void deleteResident(Long id) {
        residentRepository.deleteById(id);
    }

    // Delete All Residents
    public void deleteAllResidents() {
        residentRepository.deleteAll();
    }

    // Search By Name
    public List<Resident> searchByName(String name) {
        return residentRepository.findByNameContainingIgnoreCase(name);
    }

    // Search By Email
    public List<Resident> searchByEmail(String email) {
        return residentRepository.findByEmailContainingIgnoreCase(email);
    }

    // Search By Flat Number
    public List<Resident> searchByFlatNumber(String flatNumber) {
        return residentRepository
                .findByFlatNumberContainingIgnoreCase(flatNumber);
    }

    // Search By Name, Email or Flat Number
    public List<Resident> searchResidents(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return getAllResidents();
        }

        String value = keyword.trim();

        List<Resident> results = new ArrayList<>();

        results.addAll(searchByName(value));
        results.addAll(searchByEmail(value));
        results.addAll(searchByFlatNumber(value));

        return results.stream().distinct().toList();
    }

    // Check Duplicate Email During Add or Update
    public boolean isEmailAlreadyUsed(String email, Long id) {

        if (email == null || email.isBlank()) {
            return false;
        }

        if (id == null) {
            return residentRepository.existsByEmailIgnoreCase(email);
        }

        return residentRepository
                .existsByEmailIgnoreCaseAndIdNot(email, id);
    }

    // Check Whether Email Exists
    public boolean emailExists(String email) {
        return residentRepository.existsByEmailIgnoreCase(email);
    }
}
