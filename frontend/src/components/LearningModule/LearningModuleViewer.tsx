import React, { useState, useEffect, useCallback } from 'react';
import {
  Box,
  Container,
  Grid,
  Paper,
  Typography,
  LinearProgress,
  Chip,
  IconButton,
  Tooltip,
  Fab,
  Drawer,
  List,
  ListItem,
  ListItemText,
  ListItemIcon,
  Divider,
  Card,
  CardContent,
  CardActions,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Alert,
  Collapse,
} from '@mui/material';
import {
  PlayArrow,
  Pause,
  NavigateNext,
  NavigateBefore,
  Bookmark,
  BookmarkBorder,
  Notes,
  MenuBook,
  CheckCircle,
  RadioButtonUnchecked,
  Timer,
  Quiz,
  Code,
  VideoLibrary,
  Psychology,
  School,
  Assignment,
  ExpandMore,
  ExpandLess,
  Fullscreen,
  FullscreenExit,
} from '@mui/icons-material';
import { LearningModule, LearningSection, StudentProgress, ModuleSettings } from '../../types/learningModule';
import ContentRenderer from './ContentRenderer';
import QuizComponent from './QuizComponent';
import ProgressTracker from './ProgressTracker';
import NotesPanel from './NotesPanel';
import ResourcePanel from './ResourcePanel';

interface LearningModuleViewerProps {
  module: LearningModule;
  studentProgress?: StudentProgress;
  settings?: ModuleSettings;
  onProgressUpdate?: (progress: Partial<StudentProgress>) => void;
  onComplete?: (finalScore: number) => void;
}

