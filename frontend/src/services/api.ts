import axios from 'axios';
import type { 
  User, 
  Course, 
  Lesson, 
  Enrollment, 
  Assignment, 
  Submission,
  RegisterData,
  LoginData,
  ApiResponse,
  CourseFilters,
  DashboardStats
} from '../types';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add credentials for OAuth2
api.interceptors.request.use(
  (config) => {
    config.withCredentials = true;
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authAPI = {
  login: async (data: LoginData): Promise<ApiResponse<{ user: User; token: string }>> => {
    const response = await api.post('/auth/login', data);
    return response.data;
  },
  
  register: async (data: RegisterData): Promise<ApiResponse<{ user: User; token: string }>> => {
    const response = await api.post('/auth/register', data);
    return response.data;
  },
  
  getCurrentUser: async (): Promise<ApiResponse<User>> => {
    const response = await api.get('/auth/me');
    return response.data;
  },
  
  logout: async (): Promise<ApiResponse<null>> => {
    const response = await api.post('/auth/logout');
    return response.data;
  },
};

// Users API
export const usersAPI = {
  getUsers: async (): Promise<ApiResponse<User[]>> => {
    const response = await api.get('/users');
    return response.data;
  },
  
  getUser: async (id: string): Promise<ApiResponse<User>> => {
    const response = await api.get(`/users/${id}`);
    return response.data;
  },
  
  updateUser: async (id: string, data: Partial<User>): Promise<ApiResponse<User>> => {
    const response = await api.put(`/users/${id}`, data);
    return response.data;
  },
  
  deleteUser: async (id: string): Promise<ApiResponse<null>> => {
    const response = await api.delete(`/users/${id}`);
    return response.data;
  },
};

// Courses API
export const coursesAPI = {
  getCourses: async (filters?: CourseFilters): Promise<ApiResponse<Course[]>> => {
    const response = await api.get('/courses', { params: filters });
    return response.data;
  },
  
  getCourse: async (id: string): Promise<ApiResponse<Course>> => {
    const response = await api.get(`/courses/${id}`);
    return response.data;
  },
  
  createCourse: async (data: Partial<Course>): Promise<ApiResponse<Course>> => {
    const response = await api.post('/courses', data);
    return response.data;
  },
  
  updateCourse: async (id: string, data: Partial<Course>): Promise<ApiResponse<Course>> => {
    const response = await api.put(`/courses/${id}`, data);
    return response.data;
  },
  
  deleteCourse: async (id: string): Promise<ApiResponse<null>> => {
    const response = await api.delete(`/courses/${id}`);
    return response.data;
  },
  
  getInstructorCourses: async (instructorId: string): Promise<ApiResponse<Course[]>> => {
    const response = await api.get(`/courses/instructor/${instructorId}`);
    return response.data;
  },
};

// Lessons API
export const lessonsAPI = {
  getLessons: async (courseId: string): Promise<ApiResponse<Lesson[]>> => {
    const response = await api.get(`/courses/${courseId}/lessons`);
    return response.data;
  },
  
  getLesson: async (courseId: string, lessonId: string): Promise<ApiResponse<Lesson>> => {
    const response = await api.get(`/courses/${courseId}/lessons/${lessonId}`);
    return response.data;
  },
  
  createLesson: async (courseId: string, data: Partial<Lesson>): Promise<ApiResponse<Lesson>> => {
    const response = await api.post(`/courses/${courseId}/lessons`, data);
    return response.data;
  },
  
  updateLesson: async (courseId: string, lessonId: string, data: Partial<Lesson>): Promise<ApiResponse<Lesson>> => {
    const response = await api.put(`/courses/${courseId}/lessons/${lessonId}`, data);
    return response.data;
  },
  
  deleteLesson: async (courseId: string, lessonId: string): Promise<ApiResponse<null>> => {
    const response = await api.delete(`/courses/${courseId}/lessons/${lessonId}`);
    return response.data;
  },
};

// Enrollments API
export const enrollmentsAPI = {
  getEnrollments: async (): Promise<ApiResponse<Enrollment[]>> => {
    const response = await api.get('/enrollments');
    return response.data;
  },
  
  getStudentEnrollments: async (studentId: string): Promise<ApiResponse<Enrollment[]>> => {
    const response = await api.get(`/enrollments/student/${studentId}`);
    return response.data;
  },
  
  getCourseEnrollments: async (courseId: string): Promise<ApiResponse<Enrollment[]>> => {
    const response = await api.get(`/enrollments/course/${courseId}`);
    return response.data;
  },
  
  enrollInCourse: async (courseId: string): Promise<ApiResponse<Enrollment>> => {
    const response = await api.post('/enrollments', { courseId });
    return response.data;
  },
  
  updateProgress: async (enrollmentId: string, progress: number): Promise<ApiResponse<Enrollment>> => {
    const response = await api.put(`/enrollments/${enrollmentId}/progress`, { progress });
    return response.data;
  },
};

// Assignments API
export const assignmentsAPI = {
  getAssignments: async (courseId: string): Promise<ApiResponse<Assignment[]>> => {
    const response = await api.get(`/courses/${courseId}/assignments`);
    return response.data;
  },
  
  getAssignment: async (courseId: string, assignmentId: string): Promise<ApiResponse<Assignment>> => {
    const response = await api.get(`/courses/${courseId}/assignments/${assignmentId}`);
    return response.data;
  },
  
  createAssignment: async (courseId: string, data: Partial<Assignment>): Promise<ApiResponse<Assignment>> => {
    const response = await api.post(`/courses/${courseId}/assignments`, data);
    return response.data;
  },
  
  updateAssignment: async (courseId: string, assignmentId: string, data: Partial<Assignment>): Promise<ApiResponse<Assignment>> => {
    const response = await api.put(`/courses/${courseId}/assignments/${assignmentId}`, data);
    return response.data;
  },
  
  deleteAssignment: async (courseId: string, assignmentId: string): Promise<ApiResponse<null>> => {
    const response = await api.delete(`/courses/${courseId}/assignments/${assignmentId}`);
    return response.data;
  },
};

// Submissions API
export const submissionsAPI = {
  getSubmissions: async (assignmentId: string): Promise<ApiResponse<Submission[]>> => {
    const response = await api.get(`/assignments/${assignmentId}/submissions`);
    return response.data;
  },
  
  getSubmission: async (assignmentId: string, submissionId: string): Promise<ApiResponse<Submission>> => {
    const response = await api.get(`/assignments/${assignmentId}/submissions/${submissionId}`);
    return response.data;
  },
  
  submitAssignment: async (assignmentId: string, data: Partial<Submission>): Promise<ApiResponse<Submission>> => {
    const response = await api.post(`/assignments/${assignmentId}/submissions`, data);
    return response.data;
  },
  
  gradeSubmission: async (submissionId: string, grade: number, feedback?: string): Promise<ApiResponse<Submission>> => {
    const response = await api.put(`/submissions/${submissionId}/grade`, { grade, feedback });
    return response.data;
  },
};

// Dashboard API
export const dashboardAPI = {
  getStats: async (): Promise<ApiResponse<DashboardStats>> => {
    const response = await api.get('/dashboard/stats');
    return response.data;
  },
};

export default api;
