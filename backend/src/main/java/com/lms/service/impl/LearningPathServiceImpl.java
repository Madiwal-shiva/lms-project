package com.lms.service.impl;

import com.lms.entity.Course;
import com.lms.entity.LearningPath;
import com.lms.entity.User;
import com.lms.exception.ResourceNotFoundException;
import com.lms.repository.CourseRepository;
import com.lms.repository.LearningPathRepository;
import com.lms.service.LearningPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LearningPathServiceImpl implements LearningPathService {

    @Autowired
    private LearningPathRepository learningPathRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public LearningPath createLearningPath(LearningPath learningPath) {
        learningPath.setCreatedAt(LocalDateTime.now());
        return learningPathRepository.save(learningPath);
    }

    @Override
    public LearningPath updateLearningPath(Long id, LearningPath learningPath) {
        LearningPath existingPath = learningPathRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Learning path not found with id: " + id));
        
        existingPath.setTitle(learningPath.getTitle());
        existingPath.setDescription(learningPath.getDescription());
        existingPath.setUpdatedAt(LocalDateTime.now());
        
        return learningPathRepository.save(existingPath);
    }

    @Override
    public Optional<LearningPath> findById(Long id) {
        return learningPathRepository.findById(id);
    }

    @Override
    public Page<LearningPath> getAllLearningPaths(Pageable pageable) {
        return learningPathRepository.findAll(pageable);
    }

    @Override
    public Page<LearningPath> getLearningPathsByInstructor(User instructor, Pageable pageable) {
        return learningPathRepository.findByInstructor(instructor, pageable);
    }

    @Override
    public Page<LearningPath> searchLearningPaths(String searchTerm, Pageable pageable) {
        return learningPathRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchTerm, searchTerm, pageable);
    }

    @Override
    public void deleteLearningPath(Long id) {
        if (!learningPathRepository.existsById(id)) {
            throw new ResourceNotFoundException("Learning path not found with id: " + id);
        }
        learningPathRepository.deleteById(id);
    }

    @Override
    public LearningPath addCourseToPath(Long pathId, Long courseId) {
        LearningPath learningPath = learningPathRepository.findById(pathId)
                .orElseThrow(() -> new ResourceNotFoundException("Learning path not found with id: " + pathId));
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
        
        learningPath.getCourses().add(course);
        learningPath.setUpdatedAt(LocalDateTime.now());
        
        return learningPathRepository.save(learningPath);
    }

    @Override
    public LearningPath removeCourseFromPath(Long pathId, Long courseId) {
        LearningPath learningPath = learningPathRepository.findById(pathId)
                .orElseThrow(() -> new ResourceNotFoundException("Learning path not found with id: " + pathId));
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
        
        learningPath.getCourses().remove(course);
        learningPath.setUpdatedAt(LocalDateTime.now());
        
        return learningPathRepository.save(learningPath);
    }

    @Override
    public List<LearningPath> getPublishedLearningPaths() {
        return learningPathRepository.findByPublishedTrue();
    }
}