package com.lms.repository;

import com.lms.entity.Course;
import com.lms.entity.User;
import com.lms.enums.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Course entity
 * 
 * @author LMS Team
 * @version 1.0
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * Find courses by instructor
     * 
     * @param instructor The instructor user
     * @return List of courses
     */
    List<Course> findByInstructor(User instructor);

    /**
     * Find courses by instructor with pagination
     * 
     * @param instructor The instructor user
     * @param pageable Pagination information
     * @return Page of courses
     */
    Page<Course> findByInstructor(User instructor, Pageable pageable);

    /**
     * Find courses by status
     * 
     * @param status The course status
     * @return List of courses
     */
    List<Course> findByStatus(CourseStatus status);

    /**
     * Find courses by status with pagination
     * 
     * @param status The course status
     * @param pageable Pagination information
     * @return Page of courses
     */
    Page<Course> findByStatus(CourseStatus status, Pageable pageable);

    /**
     * Find courses by category
     * 
     * @param category The course category
     * @return List of courses
     */
    List<Course> findByCategory(String category);

    /**
     * Find courses by category with pagination
     * 
     * @param category The course category
     * @param pageable Pagination information
     * @return Page of courses
     */
    Page<Course> findByCategory(String category, Pageable pageable);

    /**
     * Find courses by level
     * 
     * @param level The course level
     * @return List of courses
     */
    List<Course> findByLevel(String level);

    /**
     * Find featured courses
     * 
     * @return List of featured courses
     */
    List<Course> findByFeaturedTrue();

    /**
     * Find featured courses with status
     * 
     * @param status Course status
     * @param pageable Pagination information
     * @return Page of featured courses
     */
    Page<Course> findByFeaturedTrueAndStatus(CourseStatus status, Pageable pageable);

    /**
     * Find courses by title containing text (case insensitive)
     * 
     * @param title Title search term
     * @param pageable Pagination information
     * @return Page of courses
     */
    Page<Course> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    /**
     * Find courses by price range
     * 
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @return List of courses
     */
    List<Course> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Find courses by price range with pagination
     * 
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @param pageable Pagination information
     * @return Page of courses
     */
    Page<Course> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    /**
     * Find courses by start date range
     * 
     * @param startDate Start date
     * @param endDate End date
     * @return List of courses
     */
    List<Course> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find courses by minimum rating
     * 
     * @param minRating Minimum rating
     * @return List of courses
     */
    @Query("SELECT c FROM Course c WHERE c.rating >= :minRating")
    List<Course> findByRatingGreaterThanEqual(@Param("minRating") Double minRating);

    /**
     * Find courses by instructor and status
     * 
     * @param instructor The instructor user
     * @param status The course status
     * @return List of courses
     */
    List<Course> findByInstructorAndStatus(User instructor, CourseStatus status);

    /**
     * Find courses by language
     * 
     * @param language The course language
     * @return List of courses
     */
    List<Course> findByLanguage(String language);

    /**
     * Find courses ordered by enrollment count
     * 
     * @param pageable Pagination information
     * @return Page of courses ordered by enrollment count
     */
    Page<Course> findAllByOrderByEnrollmentCountDesc(Pageable pageable);

    /**
     * Find courses ordered by rating
     * 
     * @param pageable Pagination information
     * @return Page of courses ordered by rating
     */
    Page<Course> findAllByOrderByRatingDesc(Pageable pageable);

    /**
     * Find courses ordered by creation date
     * 
     * @param pageable Pagination information
     * @return Page of courses ordered by creation date
     */
    Page<Course> findAllByOrderByCreatedAtDesc(Pageable pageable);

    /**
     * Find courses by title or description containing text
     * 
     * @param title Title search term
     * @param description Description search term
     * @param pageable Pagination information
     * @return Page of matching courses
     */
    Page<Course> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description, Pageable pageable);

    /**
     * Search courses by title, description, or category
     * 
     * @param searchTerm Search term
     * @param pageable Pagination information
     * @return Page of matching courses
     */
    @Query("SELECT c FROM Course c WHERE " +
           "LOWER(c.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.category) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Course> searchCourses(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Count courses by status
     * 
     * @param status The course status
     * @return Count of courses
     */
    long countByStatus(CourseStatus status);

    /**
     * Count courses by instructor
     * 
     * @param instructor The instructor user
     * @return Count of courses
     */
    long countByInstructor(User instructor);

    /**
     * Count courses by category
     * 
     * @param category The course category
     * @return Count of courses
     */
    long countByCategory(String category);

    /**
     * Find courses with available slots
     * 
     * @return List of courses with available enrollment slots
     */
    @Query("SELECT c FROM Course c WHERE c.maxStudents IS NULL OR c.enrollmentCount < c.maxStudents")
    List<Course> findCoursesWithAvailableSlots();

    /**
     * Find courses by tags containing
     * 
     * @param tag The tag to search for
     * @return List of courses
     */
    @Query("SELECT c FROM Course c WHERE LOWER(c.tags) LIKE LOWER(CONCAT('%', :tag, '%'))")
    List<Course> findByTagsContainingIgnoreCase(@Param("tag") String tag);
}
