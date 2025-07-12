import React, { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  Card,
  CardContent,
  Container,
  Grid,
  Button,
  CircularProgress,
} from '@mui/material';
import { School, People, TrendingUp, Add } from '@mui/icons-material';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import { api } from '../services/api';

const SimpleDashboard: React.FC = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [stats, setStats] = useState({
    totalCourses: 0,
    enrolledCourses: 0,
    totalStudents: 0
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      if (user?.role === 'INSTRUCTOR') {
        // Fetch instructor stats
        const coursesResponse = await api.get('/courses/statistics');
        setStats({
          totalCourses: coursesResponse.data.totalCourses || 0,
          enrolledCourses: 0,
          totalStudents: coursesResponse.data.totalEnrollments || 0
        });
      } else {
        // Fetch student stats
        const enrollmentsResponse = await api.get('/enrollments/my-courses');
        setStats({
          totalCourses: 0,
          enrolledCourses: enrollmentsResponse.data.length || 0,
          totalStudents: 0
        });
      }
    } catch (error) {
      console.error('Error fetching stats:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="lg">
      <Box sx={{ py: 4 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
          <Typography variant="h4" component="h1">
            Welcome back, {user?.firstName} {user?.lastName}!
          </Typography>
          {user?.role === 'INSTRUCTOR' && (
            <Button
              variant="contained"
              startIcon={<Add />}
              onClick={() => navigate('/create-course')}
            >
              Create Course
            </Button>
          )}
        </Box>
        
        {loading ? (
          <Box sx={{ display: 'flex', justifyContent: 'center', p: 4 }}>
            <CircularProgress />
          </Box>
        ) : (
          <Grid container spacing={3} sx={{ mb: 4 }}>
            {user?.role === 'INSTRUCTOR' ? (
              <>
                <Grid item xs={12} sm={6} md={4}>
                  <Card>
                    <CardContent>
                      <Box sx={{ display: 'flex', alignItems: 'center' }}>
                        <School sx={{ fontSize: 40, color: 'primary.main', mr: 2 }} />
                        <Box>
                          <Typography variant="h5" component="div">
                            {stats.totalCourses}
                          </Typography>
                          <Typography variant="body2" color="text.secondary">
                            My Courses
                          </Typography>
                        </Box>
                      </Box>
                    </CardContent>
                  </Card>
                </Grid>
                <Grid item xs={12} sm={6} md={4}>
                  <Card>
                    <CardContent>
                      <Box sx={{ display: 'flex', alignItems: 'center' }}>
                        <People sx={{ fontSize: 40, color: 'success.main', mr: 2 }} />
                        <Box>
                          <Typography variant="h5" component="div">
                            {stats.totalStudents}
                          </Typography>
                          <Typography variant="body2" color="text.secondary">
                            Total Enrollments
                          </Typography>
                        </Box>
                      </Box>
                    </CardContent>
                  </Card>
                </Grid>
              </>
            ) : (
              <>
                <Grid item xs={12} sm={6} md={4}>
                  <Card>
                    <CardContent>
                      <Box sx={{ display: 'flex', alignItems: 'center' }}>
                        <School sx={{ fontSize: 40, color: 'primary.main', mr: 2 }} />
                        <Box>
                          <Typography variant="h5" component="div">
                            {stats.enrolledCourses}
                          </Typography>
                          <Typography variant="body2" color="text.secondary">
                            Enrolled Courses
                          </Typography>
                        </Box>
                      </Box>
                    </CardContent>
                  </Card>
                </Grid>
              </>
            )}
            <Grid item xs={12} sm={6} md={4}>
              <Card>
                <CardContent>
                  <Box sx={{ display: 'flex', alignItems: 'center' }}>
                    <TrendingUp sx={{ fontSize: 40, color: 'warning.main', mr: 2 }} />
                    <Box>
                      <Typography variant="h5" component="div">
                        0%
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        Average Progress
                      </Typography>
                    </Box>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          </Grid>
        )}
        
        <Grid container spacing={3}>
          <Grid item xs={12} md={6}>
            <Card>
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  Quick Actions
                </Typography>
                <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                  {user?.role === 'INSTRUCTOR' ? (
                    <>
                      <Button variant="outlined" onClick={() => navigate('/create-course')}>
                        Create New Course
                      </Button>
                      <Button variant="outlined" onClick={() => navigate('/my-courses')}>
                        Manage My Courses
                      </Button>
                    </>
                  ) : (
                    <>
                      <Button variant="outlined" onClick={() => navigate('/courses')}>
                        Browse Courses
                      </Button>
                      <Button variant="outlined" onClick={() => navigate('/my-courses')}>
                        My Enrolled Courses
                      </Button>
                    </>
                  )}
                </Box>
              </CardContent>
            </Card>
          </Grid>
          <Grid item xs={12} md={6}>
            <Card>
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  Getting Started
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  {user?.role === 'INSTRUCTOR' 
                    ? 'Start by creating your first course and sharing your knowledge with students worldwide.'
                    : 'Explore our course catalog and start your learning journey today!'}
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      </Box>
    </Container>
  );
};

export default SimpleDashboard;
