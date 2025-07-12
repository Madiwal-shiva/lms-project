package com.lms.repository;

import com.lms.entity.LearningModule;
import com.lms.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LearningModuleRepository extends JpaRepository<LearningModule, Long> {

    /**
     * Find all published learning modules
     */
    List<LearningModule> findByIsPublishedTrue();

    /**
     * Find all learning modules by creator
     */
    List<LearningModule> findByCreatedBy(User createdBy);

    /**
     * Find published learning modules by creator
     */
    List<LearningModule> findByCreatedByAndIsPublishedTrue(User createdBy);

    /**
     * Find learning modules by subject
     */
    List<LearningModule> findBySubjectAndIsPublishedTrue(String subject);

    /**
     * Find learning modules by level
     */
    List<LearningModule> findByLevelAndIsPublishedTrue(String level);

    /**
     * Find learning modules by subject and level
     */
    List<LearningModule> findBySubjectAndLevelAndIsPublishedTrue(String subject, String level);

    /**
     * Search learning modules by title or description
     */
    @Query("SELECT lm FROM LearningModule lm WHERE " +
           "(lm.title LIKE %:searchTerm% OR lm.description LIKE %:searchTerm%) " +
           "AND lm.isPublished = true")
    List<LearningModule> searchPublishedModules(@Param("searchTerm") String searchTerm);

    /**
     * Find learning modules by tags
     */
    @Query("SELECT lm FROM LearningModule lm WHERE " +
           "lm.tags LIKE %:tag% AND lm.isPublished = true")
    List<LearningModule> findByTagsContaining(@Param("tag") String tag);

    /**
     * Get all distinct subjects from published modules
     */
    @Query("SELECT DISTINCT lm.subject FROM LearningModule lm WHERE " +
           "lm.isPublished = true AND lm.subject IS NOT NULL ORDER BY lm.subject")
    List<String> findDistinctSubjects();

    /**
     * Get all distinct levels from published modules
     */
    @Query("SELECT DISTINCT lm.level FROM LearningModule lm WHERE " +
           "lm.isPublished = true AND lm.level IS NOT NULL ORDER BY lm.level")
    List<String> findDistinctLevels();

    /**
     * Find learning modules with pagination
     */
    Page<LearningModule> findByIsPublishedTrue(Pageable pageable);

    /**
     * Find learning modules by creator with pagination
     */
    Page<LearningModule> findByCreatedBy(User createdBy, Pageable pageable);

    /**
     * Advanced search with filters
     */
    @Query("SELECT lm FROM LearningModule lm WHERE " +
           "(:subject IS NULL OR lm.subject = :subject) " +
           "AND (:level IS NULL OR lm.level = :level) " +
           "AND (:searchTerm IS NULL OR lm.title LIKE %:searchTerm% OR lm.description LIKE %:searchTerm%) " +
           "AND lm.isPublished = true")
    Page<LearningModule> findWithFilters(
        @Param("subject") String subject,
        @Param("level") String level,
        @Param("searchTerm") String searchTerm,
        Pageable pageable
    );

    /**
     * Count published modules by creator
     */
    long countByCreatedByAndIsPublishedTrue(User createdBy);

    /**
     * Count all published modules
     */
    long countByIsPublishedTrue();
}
