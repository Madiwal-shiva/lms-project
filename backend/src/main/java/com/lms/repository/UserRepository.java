package com.lms.repository;

import com.lms.entity.User;
import com.lms.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity
 * 
 * @author LMS Team
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email
     * 
     * @param email The email address
     * @return Optional user
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by Google ID
     * 
     * @param googleId The Google ID
     * @return Optional user
     */
    Optional<User> findByGoogleId(String googleId);

    /**
     * Find users by role
     * 
     * @param role The user role
     * @return List of users
     */
    List<User> findByRole(Role role);

    /**
     * Find users by role with pagination
     * 
     * @param role The user role
     * @param pageable Pagination information
     * @return Page of users
     */
    Page<User> findByRole(Role role, Pageable pageable);

    /**
     * Find active users
     * 
     * @return List of active users
     */
    List<User> findByActiveTrue();

    /**
     * Find active users with pagination
     * 
     * @param pageable Pagination information
     * @return Page of active users
     */
    Page<User> findByActiveTrue(Pageable pageable);

    /**
     * Find users by role and active status
     * 
     * @param role The user role
     * @param isActive Active status
     * @return List of users
     */
    List<User> findByRoleAndIsActive(Role role, Boolean isActive);

    /**
     * Find users by first name or last name containing text (case insensitive)
     * 
     * @param firstName First name search term
     * @param lastName Last name search term
     * @param pageable Pagination information
     * @return Page of users
     */
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    Page<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            @Param("firstName") String firstName, 
            @Param("lastName") String lastName, 
            Pageable pageable);

    /**
     * Find users by city
     * 
     * @param city The city name
     * @return List of users
     */
    List<User> findByCity(String city);

    /**
     * Find users by country
     * 
     * @param country The country name
     * @return List of users
     */
    List<User> findByCountry(String country);

    /**
     * Check if email exists
     * 
     * @param email The email address
     * @return true if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Count users by role
     * 
     * @param role The user role
     * @return Count of users
     */
    long countByRole(Role role);

    /**
     * Count active users
     * 
     * @return Count of active users
     */
    long countByActiveTrue();

    /**
     * Find instructors with courses
     * 
     * @return List of instructors who have created courses
     */
    @Query("SELECT DISTINCT u FROM User u JOIN u.createdCourses c WHERE u.role = :role")
    List<User> findInstructorsWithCourses(@Param("role") Role role);

    /**
     * Find students with enrollments
     * 
     * @return List of students who have enrollments
     */
    @Query("SELECT DISTINCT u FROM User u JOIN u.enrollments e WHERE u.role = :role")
    List<User> findStudentsWithEnrollments(@Param("role") Role role);
}
