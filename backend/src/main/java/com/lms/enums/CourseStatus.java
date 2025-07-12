package com.lms.enums;

/**
 * Enum representing different course statuses
 * 
 * @author LMS Team
 * @version 1.0
 */
public enum CourseStatus {
    /**
     * Course is being created/edited
     */
    DRAFT,
    
    /**
     * Course is published and available for enrollment
     */
    PUBLISHED,
    
    /**
     * Course is temporarily unavailable
     */
    SUSPENDED,
    
    /**
     * Course is completed and archived
     */
    ARCHIVED
}
