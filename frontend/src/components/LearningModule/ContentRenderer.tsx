import React, { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  Card,
  CardContent,
  CardActions,
  Button,
  Paper,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Alert,
  Zoom,
  Fade,
  Slide,
  TextField,
  IconButton,
  Tooltip,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from '@mui/material';
import {
  ExpandMore,
  PlayArrow,
  Pause,
  VolumeUp,
  VolumeOff,
  Fullscreen,
  Code as CodeIcon,
  Psychology,
  Science,
  Lightbulb,
  Warning,
  Info,
  CheckCircle,
  ContentCopy,
  ZoomIn,
  ZoomOut,
} from '@mui/material-icons';
import { ContentBlock, InteractiveElement } from '../../types/learningModule';
import SyntaxHighlighter from 'react-syntax-highlighter';
import { vs2015 } from 'react-syntax-highlighter/dist/esm/styles/hljs';

interface ContentRendererProps {
  content: ContentBlock;
  onInteraction?: () => void;
  isFullscreen?: boolean;
}

const ContentRenderer: React.FC<ContentRendererProps> = ({
  content,
  onInteraction,
  isFullscreen = false,
}) => {
  const [isExpanded, setIsExpanded] = useState(false);
  const [codeExecutionResult, setCodeExecutionResult] = useState<string>('');
  const [zoomLevel, setZoomLevel] = useState(1);
  const [showZoomDialog, setShowZoomDialog] = useState(false);

  const handleInteraction = () => {
    if (onInteraction) {
      onInteraction();
    }
  };

  const copyToClipboard = (text: string) => {
    navigator.clipboard.writeText(text);
  };

  const renderTextContent = (text: string) => {
    // Enhanced text rendering with markdown-like features
    const lines = text.split('\n');
    
    return lines.map((line, index) => {
      // Handle different text formatting
      if (line.startsWith('# ')) {
        return (
          <Typography key={index} variant="h4" component="h2" sx={{ mt: 2, mb: 1 }}>
            {line.substring(2)}
          </Typography>
        );
      } else if (line.startsWith('## ')) {
        return (
          <Typography key={index} variant="h5" component="h3" sx={{ mt: 2, mb: 1 }}>
            {line.substring(3)}
          </Typography>
        );
      } else if (line.startsWith('### ')) {
        return (
          <Typography key={index} variant="h6" component="h4" sx={{ mt: 1, mb: 1 }}>
            {line.substring(4)}
          </Typography>
        );
      } else if (line.startsWith('> ')) {
        return (
          <Alert key={index} severity="info" sx={{ my: 1 }}>
            {line.substring(2)}
          </Alert>
        );
      } else if (line.startsWith('! ')) {
        return (
          <Alert key={index} severity="warning" sx={{ my: 1 }}>
            {line.substring(2)}
          </Alert>
        );
      } else if (line.startsWith('✓ ')) {
        return (
          <Alert key={index} severity="success" sx={{ my: 1 }}>
            {line.substring(2)}
          </Alert>
        );
      } else if (line.startsWith('- ')) {
        return (
          <Typography key={index} component="li" sx={{ ml: 2 }}>
            {line.substring(2)}
          </Typography>
        );
      } else if (line.trim() === '') {
        return <Box key={index} sx={{ height: 16 }} />;
      } else {
        return (
          <Typography key={index} paragraph>
            {line}
          </Typography>
        );
      }
    });
  };

  const renderCodeContent = (code: string, language = 'javascript') => {
    return (
      <Card sx={{ my: 2 }}>
        <CardContent>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
            <Typography variant="h6" sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
              <CodeIcon /> Code Example
            </Typography>
            <Tooltip title="Copy to clipboard">
              <IconButton onClick={() => copyToClipboard(code)} size="small">
                <ContentCopy />
              </IconButton>
            </Tooltip>
          </Box>
          <SyntaxHighlighter
            language={language}
            style={vs2015}
            customStyle={{
              borderRadius: '8px',
              fontSize: '14px',
            }}
          >
            {code}
          </SyntaxHighlighter>
          {language === 'javascript' && (
            <CardActions>
              <Button
                startIcon={<PlayArrow />}
                onClick={() => {
                  try {
                    // Simple code execution simulation
                    const result = eval(code);
                    setCodeExecutionResult(JSON.stringify(result, null, 2));
                  } catch (error) {
                    setCodeExecutionResult(`Error: ${error.message}`);
                  }
                  handleInteraction();
                }}
              >
                Run Code
              </Button>
            </CardActions>
          )}
          {codeExecutionResult && (
            <Box sx={{ mt: 2, p: 2, bgcolor: 'grey.100', borderRadius: 1 }}>
              <Typography variant="body2" component="pre">
                Output: {codeExecutionResult}
              </Typography>
            </Box>
          )}
        </CardContent>
      </Card>
    );
  };

  const renderVideoContent = (videoData: any) => {
    const [isPlaying, setIsPlaying] = useState(false);
    const [isMuted, setIsMuted] = useState(false);

    return (
      <Card sx={{ my: 2 }}>
        <CardContent>
          <Box sx={{ position: 'relative', paddingTop: '56.25%', bgcolor: 'black', borderRadius: 1 }}>
            {/* Video placeholder - replace with actual video component */}
            <Box
              sx={{
                position: 'absolute',
                top: 0,
                left: 0,
                width: '100%',
                height: '100%',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                color: 'white',
              }}
            >
              <Typography variant="h4">🎥</Typography>
            </Box>
            <Box
              sx={{
                position: 'absolute',
                bottom: 16,
                left: 16,
                right: 16,
                display: 'flex',
                gap: 1,
              }}
            >
              <IconButton
                onClick={() => {
                  setIsPlaying(!isPlaying);
                  handleInteraction();
                }}
                sx={{ color: 'white' }}
              >
                {isPlaying ? <Pause /> : <PlayArrow />}
              </IconButton>
              <IconButton
                onClick={() => setIsMuted(!isMuted)}
                sx={{ color: 'white' }}
              >
                {isMuted ? <VolumeOff /> : <VolumeUp />}
              </IconButton>
              <IconButton sx={{ color: 'white', ml: 'auto' }}>
                <Fullscreen />
              </IconButton>
            </Box>
          </Box>
          <Typography variant="h6" sx={{ mt: 2 }}>
            {videoData.title || 'Video Content'}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            {videoData.description}
          </Typography>
        </CardContent>
      </Card>
    );
  };

  const renderInteractiveContent = (interactive: InteractiveElement) => {
    const [userInput, setUserInput] = useState('');
    const [feedback, setFeedback] = useState('');

    const handleSimulation = () => {
      switch (interactive.type) {
        case 'simulation':
          setFeedback('Simulation running... Results will appear here.');
          handleInteraction();
          break;
        case 'code-editor':
          setFeedback('Code compiled successfully!');
          handleInteraction();
          break;
        case 'virtual-lab':
          setFeedback('Experiment completed. Observe the results.');
          handleInteraction();
          break;
        default:
          setFeedback('Interactive element activated.');
          handleInteraction();
      }
    };

    const getInteractiveIcon = () => {
      switch (interactive.type) {
        case 'simulation':
          return <Psychology />;
        case 'virtual-lab':
          return <Science />;
        case 'code-editor':
          return <CodeIcon />;
        default:
          return <Lightbulb />;
      }
    };

    return (
      <Card sx={{ my: 2, border: '2px solid', borderColor: 'primary.main' }}>
        <CardContent>
          <Typography variant="h6" sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 2 }}>
            {getInteractiveIcon()}
            {interactive.title}
          </Typography>
          
          <Box sx={{ my: 2, p: 2, bgcolor: 'grey.50', borderRadius: 1 }}>
            <Typography variant="body1" paragraph>
              {interactive.content.description || 'Interactive learning element'}
            </Typography>
            
            {interactive.type === 'code-editor' && (
              <TextField
                fullWidth
                multiline
                rows={6}
                variant="outlined"
                placeholder="Enter your code here..."
                value={userInput}
                onChange={(e) => setUserInput(e.target.value)}
                sx={{ mb: 2, fontFamily: 'monospace' }}
              />
            )}
            
            {interactive.type === 'simulation' && (
              <Box sx={{ 
                height: 200, 
                bgcolor: 'background.paper', 
                border: '1px dashed grey', 
                borderRadius: 1,
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center'
              }}>
                <Typography color="text.secondary">
                  Simulation Canvas - Click "Start" to begin
                </Typography>
              </Box>
            )}
            
            {interactive.type === 'virtual-lab' && (
              <Box sx={{ 
                display: 'grid', 
                gridTemplateColumns: 'repeat(3, 1fr)', 
                gap: 2,
                my: 2 
              }}>
                {['🧪', '⚗️', '🔬'].map((icon, index) => (
                  <Paper
                    key={index}
                    sx={{
                      p: 2,
                      textAlign: 'center',
                      cursor: 'pointer',
                      '&:hover': { bgcolor: 'action.hover' }
                    }}
                    onClick={handleSimulation}
                  >
                    <Typography variant="h3">{icon}</Typography>
                    <Typography variant="caption">Tool {index + 1}</Typography>
                  </Paper>
                ))}
              </Box>
            )}
          </Box>

          {feedback && (
            <Fade in={Boolean(feedback)}>
              <Alert severity="info" sx={{ my: 2 }}>
                {feedback}
              </Alert>
            </Fade>
          )}

          <CardActions>
            <Button
              variant="contained"
              startIcon={<PlayArrow />}
              onClick={handleSimulation}
              color="primary"
            >
              {interactive.type === 'simulation' ? 'Start Simulation' :
               interactive.type === 'code-editor' ? 'Run Code' :
               interactive.type === 'virtual-lab' ? 'Start Experiment' :
               'Activate'}
            </Button>
            <Button
              onClick={() => setFeedback('')}
              disabled={!feedback}
            >
              Reset
            </Button>
          </CardActions>
        </CardContent>
      </Card>
    );
  };

  const renderImageContent = (imageData: any) => {
    return (
      <Card sx={{ my: 2 }}>
        <CardContent>
          <Box
            sx={{
              position: 'relative',
              textAlign: 'center',
              '& img': {
                maxWidth: '100%',
                height: 'auto',
                borderRadius: 1,
                transform: `scale(${zoomLevel})`,
                transition: 'transform 0.3s ease',
              }
            }}
          >
            {/* Image placeholder */}
            <Box
              sx={{
                width: '100%',
                height: 300,
                bgcolor: 'grey.200',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                borderRadius: 1,
                transform: `scale(${zoomLevel})`,
                transition: 'transform 0.3s ease',
              }}
            >
              <Typography variant="h6" color="text.secondary">
                📷 {imageData.title || 'Image Content'}
              </Typography>
            </Box>
            
            <Box sx={{ position: 'absolute', top: 8, right: 8, display: 'flex', gap: 1 }}>
              <IconButton
                onClick={() => setZoomLevel(prev => Math.min(prev + 0.2, 3))}
                size="small"
                sx={{ bgcolor: 'rgba(255,255,255,0.8)' }}
              >
                <ZoomIn />
              </IconButton>
              <IconButton
                onClick={() => setZoomLevel(prev => Math.max(prev - 0.2, 0.5))}
                size="small"
                sx={{ bgcolor: 'rgba(255,255,255,0.8)' }}
              >
                <ZoomOut />
              </IconButton>
              <IconButton
                onClick={() => setShowZoomDialog(true)}
                size="small"
                sx={{ bgcolor: 'rgba(255,255,255,0.8)' }}
              >
                <Fullscreen />
              </IconButton>
            </Box>
          </Box>
          
          <Typography variant="h6" sx={{ mt: 2 }}>
            {imageData.title}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            {imageData.description}
          </Typography>
        </CardContent>
      </Card>
    );
  };

  const renderContent = () => {
    const containerSx = {
      p: 3,
      minHeight: isFullscreen ? '100vh' : '60vh',
      position: 'relative',
      ...(isFullscreen && {
        position: 'fixed',
        top: 0,
        left: 0,
        width: '100vw',
        height: '100vh',
        zIndex: 9999,
        bgcolor: 'background.paper',
        overflow: 'auto',
      }),
    };

    const animationVariant = content.metadata?.difficulty === 'hard' ? 'slide' : 'fade';

    const AnimationWrapper = animationVariant === 'slide' ? Slide : Fade;

    return (
      <AnimationWrapper in={true} timeout={600}>
        <Box sx={containerSx}>
          {content.metadata?.difficulty && (
            <Chip
              label={content.metadata.difficulty.toUpperCase()}
              color={
                content.metadata.difficulty === 'easy' ? 'success' :
                content.metadata.difficulty === 'medium' ? 'warning' : 'error'
              }
              size="small"
              sx={{ mb: 2 }}
            />
          )}

          {content.type === 'text' && renderTextContent(content.content as string)}
          {content.type === 'heading' && (
            <Typography variant="h4" component="h2" sx={{ mb: 2 }}>
              {content.content as string}
            </Typography>
          )}
          {content.type === 'code' && renderCodeContent(content.content as string)}
          {content.type === 'video' && renderVideoContent(content.content)}
          {content.type === 'image' && renderImageContent(content.content)}
          {content.type === 'interactive' && renderInteractiveContent(content.content as InteractiveElement)}

          {content.metadata?.estimatedTime && (
            <Box sx={{ mt: 3, p: 2, bgcolor: 'action.hover', borderRadius: 1 }}>
              <Typography variant="body2" color="text.secondary">
                📚 Estimated reading time: {content.metadata.estimatedTime} minutes
              </Typography>
            </Box>
          )}

          {content.metadata?.tags && content.metadata.tags.length > 0 && (
            <Box sx={{ mt: 2, display: 'flex', gap: 1, flexWrap: 'wrap' }}>
              {content.metadata.tags.map((tag, index) => (
                <Chip key={index} label={tag} size="small" variant="outlined" />
              ))}
            </Box>
          )}
        </Box>
      </AnimationWrapper>
    );
  };

  return (
    <>
      {renderContent()}
      
      {/* Zoom Dialog for Images */}
      <Dialog
        open={showZoomDialog}
        onClose={() => setShowZoomDialog(false)}
        maxWidth="lg"
        fullWidth
      >
        <DialogTitle>
          Full Size View
          <IconButton
            onClick={() => setShowZoomDialog(false)}
            sx={{ position: 'absolute', right: 8, top: 8 }}
          >
            ✕
          </IconButton>
        </DialogTitle>
        <DialogContent>
          {content.type === 'image' && renderImageContent(content.content)}
        </DialogContent>
      </Dialog>
    </>
  );
};

export default ContentRenderer;
