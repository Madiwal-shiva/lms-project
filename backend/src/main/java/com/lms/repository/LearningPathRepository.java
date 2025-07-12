package com.lms.repository;

import com.lms.entity.LearningPath;
import com.lms.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningPathRepository extends JpaRepository<LearningPath, Long> {
    
    Page<LearningPath> findByInstructor(User instructor, Pageable pageable);
    
    Page<LearningPath> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description, Pageable pageable);
    
    List<LearningPath> findByPublishedTrue();
    
    List<LearningPath> findByInstructor(User instructor);
    
    long countByInstructor(User instructor);
}