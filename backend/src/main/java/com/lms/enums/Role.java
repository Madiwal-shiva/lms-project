package com.lms.enums;

/**
 * Enum representing different user roles in the LMS system
 * 
 * @author LMS Team
 * @version 1.0
 */
public enum Role {
    /**
     * Instructor role - can create courses, manage content, track progress
     */
    INSTRUCTOR,
    
    /**
     * Student role - can enroll in courses, access materials, take assessments
     */
    STUDENT,
    
    /**
     * Admin role - has full system access
     */
    ADMIN
}
