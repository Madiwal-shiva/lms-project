package com.lms.service;

import com.lms.entity.LearningPath;
import com.lms.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LearningPathService {
    
    LearningPath createLearningPath(LearningPath learningPath);
    
    LearningPath updateLearningPath(Long id, LearningPath learningPath);
    
    Optional<LearningPath> findById(Long id);
    
    Page<LearningPath> getAllLearningPaths(Pageable pageable);
    
    Page<LearningPath> getLearningPathsByInstructor(User instructor, Pageable pageable);
    
    Page<LearningPath> searchLearningPaths(String searchTerm, Pageable pageable);
    
    void deleteLearningPath(Long id);
    
    LearningPath addCourseToPath(Long pathId, Long courseId);
    
    LearningPath removeCourseFromPath(Long pathId, Long courseId);
    
    List<LearningPath> getPublishedLearningPaths();
}