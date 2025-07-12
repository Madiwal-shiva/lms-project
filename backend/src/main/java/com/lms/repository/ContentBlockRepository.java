package com.lms.repository;

import com.lms.entity.ContentBlock;
import com.lms.entity.LearningSection;
import com.lms.enums.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentBlockRepository extends JpaRepository<ContentBlock, Long> {

    /**
     * Find all content blocks for a section, ordered by order index
     */
    List<ContentBlock> findBySectionOrderByOrderIndex(LearningSection section);

    /**
     * Find content blocks by type for a section
     */
    List<ContentBlock> findBySectionAndContentTypeOrderByOrderIndex(LearningSection section, ContentType contentType);

    /**
     * Count content blocks in a section
     */
    long countBySection(LearningSection section);

    /**
     * Find content block by section and order index
     */
    ContentBlock findBySectionAndOrderIndex(LearningSection section, Integer orderIndex);
}
