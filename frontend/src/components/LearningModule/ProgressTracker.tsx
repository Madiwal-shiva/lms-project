import React from 'react';
import {
  Box,
  Typography,
  LinearProgress,
  Card,
  CardContent,
  Grid,
  Chip,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  CircularProgress,
  Tooltip,
  Paper,
} from '@mui/material';
import {
  CheckCircle,
  RadioButtonUnchecked,
  Timer,
  Quiz,
  TrendingUp,
  School,
  Assignment,
  Star,
  EmojiEvents,
} from '@mui/icons-material';
import { LearningModule, StudentProgress } from '../../types/learningModule';

interface ProgressTrackerProps {
  module: LearningModule;
  progress: StudentProgress;
  showDetailed?: boolean;
}

const ProgressTracker: React.FC<ProgressTrackerProps> = ({
  module,
  progress,
  showDetailed = false,
}) => {
  // Calculate progress metrics
  const totalSections = module.sections.length;
  const completedSections = Math.max(0, progress.currentSection);
  const sectionProgress = totalSections > 0 ? (completedSections / totalSections) * 100 : 0;

  const totalObjectives = module.learningObjectives.length;
  const completedObjectives = progress.completedObjectives.length;
  const objectiveProgress = totalObjectives > 0 ? (completedObjectives / totalObjectives) * 100 : 0;

  const quizCount = module.sections.filter(section => section.quiz && section.quiz.length > 0).length;
  const completedQuizzes = Object.keys(progress.quizScores).length;
  const quizProgress = quizCount > 0 ? (completedQuizzes / quizCount) * 100 : 0;

  const averageQuizScore = completedQuizzes > 0 
    ? Object.values(progress.quizScores).reduce((sum, score) => sum + score, 0) / completedQuizzes
    : 0;

  const overallProgress = (sectionProgress + objectiveProgress + quizProgress) / 3;

  const formatTime = (seconds: number) => {
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    
    if (hours > 0) {
      return `${hours}h ${minutes}m`;
    }
    return `${minutes}m`;
  };

  const getProgressColor = (percentage: number) => {
    if (percentage >= 80) return 'success';
    if (percentage >= 60) return 'warning';
    return 'error';
  };

  const getGradeLevel = (score: number) => {
    if (score >= 90) return { grade: 'A', color: 'success.main', icon: '🏆' };
    if (score >= 80) return { grade: 'B', color: 'info.main', icon: '⭐' };
    if (score >= 70) return { grade: 'C', color: 'warning.main', icon: '👍' };
    if (score >= 60) return { grade: 'D', color: 'orange', icon: '📚' };
    return { grade: 'F', color: 'error.main', icon: '💪' };
  };

  const gradeInfo = getGradeLevel(averageQuizScore);

  if (!showDetailed) {
    // Compact progress view
    return (
      <Box sx={{ position: 'fixed', bottom: 16, left: 16, zIndex: 1000 }}>
        <Card sx={{ minWidth: 200, maxWidth: 300 }}>
          <CardContent sx={{ pb: 2 }}>
            <Typography variant="body2" color="text.secondary" gutterBottom>
              Overall Progress
            </Typography>
            <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
              <Box sx={{ width: '100%', mr: 1 }}>
                <LinearProgress
                  variant="determinate"
                  value={overallProgress}
                  color={getProgressColor(overallProgress)}
                  sx={{ height: 6, borderRadius: 3 }}
                />
              </Box>
              <Typography variant="body2" color="text.secondary">
                {Math.round(overallProgress)}%
              </Typography>
            </Box>
            <Typography variant="caption" color="text.secondary">
              Time: {formatTime(progress.timeSpent)}
            </Typography>
          </CardContent>
        </Card>
      </Box>
    );
  }

  // Detailed progress view
  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h6" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
        <TrendingUp /> Learning Progress
      </Typography>

      <Grid container spacing={3}>
        {/* Overall Progress */}
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Overall Progress
              </Typography>
              <Box sx={{ position: 'relative', display: 'inline-flex', mb: 2 }}>
                <CircularProgress
                  variant="determinate"
                  value={overallProgress}
                  size={100}
                  thickness={4}
                  color={getProgressColor(overallProgress)}
                />
                <Box
                  sx={{
                    top: 0,
                    left: 0,
                    bottom: 0,
                    right: 0,
                    position: 'absolute',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                  }}
                >
                  <Typography variant="h6" component="div" color="text.secondary">
                    {Math.round(overallProgress)}%
                  </Typography>
                </Box>
              </Box>
              <Typography variant="body2" color="text.secondary">
                {overallProgress >= 100 ? '🎉 Module Complete!' : 'Keep going!'}
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        {/* Time Tracking */}
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                <Timer /> Time Spent
              </Typography>
              <Typography variant="h4" color="primary">
                {formatTime(progress.timeSpent)}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Estimated: {module.estimatedDuration} minutes
              </Typography>
              <LinearProgress
                variant="determinate"
                value={Math.min((progress.timeSpent / 60) / module.estimatedDuration * 100, 100)}
                sx={{ mt: 1 }}
              />
            </CardContent>
          </Card>
        </Grid>

        {/* Section Progress */}
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                <School /> Sections
              </Typography>
              <Typography variant="h4" color="primary">
                {completedSections}/{totalSections}
              </Typography>
              <LinearProgress
                variant="determinate"
                value={sectionProgress}
                color={getProgressColor(sectionProgress)}
                sx={{ mt: 1, height: 6, borderRadius: 3 }}
              />
              <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
                {Math.round(sectionProgress)}% complete
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        {/* Objectives Progress */}
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                <Assignment /> Objectives
              </Typography>
              <Typography variant="h4" color="primary">
                {completedObjectives}/{totalObjectives}
              </Typography>
              <LinearProgress
                variant="determinate"
                value={objectiveProgress}
                color={getProgressColor(objectiveProgress)}
                sx={{ mt: 1, height: 6, borderRadius: 3 }}
              />
              <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
                {Math.round(objectiveProgress)}% complete
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        {/* Quiz Performance */}
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                <Quiz /> Quiz Performance
              </Typography>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 1 }}>
                <Typography variant="h4" color={gradeInfo.color}>
                  {averageQuizScore > 0 ? Math.round(averageQuizScore) : '--'}%
                </Typography>
                <Typography variant="h5">{gradeInfo.icon}</Typography>
              </Box>
              <Chip
                label={`Grade: ${gradeInfo.grade}`}
                color={getProgressColor(averageQuizScore)}
                size="small"
                sx={{ mb: 1 }}
              />
              <Typography variant="body2" color="text.secondary">
                {completedQuizzes}/{quizCount} quizzes completed
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        {/* Learning Objectives List */}
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Learning Objectives
              </Typography>
              <List dense>
                {module.learningObjectives.map((objective) => (
                  <ListItem key={objective.id} sx={{ px: 0 }}>
                    <ListItemIcon sx={{ minWidth: 32 }}>
                      {progress.completedObjectives.includes(objective.id) ? (
                        <CheckCircle color="success" />
                      ) : (
                        <RadioButtonUnchecked color="disabled" />
                      )}
                    </ListItemIcon>
                    <ListItemText
                      primary={objective.title}
                      secondary={objective.description}
                      primaryTypographyProps={{
                        style: {
                          textDecoration: progress.completedObjectives.includes(objective.id) 
                            ? 'line-through' 
                            : 'none',
                        },
                      }}
                    />
                  </ListItem>
                ))}
              </List>
            </CardContent>
          </Card>
        </Grid>

        {/* Quiz Scores Breakdown */}
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Quiz Scores
              </Typography>
              {Object.keys(progress.quizScores).length === 0 ? (
                <Typography variant="body2" color="text.secondary">
                  No quizzes completed yet
                </Typography>
              ) : (
                <List dense>
                  {module.sections
                    .filter(section => section.quiz && section.quiz.length > 0)
                    .map((section) => {
                      const score = progress.quizScores[section.id];
                      const hasScore = score !== undefined;
                      
                      return (
                        <ListItem key={section.id} sx={{ px: 0 }}>
                          <ListItemIcon sx={{ minWidth: 32 }}>
                            {hasScore ? <CheckCircle color="success" /> : <Quiz color="disabled" />}
                          </ListItemIcon>
                          <ListItemText
                            primary={section.title}
                            secondary={hasScore ? `Score: ${score}%` : 'Not completed'}
                          />
                          {hasScore && (
                            <Chip
                              label={getGradeLevel(score).grade}
                              color={getProgressColor(score)}
                              size="small"
                            />
                          )}
                        </ListItem>
                      );
                    })}
                </List>
              )}
            </CardContent>
          </Card>
        </Grid>

        {/* Study Statistics */}
        <Grid item xs={12}>
          <Paper sx={{ p: 2, bgcolor: 'action.hover' }}>
            <Typography variant="h6" gutterBottom>
              Study Statistics
            </Typography>
            <Grid container spacing={2}>
              <Grid item xs={6} sm={3}>
                <Box sx={{ textAlign: 'center' }}>
                  <Typography variant="h6" color="primary">
                    {progress.notes.length}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Notes Taken
                  </Typography>
                </Box>
              </Grid>
              <Grid item xs={6} sm={3}>
                <Box sx={{ textAlign: 'center' }}>
                  <Typography variant="h6" color="primary">
                    {progress.bookmarks.length}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Bookmarks
                  </Typography>
                </Box>
              </Grid>
              <Grid item xs={6} sm={3}>
                <Box sx={{ textAlign: 'center' }}>
                  <Typography variant="h6" color="primary">
                    {Math.round((progress.timeSpent / 60) / module.estimatedDuration * 100)}%
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Time Efficiency
                  </Typography>
                </Box>
              </Grid>
              <Grid item xs={6} sm={3}>
                <Box sx={{ textAlign: 'center' }}>
                  <Typography variant="h6" color="primary">
                    {progress.lastAccessed.toLocaleDateString()}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Last Accessed
                  </Typography>
                </Box>
              </Grid>
            </Grid>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

export default ProgressTracker;
