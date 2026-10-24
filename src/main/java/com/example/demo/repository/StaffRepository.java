
package com.example.demo.repository;

import com.example.demo.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    // Search staff by name
    List<Staff> findByNameContainingIgnoreCase(String name);

    // Search staff by email
    List<Staff> findByEmailContainingIgnoreCase(String email);

    // Search staff by role
    List<Staff> findByRoleContainingIgnoreCase(String role);

    // Search staff by department
    List<Staff> findByDepartmentContainingIgnoreCase(String department);

    // Check whether an email already exists
    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}
