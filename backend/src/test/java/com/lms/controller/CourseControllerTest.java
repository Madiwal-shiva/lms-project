package com.lms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.entity.Course;
import com.lms.entity.User;
import com.lms.enums.CourseStatus;
import com.lms.enums.Role;
import com.lms.service.CourseService;
import com.lms.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private Course testCourse;
    private User testInstructor;

    @BeforeEach
    void setUp() {
        testInstructor = new User();
        testInstructor.setId(1L);
        testInstructor.setFirstName("John");
        testInstructor.setLastName("Instructor");
        testInstructor.setEmail("instructor@test.com");
        testInstructor.setRole(Role.INSTRUCTOR);

        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setTitle("Test Course");
        testCourse.setDescription("Test Description");
        testCourse.setInstructor(testInstructor);
        testCourse.setStatus(CourseStatus.PUBLISHED);
    }

    @Test
    @WithMockUser
    void getAllCourses_ShouldReturnPageOfCourses() throws Exception {
        Page<Course> coursePage = new PageImpl<>(Arrays.asList(testCourse));
        when(courseService.getAllCourses(any())).thenReturn(coursePage);

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].title").value("Test Course"));
    }

    @Test
    @WithMockUser
    void getCourseById_ShouldReturnCourse_WhenCourseExists() throws Exception {
        when(courseService.findById(1L)).thenReturn(Optional.of(testCourse));

        mockMvc.perform(get("/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Course"));
    }

    @Test
    @WithMockUser
    void getCourseById_ShouldReturnNotFound_WhenCourseDoesNotExist() throws Exception {
        when(courseService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/courses/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "instructor@test.com")
    void createCourse_ShouldCreateCourse() throws Exception {
        when(userService.findByEmail("instructor@test.com")).thenReturn(Optional.of(testInstructor));
        when(courseService.createCourse(any(Course.class))).thenReturn(testCourse);

        mockMvc.perform(post("/courses")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCourse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Course"));
    }

    @Test
    @WithMockUser
    void publishCourse_ShouldPublishCourse() throws Exception {
        testCourse.setStatus(CourseStatus.PUBLISHED);
        when(courseService.publishCourse(1L)).thenReturn(testCourse);

        mockMvc.perform(put("/courses/1/publish")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PUBLISHED"));
    }

    @Test
    @WithMockUser
    void deleteCourse_ShouldDeleteCourse() throws Exception {
        mockMvc.perform(delete("/courses/1")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }
}