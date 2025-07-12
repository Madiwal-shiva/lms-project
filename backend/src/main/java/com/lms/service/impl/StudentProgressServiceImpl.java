package com.lms.service.impl;

import com.lms.entity.Course;
import com.lms.entity.StudentProgress;
import com.lms.entity.User;
import com.lms.exception.ResourceNotFoundException;
import com.lms.repository.StudentProgressRepository;
import com.lms.service.StudentProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentProgressServiceImpl implements StudentProgressService {

    @Autowired
    private StudentProgressRepository studentProgressRepository;

    @Override
    public StudentProgress saveProgress(StudentProgress progress) {
        Optional<StudentProgress> existingProgress = studentProgressRepository
                .findByStudentAndCourseAndContentId(progress.getStudent(), progress.getCourse(), progress.getContentId());
        
        if (existingProgress.isPresent()) {
            StudentProgress existing = existingProgress.get();
            existing.setProgressPercentage(progress.getProgressPercentage());
            existing.setLastAccessedAt(LocalDateTime.now());
            existing.setCompleted(progress.isCompleted());
            return studentProgressRepository.save(existing);
        } else {
            progress.setLastAccessedAt(LocalDateTime.now());
            return studentProgressRepository.save(progress);
        }
    }

    @Override
    public Optional<StudentProgress> getProgress(User student, Course course, String contentId) {
        return studentProgressRepository.findByStudentAndCourseAndContentId(student, course, contentId);
    }

    @Override
    public List<StudentProgress> getStudentProgress(User student, Course course) {
        return studentProgressRepository.findByStudentAndCourse(student, course);
    }

    @Override
    public Page<StudentProgress> getProgressByStudent(User student, Pageable pageable) {
        return studentProgressRepository.findByStudent(student, pageable);
    }

    @Override
    public Page<StudentProgress> getProgressByCourse(Course course, Pageable pageable) {
        return studentProgressRepository.findByCourse(course, pageable);
    }

    @Override
    public Double calculateCourseProgress(User student, Course course) {
        List<StudentProgress> progressList = studentProgressRepository.findByStudentAndCourse(student, course);
        if (progressList.isEmpty()) {
            return 0.0;
        }
        
        double totalProgress = progressList.stream()
                .mapToDouble(StudentProgress::getProgressPercentage)
                .sum();
        
        return totalProgress / progressList.size();
    }

    @Override
    public List<StudentProgress> getProgressByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return studentProgressRepository.findByLastAccessedAtBetween(startDate, endDate);
    }

    @Override
    public Map<String, Object> getProgressStatistics(User instructor) {
        List<StudentProgress> allProgress = studentProgressRepository.findByInstructor(instructor);
        
        long totalStudents = allProgress.stream()
                .map(p -> p.getStudent().getId())
                .distinct()
                .count();
        
        long completedLessons = allProgress.stream()
                .mapToLong(p -> p.isCompleted() ? 1 : 0)
                .sum();
        
        double averageProgress = allProgress.stream()
                .mapToDouble(StudentProgress::getProgressPercentage)
                .average()
                .orElse(0.0);
        
        return Map.of(
                "totalStudents", totalStudents,
                "completedLessons", completedLessons,
                "averageProgress", averageProgress,
                "totalProgressRecords", allProgress.size()
        );
    }

    @Override
    public List<StudentProgress> getProgressByLocation(String city, String country) {
        return studentProgressRepository.findByStudentCityOrStudentCountry(city, country);
    }

    @Override
    public Map<String, Long> getProgressByMonth(int year) {
        List<StudentProgress> yearProgress = studentProgressRepository.findByYear(year);
        
        return yearProgress.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getLastAccessedAt().getMonth().toString(),
                        Collectors.counting()
                ));
    }

    @Override
    public void deleteProgress(Long id) {
        if (!studentProgressRepository.existsById(id)) {
            throw new ResourceNotFoundException("Progress record not found with id: " + id);
        }
        studentProgressRepository.deleteById(id);
    }
}