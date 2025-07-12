package com.lms.repository;

import com.lms.entity.LearningModule;
import com.lms.entity.LearningSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningSectionRepository extends JpaRepository<LearningSection, Long> {

    /**
     * Find all sections for a learning module, ordered by order index
     */
    List<LearningSection> findByModuleOrderByOrderIndex(LearningModule module);

    /**
     * Find required sections for a learning module
     */
    List<LearningSection> findByModuleAndIsRequiredTrueOrderByOrderIndex(LearningModule module);

    /**
     * Count sections in a module
     */
    long countByModule(LearningModule module);

    /**
     * Find section by module and order index
     */
    LearningSection findByModuleAndOrderIndex(LearningModule module, Integer orderIndex);
}
