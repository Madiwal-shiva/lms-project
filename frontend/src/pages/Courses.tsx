import React, { useState, useEffect } from 'react';
import {
  Box,
  Grid,
  Card,
  CardContent,
  CardMedia,
  Typography,
  Button,
  TextField,
  Chip,
  CircularProgress,
  Alert
} from '@mui/material';
import { useAuth } from '../context/AuthContext';
import { api } from '../services/api';

interface Course {
  id: number;
  title: string;
  description: string;
  shortDescription: string;
  instructor: {
    id: number;
    firstName: string;
    lastName: string;
  };
  category: string;
  level: string;
  durationHours: number;
  price: number;
  rating: number;
  enrollmentCount: number;
  thumbnailUrl: string;
}

const Courses: React.FC = () => {
  const { user } = useAuth();
  const [courses, setCourses] = useState<Course[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchCourses();
  }, []);

  const fetchCourses = async () => {
    try {
      const response = await api.get('/courses/published');
      setCourses(response.data.content || []);
    } catch (err) {
      setError('Failed to fetch courses');
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    if (!searchTerm.trim()) {
      fetchCourses();
      return;
    }
    
    try {
      setLoading(true);
      const response = await api.get(`/courses/search?query=${searchTerm}`);
      setCourses(response.data.content || []);
    } catch (err) {
      setError('Failed to search courses');
    } finally {
      setLoading(false);
    }
  };

  const handleEnroll = async (courseId: number) => {
    try {
      await api.post(`/enrollments/enroll/${courseId}`);
      alert('Successfully enrolled in course!');
    } catch (err) {
      alert('Failed to enroll in course');
    }
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', p: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" gutterBottom>
        Available Courses
      </Typography>

      <Box sx={{ mb: 3, display: 'flex', gap: 2 }}>
        <TextField
          fullWidth
          placeholder="Search courses..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
        />
        <Button variant="contained" onClick={handleSearch}>
          Search
        </Button>
      </Box>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      <Grid container spacing={3}>
        {courses.map((course) => (
          <Grid item xs={12} sm={6} md={4} key={course.id}>
            <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
              <CardMedia
                component="img"
                height="200"
                image={course.thumbnailUrl || '/api/placeholder/400/200'}
                alt={course.title}
              />
              <CardContent sx={{ flexGrow: 1 }}>
                <Typography variant="h6" gutterBottom>
                  {course.title}
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  {course.shortDescription || course.description.substring(0, 100) + '...'}
                </Typography>
                <Box sx={{ mb: 2 }}>
                  <Chip label={course.category} size="small" sx={{ mr: 1 }} />
                  <Chip label={course.level} size="small" variant="outlined" />
                </Box>
                <Typography variant="body2" sx={{ mb: 1 }}>
                  Instructor: {course.instructor.firstName} {course.instructor.lastName}
                </Typography>
                <Typography variant="body2" sx={{ mb: 1 }}>
                  Duration: {course.durationHours} hours
                </Typography>
                <Typography variant="body2" sx={{ mb: 1 }}>
                  Students: {course.enrollmentCount}
                </Typography>
                {course.rating && (
                  <Typography variant="body2" sx={{ mb: 2 }}>
                    Rating: {course.rating.toFixed(1)}/5
                  </Typography>
                )}
              </CardContent>
              <Box sx={{ p: 2 }}>
                <Button
                  fullWidth
                  variant="contained"
                  onClick={() => handleEnroll(course.id)}
                  disabled={!user}
                >
                  Enroll Now
                </Button>
              </Box>
            </Card>
          </Grid>
        ))}
      </Grid>

      {courses.length === 0 && !loading && (
        <Typography variant="body1" sx={{ textAlign: 'center', mt: 4 }}>
          No courses found.
        </Typography>
      )}
    </Box>
  );
};

export default Courses;