package com.lms.dto;

import jakarta.validation.constraints.*;

public class CourseCreateDTO {

    @NotBlank(message = "Course title is required")
    @Size(max = 200, message = "Course title must not exceed 200 characters")
    private String title;

    @NotBlank(message = "Course description is required")
    @Size(max = 5000, message = "Course description must not exceed 5000 characters")
    private String description;

    @Size(max = 500, message = "Short description must not exceed 500 characters")
    private String shortDescription;

    @NotBlank(message = "Category is required")
    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;

    @NotBlank(message = "Level is required")
    @Pattern(regexp = "^(Beginner|Intermediate|Advanced)$", message = "Level must be Beginner, Intermediate, or Advanced")
    private String level;

    @Size(max = 50, message = "Language must not exceed 50 characters")
    private String language = "English";

    @Min(value = 1, message = "Duration must be at least 1 hour")
    @Max(value = 1000, message = "Duration must not exceed 1000 hours")
    private Integer durationHours;

    @DecimalMin(value = "0.0", message = "Price must be non-negative")
    @DecimalMax(value = "99999.99", message = "Price must not exceed 99999.99")
    private Double price;

    @Min(value = 1, message = "Maximum students must be at least 1")
    @Max(value = 10000, message = "Maximum students must not exceed 10000")
    private Integer maxStudents;

    @Size(max = 2000, message = "Prerequisites must not exceed 2000 characters")
    private String prerequisites;

    @Size(max = 2000, message = "Learning objectives must not exceed 2000 characters")
    private String learningObjectives;

    @Size(max = 500, message = "Tags must not exceed 500 characters")
    private String tags;

    public CourseCreateDTO() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public Integer getDurationHours() { return durationHours; }
    public void setDurationHours(Integer durationHours) { this.durationHours = durationHours; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getMaxStudents() { return maxStudents; }
    public void setMaxStudents(Integer maxStudents) { this.maxStudents = maxStudents; }

    public String getPrerequisites() { return prerequisites; }
    public void setPrerequisites(String prerequisites) { this.prerequisites = prerequisites; }

    public String getLearningObjectives() { return learningObjectives; }
    public void setLearningObjectives(String learningObjectives) { this.learningObjectives = learningObjectives; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
}