const LearningModuleViewer: React.FC<LearningModuleViewerProps> = ({
  module,
  studentProgress,
  settings = {
    allowSkipping: true,
    requireSequentialProgress: false,
    showProgress: true,
    enableNotes: true,
    enableBookmarks: true,
    timeTracking: true,
  },
  onProgressUpdate,
  onComplete,
}) => {
  const [currentSectionIndex, setCurrentSectionIndex] = useState(
    studentProgress?.currentSection || 0
  );
  const [currentContentIndex, setCurrentContentIndex] = useState(0);
  const [isPlaying, setIsPlaying] = useState(false);
  const [timeSpent, setTimeSpent] = useState(studentProgress?.timeSpent || 0);
  const [bookmarks, setBookmarks] = useState<string[]>(studentProgress?.bookmarks || []);
  const [notes, setNotes] = useState<string[]>(studentProgress?.notes || []);
  const [showNotes, setShowNotes] = useState(false);
  const [showResources, setShowResources] = useState(false);
  const [showObjectives, setShowObjectives] = useState(false);
  const [isFullscreen, setIsFullscreen] = useState(false);
  const [completedObjectives, setCompletedObjectives] = useState<string[]>(
    studentProgress?.completedObjectives || []
  );
  const [quizScores, setQuizScores] = useState<Record<string, number>>(
    studentProgress?.quizScores || {}
  );

  const currentSection = module.sections[currentSectionIndex];
  const currentContent = currentSection?.content[currentContentIndex];
  const totalSections = module.sections.length;
  const sectionProgress = currentSection ? (currentContentIndex + 1) / currentSection.content.length : 0;
  const moduleProgress = (currentSectionIndex + sectionProgress) / totalSections;

  // Timer for tracking time spent
  useEffect(() => {
    let interval: NodeJS.Timeout | null = null;

    if (isPlaying && settings.timeTracking) {
      interval = setInterval(() => {
        setTimeSpent((prev) => {
          const newTime = prev + 1;
          if (onProgressUpdate) {
            onProgressUpdate({ timeSpent: newTime });
          }
          return newTime;
        });
      }, 1000);
    }

    return () => {
      if (interval) clearInterval(interval);
    };
  }, [isPlaying, settings.timeTracking, onProgressUpdate]);

  // Auto-save progress
  useEffect(() => {
    if (onProgressUpdate) {
      onProgressUpdate({
        currentSection: currentSectionIndex,
        completedObjectives,
        quizScores,
        timeSpent,
        notes,
        bookmarks,
        lastAccessed: new Date(),
      });
    }
  }, [
    currentSectionIndex,
    completedObjectives,
    quizScores,
    timeSpent,
    notes,
    bookmarks,
    onProgressUpdate,
  ]);

  const handleNext = useCallback(() => {
    if (currentContentIndex < currentSection.content.length - 1) {
      setCurrentContentIndex(prev => prev + 1);
    } else if (currentSectionIndex < totalSections - 1) {
      setCurrentSectionIndex(prev => prev + 1);
      setCurrentContentIndex(0);
    }
  }, [currentContentIndex, currentSection, currentSectionIndex, totalSections]);

  const handlePrevious = useCallback(() => {
    if (currentContentIndex > 0) {
      setCurrentContentIndex(prev => prev - 1);
    } else if (currentSectionIndex > 0) {
      setCurrentSectionIndex(prev => prev - 1);
      const prevSection = module.sections[currentSectionIndex - 1];
      setCurrentContentIndex(prevSection.content.length - 1);
    }
  }, [currentContentIndex, currentSectionIndex, module.sections]);

  const handleBookmark = () => {
    const bookmarkId = `${currentSection.id}-${currentContent.id}`;
    setBookmarks(prev => {
      const newBookmarks = prev.includes(bookmarkId)
        ? prev.filter(id => id !== bookmarkId)
        : [...prev, bookmarkId];
      return newBookmarks;
    });
  };

  const handleObjectiveComplete = (objectiveId: string) => {
    setCompletedObjectives(prev => {
      if (prev.includes(objectiveId)) {
        return prev.filter(id => id !== objectiveId);
      }
      return [...prev, objectiveId];
    });
  };

  const handleQuizComplete = (sectionId: string, score: number) => {
    setQuizScores(prev => ({
      ...prev,
      [sectionId]: score,
    }));
  };

  const isCurrentBookmarked = () => {
    const bookmarkId = `${currentSection.id}-${currentContent.id}`;
    return bookmarks.includes(bookmarkId);
  };

  const formatTime = (seconds: number) => {
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    const secs = seconds % 60;
    
    if (hours > 0) {
      return `${hours}h ${minutes}m ${secs}s`;
    }
    return `${minutes}m ${secs}s`;
  };

  const getCompletionPercentage = () => {
    const totalObjectives = module.learningObjectives.length;
    const completedCount = completedObjectives.length;
    return totalObjectives > 0 ? (completedCount / totalObjectives) * 100 : 0;
  };

  const getSectionIcon = (section: LearningSection) => {
    const hasQuiz = section.quiz && section.quiz.length > 0;
    const hasVideo = section.content.some(content => content.type === 'video');
    const hasCode = section.content.some(content => content.type === 'code');
    const hasInteractive = section.content.some(content => content.type === 'interactive');

    if (hasQuiz) return <Quiz />;
    if (hasCode) return <Code />;
    if (hasVideo) return <VideoLibrary />;
    if (hasInteractive) return <Psychology />;
    return <MenuBook />;
  };

  return (
    <Box sx={{ minHeight: '100vh', bgcolor: 'grey.50' }}>
      {/* Header */}
      <Paper elevation={2} sx={{ mb: 2 }}>
        <Container maxWidth="xl">
          <Box sx={{ py: 2 }}>
            <Grid container alignItems="center" spacing={2}>
              <Grid item xs>
                <Typography variant="h5" component="h1" gutterBottom>
                  {module.title}
                </Typography>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, flexWrap: 'wrap' }}>
                  <Chip 
                    icon={<School />}
                    label={module.subject}
                    size="small"
                    color="primary"
                  />
                  <Chip 
                    label={module.level}
                    size="small"
                    color="secondary"
                  />
                  <Chip 
                    icon={<Timer />}
                    label={`${module.estimatedDuration} min`}
                    size="small"
                    variant="outlined"
                  />
                  {settings.timeTracking && (
                    <Chip 
                      icon={<Timer />}
                      label={`Spent: ${formatTime(timeSpent)}`}
                      size="small"
                      color="info"
                    />
                  )}
                </Box>
              </Grid>
              
              <Grid item>
                <Box sx={{ display: 'flex', gap: 1 }}>
                  <Tooltip title="Learning Objectives">
                    <IconButton
                      onClick={() => setShowObjectives(true)}
                      color={getCompletionPercentage() === 100 ? 'success' : 'default'}
                    >
                      <Assignment />
                    </IconButton>
                  </Tooltip>
                  
                  {settings.enableNotes && (
                    <Tooltip title="Notes">
                      <IconButton onClick={() => setShowNotes(true)}>
                        <Notes />
                      </IconButton>
                    </Tooltip>
                  )}
                  
                  <Tooltip title="Resources">
                    <IconButton onClick={() => setShowResources(true)}>
                      <MenuBook />
                    </IconButton>
                  </Tooltip>
                  
                  <Tooltip title={isFullscreen ? 'Exit Fullscreen' : 'Fullscreen'}>
                    <IconButton onClick={() => setIsFullscreen(!isFullscreen)}>
                      {isFullscreen ? <FullscreenExit /> : <Fullscreen />}
                    </IconButton>
                  </Tooltip>
                </Box>
              </Grid>
            </Grid>

            {/* Progress Bar */}
            {settings.showProgress && (
              <Box sx={{ mt: 2 }}>
                <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 1 }}>
                  <Typography variant="body2" color="text.secondary">
                    Section {currentSectionIndex + 1} of {totalSections}: {currentSection?.title}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {Math.round(moduleProgress * 100)}% Complete
                  </Typography>
                </Box>
                <LinearProgress 
                  variant="determinate" 
                  value={moduleProgress * 100}
                  sx={{ height: 8, borderRadius: 4 }}
                />
              </Box>
            )}
          </Box>
        </Container>
      </Paper>

      {/* Main Content */}
      <Container maxWidth="xl">
        <Grid container spacing={3}>
          {/* Section Navigation */}
          <Grid item xs={12} md={3}>
            <Paper sx={{ p: 2, position: 'sticky', top: 16 }}>
              <Typography variant="h6" gutterBottom>
                Course Content
              </Typography>
              <List dense>
                {module.sections.map((section, index) => (
                  <ListItem
                    key={section.id}
                    button
                    selected={index === currentSectionIndex}
                    onClick={() => {
                      if (settings.allowSkipping || index <= currentSectionIndex + 1) {
                        setCurrentSectionIndex(index);
                        setCurrentContentIndex(0);
                      }
                    }}
                    disabled={settings.requireSequentialProgress && index > currentSectionIndex + 1}
                  >
                    <ListItemIcon>
                      {getSectionIcon(section)}
                    </ListItemIcon>
                    <ListItemText
                      primary={section.title}
                      secondary={`${section.estimatedTime} min`}
                    />
                    {quizScores[section.id] && (
                      <CheckCircle color="success" fontSize="small" />
                    )}
                  </ListItem>
                ))}
              </List>
            </Paper>
          </Grid>

          {/* Content Area */}
          <Grid item xs={12} md={9}>
            <Paper sx={{ minHeight: '70vh', position: 'relative' }}>
              {currentContent && (
                <ContentRenderer
                  content={currentContent}
                  onInteraction={() => setIsPlaying(true)}
                  isFullscreen={isFullscreen}
                />
              )}

              {/* Navigation Controls */}
              <Box
                sx={{
                  position: 'absolute',
                  bottom: 16,
                  right: 16,
                  display: 'flex',
                  gap: 1,
                }}
              >
                {settings.enableBookmarks && (
                  <Tooltip title={isCurrentBookmarked() ? 'Remove Bookmark' : 'Add Bookmark'}>
                    <IconButton
                      onClick={handleBookmark}
                      color={isCurrentBookmarked() ? 'primary' : 'default'}
                    >
                      {isCurrentBookmarked() ? <Bookmark /> : <BookmarkBorder />}
                    </IconButton>
                  </Tooltip>
                )}

                <Tooltip title="Previous">
                  <span>
                    <IconButton
                      onClick={handlePrevious}
                      disabled={currentSectionIndex === 0 && currentContentIndex === 0}
                    >
                      <NavigateBefore />
                    </IconButton>
                  </span>
                </Tooltip>

                <Tooltip title={isPlaying ? 'Pause' : 'Play'}>
                  <IconButton
                    onClick={() => setIsPlaying(!isPlaying)}
                    color="primary"
                  >
                    {isPlaying ? <Pause /> : <PlayArrow />}
                  </IconButton>
                </Tooltip>

                <Tooltip title="Next">
                  <span>
                    <IconButton
                      onClick={handleNext}
                      disabled={
                        currentSectionIndex === totalSections - 1 &&
                        currentContentIndex === currentSection?.content.length - 1
                      }
                    >
                      <NavigateNext />
                    </IconButton>
                  </span>
                </Tooltip>
              </Box>
            </Paper>

            {/* Section Quiz */}
            {currentSection?.quiz && currentContentIndex === currentSection.content.length - 1 && (
              <Box sx={{ mt: 2 }}>
                <QuizComponent
                  questions={currentSection.quiz}
                  onComplete={(score) => handleQuizComplete(currentSection.id, score)}
                  previousScore={quizScores[currentSection.id]}
                />
              </Box>
            )}
          </Grid>
        </Grid>
      </Container>

      {/* Learning Objectives Dialog */}
      <Dialog
        open={showObjectives}
        onClose={() => setShowObjectives(false)}
        maxWidth="md"
        fullWidth
      >
        <DialogTitle>
          Learning Objectives
          <Typography variant="body2" color="text.secondary">
            {completedObjectives.length} of {module.learningObjectives.length} completed
          </Typography>
        </DialogTitle>
        <DialogContent>
          <LinearProgress
            variant="determinate"
            value={getCompletionPercentage()}
            sx={{ mb: 2 }}
          />
          <List>
            {module.learningObjectives.map((objective) => (
              <ListItem
                key={objective.id}
                button
                onClick={() => handleObjectiveComplete(objective.id)}
              >
                <ListItemIcon>
                  {completedObjectives.includes(objective.id) ? (
                    <CheckCircle color="success" />
                  ) : (
                    <RadioButtonUnchecked />
                  )}
                </ListItemIcon>
                <ListItemText
                  primary={objective.title}
                  secondary={objective.description}
                />
              </ListItem>
            ))}
          </List>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setShowObjectives(false)}>Close</Button>
        </DialogActions>
      </Dialog>

      {/* Notes Panel */}
      <NotesPanel
        open={showNotes}
        onClose={() => setShowNotes(false)}
        notes={notes}
        onNotesChange={setNotes}
      />

      {/* Resources Panel */}
      <ResourcePanel
        open={showResources}
        onClose={() => setShowResources(false)}
        resources={module.resources}
      />

      {/* Progress Tracker */}
      <ProgressTracker
        module={module}
        progress={{
          moduleId: module.id,
          studentId: 'current-student', // Replace with actual student ID
          completedObjectives,
          currentSection: currentSectionIndex,
          quizScores,
          timeSpent,
          lastAccessed: new Date(),
          notes,
          bookmarks,
        }}
      />
    </Box>
  );
};

export default LearningModuleViewer;
