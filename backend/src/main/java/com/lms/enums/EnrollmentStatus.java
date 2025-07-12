package com.lms.enums;

/**
 * Enum representing different enrollment statuses
 * 
 * @author LMS Team
 * @version 1.0
 */
public enum EnrollmentStatus {
    /**
     * Student is actively enrolled in the course
     */
    ACTIVE,
    
    /**
     * Student has completed the course
     */
    COMPLETED,
    
    /**
     * Student has dropped out of the course
     */
    DROPPED,
    
    /**
     * Enrollment is suspended
     */
    SUSPENDED
}
