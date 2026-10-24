
package com.example.demo.repository;

import com.example.demo.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResidentRepository extends JpaRepository<Resident, Long> {

    // Search residents by name
    List<Resident> findByNameContainingIgnoreCase(String name);

    // Search residents by email
    List<Resident> findByEmailContainingIgnoreCase(String email);

    // Search residents by flat number
    List<Resident> findByFlatNumberContainingIgnoreCase(String flatNumber);

    // Check whether an email already exists
    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}
