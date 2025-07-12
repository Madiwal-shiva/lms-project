package com.lms.service.impl;

import com.lms.entity.LearningModule;
import com.lms.entity.User;
import com.lms.exception.ResourceNotFoundException;
import com.lms.repository.LearningModuleRepository;
import com.lms.service.LearningModuleService;
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
public class LearningModuleServiceImpl implements LearningModuleService {

    private final LearningModuleRepository learningModuleRepository;

    @Autowired
    public LearningModuleServiceImpl(LearningModuleRepository learningModuleRepository) {
        this.learningModuleRepository = learningModuleRepository;
    }

    @Override
    public LearningModule createModule(LearningModule module) {
        module.setIsPublished(false); // New modules are draft by default
        return learningModuleRepository.save(module);
    }

    @Override
    public LearningModule updateModule(Long moduleId, LearningModule module) {
        LearningModule existingModule = learningModuleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Learning module not found with id: " + moduleId));

        // Update fields
        existingModule.setTitle(module.getTitle());
        existingModule.setDescription(module.getDescription());
        existingModule.setSubject(module.getSubject());
        existingModule.setLevel(module.getLevel());
        existingModule.setEstimatedDuration(module.getEstimatedDuration());
        existingModule.setTags(module.getTags());

        return learningModuleRepository.save(existingModule);
    }

    @Override
    public void deleteModule(Long moduleId) {
        LearningModule module = learningModuleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Learning module not found with id: " + moduleId));
        
        learningModuleRepository.delete(module);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LearningModule> getModuleById(Long moduleId) {
        return learningModuleRepository.findById(moduleId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LearningModule> getAllPublishedModules() {
        return learningModuleRepository.findByIsPublishedTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LearningModule> getPublishedModules(Pageable pageable) {
        return learningModuleRepository.findByIsPublishedTrue(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LearningModule> getModulesByCreator(User creator) {
        return learningModuleRepository.findByCreatedBy(creator);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LearningModule> getModulesBySubject(String subject) {
        return learningModuleRepository.findBySubjectAndIsPublishedTrue(subject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LearningModule> getModulesByLevel(String level) {
        return learningModuleRepository.findByLevelAndIsPublishedTrue(level);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LearningModule> searchModules(String searchTerm) {
        return learningModuleRepository.searchPublishedModules(searchTerm);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LearningModule> searchModulesWithFilters(String subject, String level, String searchTerm, Pageable pageable) {
        return learningModuleRepository.findWithFilters(subject, level, searchTerm, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LearningModule> getModulesByTag(String tag) {
        return learningModuleRepository.findByTagsContaining(tag);
    }

    @Override
    public LearningModule publishModule(Long moduleId) {
        LearningModule module = learningModuleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Learning module not found with id: " + moduleId));

        module.setIsPublished(true);
        return learningModuleRepository.save(module);
    }

    @Override
    public LearningModule unpublishModule(Long moduleId) {
        LearningModule module = learningModuleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Learning module not found with id: " + moduleId));

        module.setIsPublished(false);
        return learningModuleRepository.save(module);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllSubjects() {
        return learningModuleRepository.findDistinctSubjects();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllLevels() {
        return learningModuleRepository.findDistinctLevels();
    }

    @Override
    @Transactional(readOnly = true)
    public long getPublishedModuleCount() {
        return learningModuleRepository.countByIsPublishedTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public long getModuleCountByCreator(User creator) {
        return learningModuleRepository.countByCreatedByAndIsPublishedTrue(creator);
    }

    @Override
    public LearningModule cloneModule(Long moduleId, User newCreator) {
        LearningModule originalModule = learningModuleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Learning module not found with id: " + moduleId));

        LearningModule clonedModule = new LearningModule();
        clonedModule.setTitle(originalModule.getTitle() + " (Copy)");
        clonedModule.setDescription(originalModule.getDescription());
        clonedModule.setSubject(originalModule.getSubject());
        clonedModule.setLevel(originalModule.getLevel());
        clonedModule.setEstimatedDuration(originalModule.getEstimatedDuration());
        clonedModule.setTags(originalModule.getTags());
        clonedModule.setCreatedBy(newCreator);
        clonedModule.setIsPublished(false); // Cloned modules start as drafts

        return learningModuleRepository.save(clonedModule);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canUserAccessModule(User user, Long moduleId) {
        Optional<LearningModule> module = learningModuleRepository.findById(moduleId);
        
        if (module.isEmpty()) {
            return false;
        }

        LearningModule learningModule = module.get();
        
        // Creators can always access their modules
        if (learningModule.getCreatedBy().equals(user)) {
            return true;
        }

        // Others can only access published modules
        return learningModule.getIsPublished();
    }
}
