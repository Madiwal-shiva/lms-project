import React, { useState, useEffect } from 'react';
import {
  Box,
  Grid,
  Card,
  CardContent,
  Typography,
  Button,
  LinearProgress,
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
  instructor: {
    firstName: string;
    lastName: string;
  };
  category: string;
  durationHours: number;
  thumbnailUrl: string;
}

const MyCourses: React.FC = () => {
  const { user } = useAuth();
  const [courses, setCourses] = useState<Course[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (user) {
      fetchMyCourses();
    }
  }, [user]);

  const fetchMyCourses = async () => {
    try {
      const response = await api.get('/enrollments/my-courses');
      setCourses(response.data || []);
    } catch (err) {
      setError('Failed to fetch your courses');
    } finally {
      setLoading(false);
    }
  };

  const handleUnenroll = async (courseId: number) => {
    if (window.confirm('Are you sure you want to unenroll from this course?')) {
      try {
        await api.delete(`/enrollments/unenroll/${courseId}`);
        setCourses(courses.filter(course => course.id !== courseId));
        alert('Successfully unenrolled from course');
      } catch (err) {
        alert('Failed to unenroll from course');
      }
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
        My Courses
      </Typography>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      <Grid container spacing={3}>
        {courses.map((course) => (
          <Grid item xs={12} sm={6} md={4} key={course.id}>
            <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
              <CardContent sx={{ flexGrow: 1 }}>
                <Typography variant="h6" gutterBottom>
                  {course.title}
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  {course.description.substring(0, 100)}...
                </Typography>
                <Box sx={{ mb: 2 }}>
                  <Chip label={course.category} size="small" />
                </Box>
                <Typography variant="body2" sx={{ mb: 1 }}>
                  Instructor: {course.instructor.firstName} {course.instructor.lastName}
                </Typography>
                <Typography variant="body2" sx={{ mb: 2 }}>
                  Duration: {course.durationHours} hours
                </Typography>
                <Box sx={{ mb: 2 }}>
                  <Typography variant="body2" sx={{ mb: 1 }}>
                    Progress: 0%
                  </Typography>
                  <LinearProgress variant="determinate" value={0} />
                </Box>
              </CardContent>
              <Box sx={{ p: 2, display: 'flex', gap: 1 }}>
                <Button
                  variant="contained"
                  size="small"
                  sx={{ flex: 1 }}
                >
                  Continue
                </Button>
                <Button
                  variant="outlined"
                  size="small"
                  color="error"
                  onClick={() => handleUnenroll(course.id)}
                >
                  Unenroll
                </Button>
              </Box>
            </Card>
          </Grid>
        ))}
      </Grid>

      {courses.length === 0 && !loading && (
        <Box sx={{ textAlign: 'center', mt: 4 }}>
          <Typography variant="body1" sx={{ mb: 2 }}>
            You haven't enrolled in any courses yet.
          </Typography>
          <Button variant="contained" href="/courses">
            Browse Courses
          </Button>
        </Box>
      )}
    </Box>
  );
};

export default MyCourses;