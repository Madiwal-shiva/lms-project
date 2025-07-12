import React from 'react';
import {
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Toolbar,
  Divider,
  Box,
} from '@mui/material';
import {
  Dashboard,
  School,
  People,
  Assignment,
  Assessment,
  Settings,
  BookmarkBorder,
  PlayCircleOutline,
  Person,
} from '@mui/icons-material';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

interface SidebarProps {
  open: boolean;
  onClose: () => void;
}

const DRAWER_WIDTH = 240;

const Sidebar: React.FC<SidebarProps> = ({ open, onClose }) => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const getMenuItems = () => {
    const commonItems = [
      { text: 'Dashboard', icon: <Dashboard />, path: '/dashboard' },
    ];

    const adminItems = [
      { text: 'Courses', icon: <School />, path: '/courses' },
      { text: 'Users', icon: <People />, path: '/users' },
      { text: 'Analytics', icon: <Assessment />, path: '/analytics' },
    ];

    const instructorItems = [
      { text: 'My Courses', icon: <School />, path: '/my-courses' },
      { text: 'Create Course', icon: <BookmarkBorder />, path: '/create-course' },
      { text: 'Browse Courses', icon: <School />, path: '/courses' },
    ];

    const studentItems = [
      { text: 'My Courses', icon: <PlayCircleOutline />, path: '/my-courses' },
      { text: 'Browse Courses', icon: <School />, path: '/courses' },
    ];

    const settingsItems = [
      { text: 'Profile', icon: <Person />, path: '/profile' },
      { text: 'Settings', icon: <Settings />, path: '/settings' },
    ];

    let menuItems = [...commonItems];

    if (user?.role === 'admin') {
      menuItems = [...menuItems, ...adminItems];
    } else if (user?.role === 'instructor') {
      menuItems = [...menuItems, ...instructorItems];
    } else if (user?.role === 'student') {
      menuItems = [...menuItems, ...studentItems];
    }

    return { menuItems, settingsItems };
  };

  const { menuItems, settingsItems } = getMenuItems();

  const handleNavigation = (path: string) => {
    navigate(path);
    onClose();
  };

  const isActive = (path: string) => {
    return location.pathname === path;
  };

  return (
    <Drawer
      variant="temporary"
      open={open}
      onClose={onClose}
      ModalProps={{
        keepMounted: true, // Better open performance on mobile.
      }}
      sx={{
        display: { xs: 'block', sm: 'none' },
        '& .MuiDrawer-paper': {
          boxSizing: 'border-box',
          width: DRAWER_WIDTH,
        },
      }}
    >
      <Toolbar />
      <Box sx={{ overflow: 'auto' }}>
        <List>
          {menuItems.map((item) => (
            <ListItem key={item.text} disablePadding>
              <ListItemButton
                selected={isActive(item.path)}
                onClick={() => handleNavigation(item.path)}
                sx={{
                  '&.Mui-selected': {
                    backgroundColor: 'primary.main',
                    color: 'primary.contrastText',
                    '&:hover': {
                      backgroundColor: 'primary.dark',
                    },
                    '& .MuiListItemIcon-root': {
                      color: 'primary.contrastText',
                    },
                  },
                }}
              >
                <ListItemIcon>{item.icon}</ListItemIcon>
                <ListItemText primary={item.text} />
              </ListItemButton>
            </ListItem>
          ))}
        </List>
        
        <Divider />
        
        <List>
          {settingsItems.map((item) => (
            <ListItem key={item.text} disablePadding>
              <ListItemButton
                selected={isActive(item.path)}
                onClick={() => handleNavigation(item.path)}
                sx={{
                  '&.Mui-selected': {
                    backgroundColor: 'primary.main',
                    color: 'primary.contrastText',
                    '&:hover': {
                      backgroundColor: 'primary.dark',
                    },
                    '& .MuiListItemIcon-root': {
                      color: 'primary.contrastText',
                    },
                  },
                }}
              >
                <ListItemIcon>{item.icon}</ListItemIcon>
                <ListItemText primary={item.text} />
              </ListItemButton>
            </ListItem>
          ))}
        </List>
      </Box>
    </Drawer>
  );
};

export default Sidebar;
