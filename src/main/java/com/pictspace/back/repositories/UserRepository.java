package com.pictspace.back.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pictspace.back.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

      // ✅ Find HOD of a specific department
      @Query("SELECT u FROM User u WHERE u.role = 'HOD' AND u.department.id = :departmentId")
      User findHODByDepartment(@Param("departmentId") Long departmentId);
  
      // ✅ Find Principal (only one Principal exists)
      @Query("SELECT u FROM User u WHERE u.role = 'Principal'")
      User findPrincipal();
}
