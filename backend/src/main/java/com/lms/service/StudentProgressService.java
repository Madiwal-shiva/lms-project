package com.lms.service;

import com.lms.entity.LearningModule;
import com.lms.entity.StudentProgress;
import com.lms.entity.User;

import java.util.List;
import java.util.Optional;

public interface StudentProgressService {

    /**
     * Record or update student progress in a module
     */
    StudentProgress recordProgress(StudentProgress progress);

    /**
     * Retrieve progress for a specific student and module
     */
    Optional<StudentProgress> getProgress(User student, LearningModule module);

    /**
     * Retrieve all progress records for a student
     */
    List<StudentProgress> getProgressByStudent(User student);

    /**
     * Retrieve all progress records for a module
     */
    List<StudentProgress> getProgressByModule(LearningModule module);

    /**
     * Retrieve completed modules for a student
     */
    List<StudentProgress> getCompletedModules(User student);

    /**
     * Retrieve in-progress modules for a student
     */
    List<StudentProgress> getInProgressModules(User student);

    /**
     * Calculate student's average module completion percentage
     */
    Double getStudentAverageCompletionPercentage(User student);

    /**
     * Calculate total time a student has spent on modules
     */
    Long getTotalTimeSpentByStudent(User student);

    /**
     * Count completed modules by a student
     */
    long countCompletedModulesByStudent(User student);

    /**
     * Count total modules a student is enrolled in
     */
    long countTotalModulesByStudent(User student);

    /**
     * Get module statistics
     */
    Object[] getModuleStatistics(LearningModule module);

    /**
     * Get students who have completed a specific module
     */
    List<User> getStudentsWhoCompletedModule(LearningModule module);

    /**
     * Get top performing students based on completion rate
     */
    List<Object[]> getTopPerformingStudents();

    /**
     * Get students currently studying a module
     */
    List<StudentProgress> getActiveStudentsInModule(LearningModule module);

    /**
     * Check if student has access to a specific module
     */
    boolean hasAccessToModule(User student, LearningModule module);
}

