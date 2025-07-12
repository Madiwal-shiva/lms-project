package com.lms.controller;

import com.lms.entity.LearningPath;
import com.lms.entity.User;
import com.lms.service.LearningPathService;
import com.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/learning-paths")
@CrossOrigin(origins = "http://localhost:3000")
public class LearningPathController {

    @Autowired
    private LearningPathService learningPathService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<LearningPath>> getAllLearningPaths(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(learningPathService.getAllLearningPaths(pageable));
    }

    @GetMapping("/published")
    public ResponseEntity<List<LearningPath>> getPublishedLearningPaths() {
        return ResponseEntity.ok(learningPathService.getPublishedLearningPaths());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<LearningPath>> searchLearningPaths(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(learningPathService.searchLearningPaths(query, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LearningPath> getLearningPathById(@PathVariable Long id) {
        return learningPathService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LearningPath> createLearningPath(@RequestBody LearningPath learningPath, Authentication authentication) {
        User instructor = userService.findByEmail(authentication.getName()).orElse(null);
        if (instructor != null) {
            learningPath.setInstructor(instructor);
        }
        LearningPath createdPath = learningPathService.createLearningPath(learningPath);
        return ResponseEntity.ok(createdPath);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LearningPath> updateLearningPath(@PathVariable Long id, @RequestBody LearningPath learningPath) {
        LearningPath updatedPath = learningPathService.updateLearningPath(id, learningPath);
        return ResponseEntity.ok(updatedPath);
    }

    @PostMapping("/{pathId}/courses/{courseId}")
    public ResponseEntity<LearningPath> addCourseToPath(@PathVariable Long pathId, @PathVariable Long courseId) {
        LearningPath updatedPath = learningPathService.addCourseToPath(pathId, courseId);
        return ResponseEntity.ok(updatedPath);
    }

    @DeleteMapping("/{pathId}/courses/{courseId}")
    public ResponseEntity<LearningPath> removeCourseFromPath(@PathVariable Long pathId, @PathVariable Long courseId) {
        LearningPath updatedPath = learningPathService.removeCourseFromPath(pathId, courseId);
        return ResponseEntity.ok(updatedPath);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLearningPath(@PathVariable Long id) {
        learningPathService.deleteLearningPath(id);
        return ResponseEntity.noContent().build();
    }
}