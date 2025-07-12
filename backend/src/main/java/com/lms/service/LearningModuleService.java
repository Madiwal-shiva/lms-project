package com.lms.service;

import com.lms.entity.LearningModule;
import com.lms.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LearningModuleService {

    /**
     * Create a new learning module
     */
    LearningModule createModule(LearningModule module);

    /**
     * Update an existing learning module
     */
    LearningModule updateModule(Long moduleId, LearningModule module);

    /**
     * Delete a learning module
     */
    void deleteModule(Long moduleId);

    /**
     * Get learning module by ID
     */
    Optional<LearningModule> getModuleById(Long moduleId);

    /**
     * Get all published learning modules
     */
    List<LearningModule> getAllPublishedModules();

    /**
     * Get published modules with pagination
     */
    Page<LearningModule> getPublishedModules(Pageable pageable);

    /**
     * Get modules created by a specific user
     */
    List<LearningModule> getModulesByCreator(User creator);

    /**
     * Get published modules by subject
     */
    List<LearningModule> getModulesBySubject(String subject);

    /**
     * Get published modules by level
     */
    List<LearningModule> getModulesByLevel(String level);

    /**
     * Search modules by keyword
     */
    List<LearningModule> searchModules(String searchTerm);

    /**
     * Advanced search with filters
     */
    Page<LearningModule> searchModulesWithFilters(String subject, String level, String searchTerm, Pageable pageable);

    /**
     * Get modules by tags
     */
    List<LearningModule> getModulesByTag(String tag);

    /**
     * Publish a learning module
     */
    LearningModule publishModule(Long moduleId);

    /**
     * Unpublish a learning module
     */
    LearningModule unpublishModule(Long moduleId);

    /**
     * Get all distinct subjects
     */
    List<String> getAllSubjects();

    /**
     * Get all distinct levels
     */
    List<String> getAllLevels();

    /**
     * Get module statistics
     */
    long getPublishedModuleCount();

    /**
     * Get modules count by creator
     */
    long getModuleCountByCreator(User creator);

    /**
     * Clone a learning module
     */
    LearningModule cloneModule(Long moduleId, User newCreator);

    /**
     * Check if user can access module
     */
    boolean canUserAccessModule(User user, Long moduleId);
}
