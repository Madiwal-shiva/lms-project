package com.lms.service;

import com.lms.entity.User;
import com.lms.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for User operations
 * 
 * @author LMS Team
 * @version 1.0
 */
public interface UserService {

    /**
     * Create a new user
     * 
     * @param user The user to create
     * @return The created user
     */
    User createUser(User user);

    /**
     * Update an existing user
     * 
     * @param id The user ID
     * @param user The updated user data
     * @return The updated user
     */
    User updateUser(Long id, User user);

    /**
     * Find user by ID
     * 
     * @param id The user ID
     * @return Optional user
     */
    Optional<User> findById(Long id);

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
     * Get all users with pagination
     * 
     * @param pageable Pagination information
     * @return Page of users
     */
    Page<User> getAllUsers(Pageable pageable);

    /**
     * Get users by role
     * 
     * @param role The user role
     * @param pageable Pagination information
     * @return Page of users
     */
    Page<User> getUsersByRole(Role role, Pageable pageable);

    /**
     * Get active users
     * 
     * @param pageable Pagination information
     * @return Page of active users
     */
    Page<User> getActiveUsers(Pageable pageable);

    /**
     * Search users by name
     * 
     * @param name The name to search
     * @param pageable Pagination information
     * @return Page of matching users
     */
    Page<User> searchUsersByName(String name, Pageable pageable);

    /**
     * Activate a user
     * 
     * @param id The user ID
     * @return The activated user
     */
    User activateUser(Long id);

    /**
     * Deactivate a user
     * 
     * @param id The user ID
     * @return The deactivated user
     */
    User deactivateUser(Long id);

    /**
     * Delete a user
     * 
     * @param id The user ID
     */
    void deleteUser(Long id);

    /**
     * Check if email exists
     * 
     * @param email The email address
     * @return true if email exists
     */
    boolean emailExists(String email);

    /**
     * Get user statistics
     * 
     * @return User statistics
     */
    UserStatistics getUserStatistics();

    /**
     * Inner class for user statistics
     */
    class UserStatistics {
        private long totalUsers;
        private long activeUsers;
        private long instructors;
        private long students;

        public UserStatistics(long totalUsers, long activeUsers, long instructors, long students) {
            this.totalUsers = totalUsers;
            this.activeUsers = activeUsers;
            this.instructors = instructors;
            this.students = students;
        }

        // Getters and setters
        public long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
        public long getActiveUsers() { return activeUsers; }
        public void setActiveUsers(long activeUsers) { this.activeUsers = activeUsers; }
        public long getInstructors() { return instructors; }
        public void setInstructors(long instructors) { this.instructors = instructors; }
        public long getStudents() { return students; }
        public void setStudents(long students) { this.students = students; }
    }
}
