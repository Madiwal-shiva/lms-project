import React, { useState, useEffect } from 'react';
import {
  Box,
  Card,
  CardContent,
  CardActions,
  Typography,
  Button,
  Radio,
  RadioGroup,
  FormControlLabel,
  FormControl,
  FormLabel,
  Checkbox,
  FormGroup,
  TextField,
  Alert,
  LinearProgress,
  Chip,
  IconButton,
  Collapse,
  Divider,
  Paper,
  Grid,
  Tooltip,
  Fade,
  Zoom,
} from '@mui/material';
import {
  CheckCircle,
  Cancel,
  Lightbulb,
  Timer,
  Quiz as QuizIcon,
  ArrowForward,
  ArrowBack,
  Refresh,
  ExpandMore,
  ExpandLess,
  Help,
} from '@mui/icons-material';
import { QuizQuestion } from '../../types/learningModule';

interface QuizComponentProps {
  questions: QuizQuestion[];
  onComplete: (score: number) => void;
  previousScore?: number;
  timeLimit?: number; // in minutes
  allowRetry?: boolean;
  showHints?: boolean;
}

interface Answer {
  questionId: string;
  answer: string | string[];
  isCorrect: boolean;
}

const QuizComponent: React.FC<QuizComponentProps> = ({
  questions,
  onComplete,
  previousScore,
  timeLimit,
  allowRetry = true,
  showHints = true,
}) => {
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [answers, setAnswers] = useState<Record<string, string | string[]>>({});
  const [submittedAnswers, setSubmittedAnswers] = useState<Answer[]>([]);
  const [showResults, setShowResults] = useState(false);
  const [timeRemaining, setTimeRemaining] = useState((timeLimit || 30) * 60);
  const [showHints, setShowHintsState] = useState<Record<string, boolean>>({});
  const [isSubmitted, setIsSubmitted] = useState(false);
  const [showExplanations, setShowExplanations] = useState<Record<string, boolean>>({});

  const currentQuestion = questions[currentQuestionIndex];
  const totalQuestions = questions.length;
  const isLastQuestion = currentQuestionIndex === totalQuestions - 1;
  const isFirstQuestion = currentQuestionIndex === 0;

  // Timer effect
  useEffect(() => {
    if (timeLimit && !isSubmitted) {
      const timer = setInterval(() => {
        setTimeRemaining((prev) => {
          if (prev <= 1) {
            handleSubmitQuiz();
            return 0;
          }
          return prev - 1;
        });
      }, 1000);

      return () => clearInterval(timer);
    }
  }, [timeLimit, isSubmitted]);

  const formatTime = (seconds: number) => {
    const minutes = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${minutes}:${secs.toString().padStart(2, '0')}`;
  };

  const handleAnswerChange = (questionId: string, answer: string | string[]) => {
    setAnswers(prev => ({
      ...prev,
      [questionId]: answer,
    }));
  };

  const toggleHint = (questionId: string) => {
    setShowHintsState(prev => ({
      ...prev,
      [questionId]: !prev[questionId],
    }));
  };

  const toggleExplanation = (questionId: string) => {
    setShowExplanations(prev => ({
      ...prev,
      [questionId]: !prev[questionId],
    }));
  };

  const checkAnswer = (question: QuizQuestion, userAnswer: string | string[]): boolean => {
    if (question.type === 'multiple-choice' || question.type === 'true-false') {
      return userAnswer === question.correctAnswer;
    } else if (question.type === 'fill-blank') {
      const correctAnswers = Array.isArray(question.correctAnswer) 
        ? question.correctAnswer 
        : [question.correctAnswer];
      const userAnswerLower = (userAnswer as string).toLowerCase().trim();
      return correctAnswers.some(correct => 
        correct.toLowerCase().trim() === userAnswerLower
      );
    } else if (question.type === 'drag-drop') {
      // For drag-drop, compare arrays
      if (!Array.isArray(userAnswer) || !Array.isArray(question.correctAnswer)) {
        return false;
      }
      return JSON.stringify(userAnswer.sort()) === JSON.stringify(question.correctAnswer.sort());
    }
    return false;
  };

  const handleSubmitQuiz = () => {
    const results: Answer[] = questions.map(question => {
      const userAnswer = answers[question.id] || '';
      const isCorrect = checkAnswer(question, userAnswer);
      return {
        questionId: question.id,
        answer: userAnswer,
        isCorrect,
      };
    });

    setSubmittedAnswers(results);
    setShowResults(true);
    setIsSubmitted(true);

    const correctAnswers = results.filter(answer => answer.isCorrect).length;
    const score = Math.round((correctAnswers / totalQuestions) * 100);
    onComplete(score);
  };

  const handleRetry = () => {
    setAnswers({});
    setSubmittedAnswers([]);
    setShowResults(false);
    setCurrentQuestionIndex(0);
    setTimeRemaining((timeLimit || 30) * 60);
    setIsSubmitted(false);
    setShowHintsState({});
    setShowExplanations({});
  };

  const renderQuestion = (question: QuizQuestion) => {
    const userAnswer = answers[question.id];
    const submittedAnswer = submittedAnswers.find(a => a.questionId === question.id);

    switch (question.type) {
      case 'multiple-choice':
        return (
          <FormControl component="fieldset" fullWidth>
            <FormLabel component="legend" sx={{ mb: 2 }}>
              {question.question}
            </FormLabel>
            <RadioGroup
              value={userAnswer || ''}
              onChange={(e) => handleAnswerChange(question.id, e.target.value)}
              disabled={isSubmitted}
            >
              {question.options?.map((option, index) => (
                <FormControlLabel
                  key={index}
                  value={option}
                  control={<Radio />}
                  label={option}
                  sx={{
                    ...(isSubmitted && {
                      color: option === question.correctAnswer ? 'success.main' : 
                             option === userAnswer && !submittedAnswer?.isCorrect ? 'error.main' : 
                             'text.primary'
                    })
                  }}
                />
              ))}
            </RadioGroup>
          </FormControl>
        );

      case 'true-false':
        return (
          <FormControl component="fieldset" fullWidth>
            <FormLabel component="legend" sx={{ mb: 2 }}>
              {question.question}
            </FormLabel>
            <RadioGroup
              value={userAnswer || ''}
              onChange={(e) => handleAnswerChange(question.id, e.target.value)}
              disabled={isSubmitted}
              row
            >
              <FormControlLabel
                value="true"
                control={<Radio />}
                label="True"
                sx={{
                  ...(isSubmitted && {
                    color: question.correctAnswer === 'true' ? 'success.main' : 
                           userAnswer === 'true' && !submittedAnswer?.isCorrect ? 'error.main' : 
                           'text.primary'
                  })
                }}
              />
              <FormControlLabel
                value="false"
                control={<Radio />}
                label="False"
                sx={{
                  ...(isSubmitted && {
                    color: question.correctAnswer === 'false' ? 'success.main' : 
                           userAnswer === 'false' && !submittedAnswer?.isCorrect ? 'error.main' : 
                           'text.primary'
                  })
                }}
              />
            </RadioGroup>
          </FormControl>
        );

      case 'fill-blank':
        return (
          <Box>
            <Typography variant="body1" sx={{ mb: 2 }}>
              {question.question}
            </Typography>
            <TextField
              fullWidth
              placeholder="Enter your answer here..."
              value={userAnswer || ''}
              onChange={(e) => handleAnswerChange(question.id, e.target.value)}
              disabled={isSubmitted}
              error={isSubmitted && !submittedAnswer?.isCorrect}
              helperText={
                isSubmitted && !submittedAnswer?.isCorrect 
                  ? `Correct answer: ${question.correctAnswer}`
                  : ''
              }
            />
          </Box>
        );

      case 'drag-drop':
        // Simplified drag-drop implementation
        const options = question.options || [];
        const selectedOptions = (userAnswer as string[]) || [];
        
        return (
          <Box>
            <Typography variant="body1" sx={{ mb: 2 }}>
              {question.question}
            </Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
              Select items in the correct order:
            </Typography>
            <FormControl component="fieldset" fullWidth>
              <FormGroup>
                {options.map((option, index) => (
                  <FormControlLabel
                    key={index}
                    control={
                      <Checkbox
                        checked={selectedOptions.includes(option)}
                        onChange={(e) => {
                          const newAnswer = e.target.checked
                            ? [...selectedOptions, option]
                            : selectedOptions.filter(item => item !== option);
                          handleAnswerChange(question.id, newAnswer);
                        }}
                        disabled={isSubmitted}
                      />
                    }
                    label={`${selectedOptions.indexOf(option) + 1 || ''}. ${option}`}
                  />
                ))}
              </FormGroup>
            </FormControl>
          </Box>
        );

      default:
        return (
          <Typography>
            Question type "{question.type}" not implemented yet.
          </Typography>
        );
    }
  };

  const getQuestionIcon = (question: QuizQuestion) => {
    const submittedAnswer = submittedAnswers.find(a => a.questionId === question.id);
    if (isSubmitted) {
      return submittedAnswer?.isCorrect ? 
        <CheckCircle color="success" /> : 
        <Cancel color="error" />;
    }
    return <QuizIcon color="primary" />;
  };

  const getDifficultyColor = (difficulty: string) => {
    switch (difficulty) {
      case 'easy': return 'success';
      case 'medium': return 'warning';
      case 'hard': return 'error';
      default: return 'default';
    }
  };

  if (showResults) {
    const correctAnswers = submittedAnswers.filter(answer => answer.isCorrect).length;
    const score = Math.round((correctAnswers / totalQuestions) * 100);
    const passed = score >= 70; // 70% passing grade

    return (
      <Fade in={showResults}>
        <Card sx={{ mb: 3 }}>
          <CardContent>
            <Box sx={{ textAlign: 'center', mb: 3 }}>
              <Typography variant="h4" component="h2" gutterBottom>
                Quiz Results
              </Typography>
              <Box sx={{ mb: 2 }}>
                <Typography variant="h2" color={passed ? 'success.main' : 'error.main'}>
                  {score}%
                </Typography>
                <Typography variant="body1">
                  {correctAnswers} out of {totalQuestions} questions correct
                </Typography>
              </Box>
              <Alert severity={passed ? 'success' : 'warning'} sx={{ mb: 2 }}>
                {passed 
                  ? '🎉 Congratulations! You passed the quiz!' 
                  : '📚 Keep studying! You can retry to improve your score.'}
              </Alert>
            </Box>

            <Divider sx={{ my: 3 }} />

            <Typography variant="h6" gutterBottom>
              Detailed Results
            </Typography>

            {questions.map((question, index) => {
              const submittedAnswer = submittedAnswers.find(a => a.questionId === question.id);
              const userAnswer = answers[question.id];
              
              return (
                <Paper key={question.id} sx={{ p: 2, mb: 2 }}>
                  <Box sx={{ display: 'flex', alignItems: 'flex-start', gap: 2 }}>
                    <Box sx={{ mt: 0.5 }}>
                      {getQuestionIcon(question)}
                    </Box>
                    <Box sx={{ flex: 1 }}>
                      <Typography variant="body1" fontWeight="bold">
                        Question {index + 1}: {question.question}
                      </Typography>
                      <Typography variant="body2" sx={{ mt: 1 }}>
                        Your answer: {Array.isArray(userAnswer) ? userAnswer.join(', ') : userAnswer || 'No answer'}
                      </Typography>
                      <Typography variant="body2" color="success.main">
                        Correct answer: {Array.isArray(question.correctAnswer) ? 
                          question.correctAnswer.join(', ') : question.correctAnswer}
                      </Typography>
                      
                      <Box sx={{ mt: 2 }}>
                        <Button
                          size="small"
                          onClick={() => toggleExplanation(question.id)}
                          endIcon={showExplanations[question.id] ? <ExpandLess /> : <ExpandMore />}
                        >
                          Explanation
                        </Button>
                        <Collapse in={showExplanations[question.id]}>
                          <Alert severity="info" sx={{ mt: 1 }}>
                            {question.explanation}
                          </Alert>
                        </Collapse>
                      </Box>
                    </Box>
                    <Chip
                      label={`${question.points} pts`}
                      color={submittedAnswer?.isCorrect ? 'success' : 'default'}
                      size="small"
                    />
                  </Box>
                </Paper>
              );
            })}
          </CardContent>
          <CardActions sx={{ justifyContent: 'space-between' }}>
            <Typography variant="body2" color="text.secondary">
              {previousScore && `Previous best: ${previousScore}%`}
            </Typography>
            {allowRetry && (
              <Button
                variant="contained"
                startIcon={<Refresh />}
                onClick={handleRetry}
              >
                Retry Quiz
              </Button>
            )}
          </CardActions>
        </Card>
      </Fade>
    );
  }

  return (
    <Card sx={{ mb: 3 }}>
      <CardContent>
        {/* Header */}
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
          <Typography variant="h5" component="h2">
            📝 Section Quiz
          </Typography>
          {timeLimit && (
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
              <Timer color={timeRemaining < 300 ? 'error' : 'primary'} />
              <Typography 
                variant="body2" 
                color={timeRemaining < 300 ? 'error.main' : 'text.primary'}
                fontWeight="bold"
              >
                {formatTime(timeRemaining)}
              </Typography>
            </Box>
          )}
        </Box>

        {/* Progress */}
        <Box sx={{ mb: 3 }}>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
            <Typography variant="body2">
              Question {currentQuestionIndex + 1} of {totalQuestions}
            </Typography>
            <Box sx={{ display: 'flex', gap: 1 }}>
              <Chip
                label={currentQuestion.difficulty}
                color={getDifficultyColor(currentQuestion.difficulty)}
                size="small"
              />
              <Chip
                label={`${currentQuestion.points} points`}
                variant="outlined"
                size="small"
              />
            </Box>
          </Box>
          <LinearProgress
            variant="determinate"
            value={(currentQuestionIndex / totalQuestions) * 100}
            sx={{ height: 8, borderRadius: 4 }}
          />
        </Box>

        {/* Question */}
        <Box sx={{ mb: 3 }}>
          {renderQuestion(currentQuestion)}
        </Box>

        {/* Hints */}
        {showHints && currentQuestion.hints && currentQuestion.hints.length > 0 && (
          <Box sx={{ mb: 3 }}>
            <Button
              size="small"
              startIcon={<Lightbulb />}
              onClick={() => toggleHint(currentQuestion.id)}
              color="secondary"
            >
              {showHints[currentQuestion.id] ? 'Hide Hint' : 'Show Hint'}
            </Button>
            <Collapse in={showHintsState[currentQuestion.id]}>
              <Alert severity="info" icon={<Lightbulb />} sx={{ mt: 1 }}>
                {currentQuestion.hints[0]}
              </Alert>
            </Collapse>
          </Box>
        )}
      </CardContent>

      {/* Navigation */}
      <CardActions sx={{ justifyContent: 'space-between', px: 2, pb: 2 }}>
        <Button
          onClick={() => setCurrentQuestionIndex(prev => prev - 1)}
          disabled={isFirstQuestion}
          startIcon={<ArrowBack />}
        >
          Previous
        </Button>

        <Box sx={{ display: 'flex', gap: 1 }}>
          {questions.map((_, index) => (
            <Tooltip key={index} title={`Question ${index + 1}`}>
              <Box
                sx={{
                  width: 12,
                  height: 12,
                  borderRadius: '50%',
                  bgcolor: index === currentQuestionIndex ? 'primary.main' :
                           answers[questions[index].id] ? 'success.light' : 'grey.300',
                  cursor: 'pointer',
                }}
                onClick={() => setCurrentQuestionIndex(index)}
              />
            </Tooltip>
          ))}
        </Box>

        {isLastQuestion ? (
          <Button
            variant="contained"
            onClick={handleSubmitQuiz}
            disabled={Object.keys(answers).length !== totalQuestions}
            color="primary"
          >
            Submit Quiz
          </Button>
        ) : (
          <Button
            onClick={() => setCurrentQuestionIndex(prev => prev + 1)}
            endIcon={<ArrowForward />}
            variant={answers[currentQuestion.id] ? 'contained' : 'outlined'}
          >
            Next
          </Button>
        )}
      </CardActions>
    </Card>
  );
};

export default QuizComponent;
