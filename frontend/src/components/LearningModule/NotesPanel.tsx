import React, { useState } from 'react';
import {
  Drawer,
  Box,
  Typography,
  TextField,
  Button,
  List,
  ListItem,
  ListItemText,
  ListItemSecondaryAction,
  IconButton,
  Divider,
  Paper,
  Chip,
  Alert,
  Fab,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from '@mui/material';
import {
  Add,
  Delete,
  Edit,
  Save,
  Cancel,
  Search,
  Download,
  Upload,
  Notes as NotesIcon,
  Close,
} from '@mui/icons-material';

interface NotesPanelProps {
  open: boolean;
  onClose: () => void;
  notes: string[];
  onNotesChange: (notes: string[]) => void;
}

interface Note {
  id: string;
  content: string;
  timestamp: Date;
  tags: string[];
}

const NotesPanel: React.FC<NotesPanelProps> = ({
  open,
  onClose,
  notes,
  onNotesChange,
}) => {
  const [noteList, setNoteList] = useState<Note[]>(
    notes.map((note, index) => ({
      id: `note-${index}`,
      content: note,
      timestamp: new Date(),
      tags: [],
    }))
  );
  const [newNote, setNewNote] = useState('');
  const [editingId, setEditingId] = useState<string | null>(null);
  const [editContent, setEditContent] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [showAddDialog, setShowAddDialog] = useState(false);
  const [newNoteTags, setNewNoteTags] = useState('');

  const handleAddNote = () => {
    if (newNote.trim()) {
      const note: Note = {
        id: `note-${Date.now()}`,
        content: newNote.trim(),
        timestamp: new Date(),
        tags: newNoteTags.split(',').map(tag => tag.trim()).filter(Boolean),
      };
      
      const updatedNotes = [...noteList, note];
      setNoteList(updatedNotes);
      onNotesChange(updatedNotes.map(n => n.content));
      
      setNewNote('');
      setNewNoteTags('');
      setShowAddDialog(false);
    }
  };

  const handleEditNote = (id: string) => {
    const note = noteList.find(n => n.id === id);
    if (note) {
      setEditingId(id);
      setEditContent(note.content);
    }
  };

  const handleSaveEdit = () => {
    if (editingId && editContent.trim()) {
      const updatedNotes = noteList.map(note =>
        note.id === editingId
          ? { ...note, content: editContent.trim(), timestamp: new Date() }
          : note
      );
      setNoteList(updatedNotes);
      onNotesChange(updatedNotes.map(n => n.content));
      setEditingId(null);
      setEditContent('');
    }
  };

  const handleCancelEdit = () => {
    setEditingId(null);
    setEditContent('');
  };

  const handleDeleteNote = (id: string) => {
    const updatedNotes = noteList.filter(note => note.id !== id);
    setNoteList(updatedNotes);
    onNotesChange(updatedNotes.map(n => n.content));
  };

  const filteredNotes = noteList.filter(note =>
    note.content.toLowerCase().includes(searchTerm.toLowerCase()) ||
    note.tags.some(tag => tag.toLowerCase().includes(searchTerm.toLowerCase()))
  );

  const exportNotes = () => {
    const notesText = noteList.map(note => 
      `${note.timestamp.toLocaleString()}\n${note.content}\nTags: ${note.tags.join(', ')}\n\n`
    ).join('---\n\n');
    
    const blob = new Blob([notesText], { type: 'text/plain' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'learning-notes.txt';
    a.click();
    URL.revokeObjectURL(url);
  };

  const formatTimestamp = (date: Date) => {
    return new Intl.DateTimeFormat('en-US', {
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    }).format(date);
  };

  return (
    <>
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
            <NotesIcon /> My Notes
          </Typography>
          <IconButton onClick={onClose} size="small">
            <Close />
          </IconButton>
        </Box>

        {/* Search */}
        <TextField
          fullWidth
          size="small"
          placeholder="Search notes..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          InputProps={{
            startAdornment: <Search sx={{ mr: 1, color: 'text.secondary' }} />,
          }}
          sx={{ mb: 2 }}
        />

        {/* Actions */}
        <Box sx={{ display: 'flex', gap: 1, mb: 2 }}>
          <Button
            variant="contained"
            startIcon={<Add />}
            onClick={() => setShowAddDialog(true)}
            size="small"
            fullWidth
          >
            Add Note
          </Button>
          <Button
            variant="outlined"
            startIcon={<Download />}
            onClick={exportNotes}
            size="small"
            disabled={noteList.length === 0}
          >
            Export
          </Button>
        </Box>

        {/* Notes List */}
        {filteredNotes.length === 0 ? (
          <Alert severity="info" sx={{ mt: 2 }}>
            {noteList.length === 0 
              ? "No notes yet. Start taking notes to remember important concepts!"
              : "No notes match your search."}
          </Alert>
        ) : (
          <List sx={{ flex: 1, overflow: 'auto' }}>
            {filteredNotes.map((note) => (
              <React.Fragment key={note.id}>
                <ListItem
                  sx={{
                    flexDirection: 'column',
                    alignItems: 'stretch',
                    bgcolor: 'background.paper',
                    border: '1px solid',
                    borderColor: 'divider',
                    borderRadius: 1,
                    mb: 1,
                    p: 2,
                  }}
                >
                  {editingId === note.id ? (
                    <Box>
                      <TextField
                        fullWidth
                        multiline
                        rows={3}
                        value={editContent}
                        onChange={(e) => setEditContent(e.target.value)}
                        autoFocus
                        sx={{ mb: 1 }}
                      />
                      <Box sx={{ display: 'flex', gap: 1, justifyContent: 'flex-end' }}>
                        <Button
                          size="small"
                          startIcon={<Save />}
                          onClick={handleSaveEdit}
                          variant="contained"
                        >
                          Save
                        </Button>
                        <Button
                          size="small"
                          startIcon={<Cancel />}
                          onClick={handleCancelEdit}
                        >
                          Cancel
                        </Button>
                      </Box>
                    </Box>
                  ) : (
                    <>
                      <ListItemText
                        primary={note.content}
                        secondary={
                          <Box sx={{ mt: 1 }}>
                            <Typography variant="caption" color="text.secondary">
                              {formatTimestamp(note.timestamp)}
                            </Typography>
                            {note.tags.length > 0 && (
                              <Box sx={{ mt: 0.5, display: 'flex', gap: 0.5, flexWrap: 'wrap' }}>
                                {note.tags.map((tag, index) => (
                                  <Chip
                                    key={index}
                                    label={tag}
                                    size="small"
                                    variant="outlined"
                                    sx={{ fontSize: '0.7rem', height: 20 }}
                                  />
                                ))}
                              </Box>
                            )}
                          </Box>
                        }
                        sx={{ mb: 1 }}
                      />
                      <Box sx={{ display: 'flex', gap: 1, justifyContent: 'flex-end' }}>
                        <IconButton
                          size="small"
                          onClick={() => handleEditNote(note.id)}
                          color="primary"
                        >
                          <Edit fontSize="small" />
                        </IconButton>
                        <IconButton
                          size="small"
                          onClick={() => handleDeleteNote(note.id)}
                          color="error"
                        >
                          <Delete fontSize="small" />
                        </IconButton>
                      </Box>
                    </>
                  )}
                </ListItem>
              </React.Fragment>
            ))}
          </List>
        )}

        {/* Statistics */}
        {noteList.length > 0 && (
          <Paper sx={{ p: 2, mt: 2, bgcolor: 'action.hover' }}>
            <Typography variant="body2" color="text.secondary">
              📊 {noteList.length} notes total
              {searchTerm && ` • ${filteredNotes.length} matching search`}
            </Typography>
          </Paper>
        )}
      </Drawer>

      {/* Add Note Dialog */}
      <Dialog
        open={showAddDialog}
        onClose={() => setShowAddDialog(false)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>Add New Note</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            fullWidth
            multiline
            rows={4}
            placeholder="Enter your note here..."
            value={newNote}
            onChange={(e) => setNewNote(e.target.value)}
            sx={{ mb: 2 }}
          />
          <TextField
            fullWidth
            size="small"
            placeholder="Tags (comma separated)"
            value={newNoteTags}
            onChange={(e) => setNewNoteTags(e.target.value)}
            helperText="Optional: Add tags to organize your notes"
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setShowAddDialog(false)}>Cancel</Button>
          <Button
            onClick={handleAddNote}
            variant="contained"
            disabled={!newNote.trim()}
          >
            Add Note
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
};

export default NotesPanel;
