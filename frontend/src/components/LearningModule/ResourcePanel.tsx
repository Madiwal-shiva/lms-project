import React, { useState } from 'react';
import {
  Drawer,
  Box,
  Typography,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  ListItemSecondaryAction,
  IconButton,
  Chip,
  Alert,
  TextField,
  Paper,
  Divider,
  Button,
  Link,
  Accordion,
  AccordionSummary,
  AccordionDetails,
} from '@mui/material';
import {
  PictureAsPdf,
  Link as LinkIcon,
  VideoLibrary,
  AudioFile,
  Description,
  Download,
  OpenInNew,
  Search,
  Close,
  ExpandMore,
  Folder,
  Star,
  StarBorder,
} from '@mui/icons-material';
import { Resource } from '../../types/learningModule';

interface ResourcePanelProps {
  open: boolean;
  onClose: () => void;
  resources: Resource[];
}

const ResourcePanel: React.FC<ResourcePanelProps> = ({
  open,
  onClose,
  resources,
}) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [favorites, setFavorites] = useState<string[]>([]);
  const [selectedType, setSelectedType] = useState<string>('all');

  const getResourceIcon = (type: string) => {
    switch (type) {
      case 'pdf':
        return <PictureAsPdf color="error" />;
      case 'video':
        return <VideoLibrary color="primary" />;
      case 'audio':
        return <AudioFile color="secondary" />;
      case 'document':
        return <Description color="info" />;
      case 'link':
      default:
        return <LinkIcon color="success" />;
    }
  };

  const getResourceTypeColor = (type: string) => {
    switch (type) {
      case 'pdf':
        return 'error';
      case 'video':
        return 'primary';
      case 'audio':
        return 'secondary';
      case 'document':
        return 'info';
      case 'link':
      default:
        return 'success';
    }
  };

  const toggleFavorite = (resourceId: string) => {
    setFavorites(prev =>
      prev.includes(resourceId)
        ? prev.filter(id => id !== resourceId)
        : [...prev, resourceId]
    );
  };

  const filteredResources = resources.filter(resource => {
    const matchesSearch = resource.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         resource.description?.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesType = selectedType === 'all' || resource.type === selectedType;
    return matchesSearch && matchesType;
  });

  const groupedResources = filteredResources.reduce((groups, resource) => {
    const type = resource.type;
    if (!groups[type]) {
      groups[type] = [];
    }
    groups[type].push(resource);
    return groups;
  }, {} as Record<string, Resource[]>);

  const resourceTypes = Array.from(new Set(resources.map(r => r.type)));

  const handleResourceClick = (resource: Resource) => {
    if (resource.type === 'link') {
      window.open(resource.url, '_blank', 'noopener,noreferrer');
    } else {
      // For downloadable resources, trigger download
      const a = document.createElement('a');
      a.href = resource.url;
      a.download = resource.title;
      a.click();
    }
  };

  const formatFileSize = (size?: string) => {
    if (!size) return '';
    return ` (${size})`;
  };

  const getTypeDisplayName = (type: string) => {
    switch (type) {
      case 'pdf':
        return 'PDF Documents';
      case 'video':
        return 'Videos';
      case 'audio':
        return 'Audio Files';
      case 'document':
        return 'Documents';
      case 'link':
        return 'External Links';
      default:
        return type.charAt(0).toUpperCase() + type.slice(1);
    }
  };

  return (
    <Drawer
      anchor="right"
      open={open}
      onClose={onClose}
      sx={{
        '& .MuiDrawer-paper': {
          width: { xs: '100%', sm: 400, md: 450 },
          p: 2,
        },
      }}
    >
      {/* Header */}
      <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 2 }}>
        <Typography variant="h6" sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
          <Folder /> Resources
        </Typography>
        <IconButton onClick={onClose} size="small">
          <Close />
        </IconButton>
      </Box>

      {/* Search and Filter */}
      <Box sx={{ mb: 2 }}>
        <TextField
          fullWidth
          size="small"
          placeholder="Search resources..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          InputProps={{
            startAdornment: <Search sx={{ mr: 1, color: 'text.secondary' }} />,
          }}
          sx={{ mb: 2 }}
        />

        <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap' }}>
          <Chip
            label="All"
            onClick={() => setSelectedType('all')}
            color={selectedType === 'all' ? 'primary' : 'default'}
            size="small"
          />
          {resourceTypes.map(type => (
            <Chip
              key={type}
              label={getTypeDisplayName(type)}
              onClick={() => setSelectedType(type)}
              color={selectedType === type ? 'primary' : 'default'}
              size="small"
            />
          ))}
        </Box>
      </Box>

      {/* Resources List */}
      {filteredResources.length === 0 ? (
        <Alert severity="info" sx={{ mt: 2 }}>
          {resources.length === 0 
            ? "No resources available for this module."
            : "No resources match your search criteria."}
        </Alert>
      ) : (
        <Box sx={{ flex: 1, overflow: 'auto' }}>
          {/* Favorites Section */}
          {favorites.length > 0 && (
            <Paper sx={{ p: 2, mb: 2, bgcolor: 'action.hover' }}>
              <Typography variant="subtitle2" sx={{ mb: 1, display: 'flex', alignItems: 'center', gap: 1 }}>
                <Star color="warning" /> Favorites
              </Typography>
              <List dense>
                {resources
                  .filter(resource => favorites.includes(resource.id))
                  .map((resource) => (
                    <ListItem
                      key={resource.id}
                      button
                      onClick={() => handleResourceClick(resource)}
                      sx={{ px: 1, borderRadius: 1 }}
                    >
                      <ListItemIcon sx={{ minWidth: 36 }}>
                        {getResourceIcon(resource.type)}
                      </ListItemIcon>
                      <ListItemText
                        primary={resource.title}
                        secondary={resource.size && formatFileSize(resource.size)}
                        primaryTypographyProps={{ variant: 'body2' }}
                        secondaryTypographyProps={{ variant: 'caption' }}
                      />
                    </ListItem>
                  ))}
              </List>
            </Paper>
          )}

          {/* Grouped Resources */}
          {Object.entries(groupedResources).map(([type, typeResources]) => (
            <Accordion key={type} defaultExpanded sx={{ mb: 1 }}>
              <AccordionSummary expandIcon={<ExpandMore />}>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                  {getResourceIcon(type)}
                  <Typography variant="subtitle2">
                    {getTypeDisplayName(type)} ({typeResources.length})
                  </Typography>
                </Box>
              </AccordionSummary>
              <AccordionDetails sx={{ pt: 0 }}>
                <List dense>
                  {typeResources.map((resource) => (
                    <ListItem
                      key={resource.id}
                      sx={{
                        border: '1px solid',
                        borderColor: 'divider',
                        borderRadius: 1,
                        mb: 1,
                        px: 2,
                        py: 1,
                      }}
                    >
                      <ListItemIcon sx={{ minWidth: 40 }}>
                        {getResourceIcon(resource.type)}
                      </ListItemIcon>
                      <ListItemText
                        primary={
                          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                            {resource.title}
                            <Chip
                              label={resource.type.toUpperCase()}
                              size="small"
                              color={getResourceTypeColor(resource.type)}
                              sx={{ fontSize: '0.7rem', height: 18 }}
                            />
                          </Box>
                        }
                        secondary={
                          <Box>
                            {resource.description && (
                              <Typography variant="body2" color="text.secondary" sx={{ mb: 0.5 }}>
                                {resource.description}
                              </Typography>
                            )}
                            {resource.size && (
                              <Typography variant="caption" color="text.secondary">
                                Size: {resource.size}
                              </Typography>
                            )}
                          </Box>
                        }
                      />
                      <ListItemSecondaryAction>
                        <Box sx={{ display: 'flex', flexDirection: 'column', gap: 0.5 }}>
                          <IconButton
                            size="small"
                            onClick={() => toggleFavorite(resource.id)}
                            color={favorites.includes(resource.id) ? 'warning' : 'default'}
                          >
                            {favorites.includes(resource.id) ? <Star /> : <StarBorder />}
                          </IconButton>
                          <IconButton
                            size="small"
                            onClick={() => handleResourceClick(resource)}
                            color="primary"
                          >
                            {resource.type === 'link' ? <OpenInNew /> : <Download />}
                          </IconButton>
                        </Box>
                      </ListItemSecondaryAction>
                    </ListItem>
                  ))}
                </List>
              </AccordionDetails>
            </Accordion>
          ))}
        </Box>
      )}

      {/* Statistics */}
      {resources.length > 0 && (
        <Paper sx={{ p: 2, mt: 2, bgcolor: 'action.hover' }}>
          <Typography variant="body2" color="text.secondary">
            📚 {resources.length} resources total
            {searchTerm && ` • ${filteredResources.length} matching search`}
            {favorites.length > 0 && ` • ${favorites.length} favorites`}
          </Typography>
        </Paper>
      )}

      {/* Quick Actions */}
      <Box sx={{ mt: 2, display: 'flex', gap: 1 }}>
        <Button
          size="small"
          variant="outlined"
          onClick={() => {
            resources.forEach(resource => {
              if (resource.type !== 'link') {
                const a = document.createElement('a');
                a.href = resource.url;
                a.download = resource.title;
                a.click();
              }
            });
          }}
          disabled={resources.filter(r => r.type !== 'link').length === 0}
          fullWidth
        >
          Download All Files
        </Button>
      </Box>
    </Drawer>
  );
};

export default ResourcePanel;
