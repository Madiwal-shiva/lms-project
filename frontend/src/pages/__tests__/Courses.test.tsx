import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import Courses from '../Courses';
import { AuthProvider } from '../../context/AuthContext';
import * as api from '../../services/api';

jest.mock('../../services/api');
const mockedApi = api as jest.Mocked<typeof api>;

const mockCourses = [
  {
    id: 1,
    title: 'React Basics',
    description: 'Learn React fundamentals',
    shortDescription: 'React course',
    instructor: {
      id: 1,
      firstName: 'Jane',
      lastName: 'Smith'
    },
    category: 'Programming',
    level: 'Beginner',
    durationHours: 10,
    price: 99.99,
    rating: 4.5,
    enrollmentCount: 100,
    thumbnailUrl: 'test-image.jpg'
  }
];

const renderWithProviders = (component: React.ReactElement) => {
  return render(
    <BrowserRouter>
      <AuthProvider>
        {component}
      </AuthProvider>
    </BrowserRouter>
  );
};

describe('Courses Component', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('renders courses list', async () => {
    mockedApi.api.get.mockResolvedValue({
      data: { content: mockCourses }
    });

    renderWithProviders(<Courses />);

    expect(screen.getByText('Available Courses')).toBeInTheDocument();
    
    await waitFor(() => {
      expect(screen.getByText('React Basics')).toBeInTheDocument();
    });
  });

  test('handles search functionality', async () => {
    mockedApi.api.get.mockResolvedValue({
      data: { content: mockCourses }
    });

    renderWithProviders(<Courses />);

    const searchInput = screen.getByPlaceholderText('Search courses...');
    const searchButton = screen.getByText('Search');

    fireEvent.change(searchInput, { target: { value: 'React' } });
    fireEvent.click(searchButton);

    await waitFor(() => {
      expect(mockedApi.api.get).toHaveBeenCalledWith('/courses/search?query=React');
    });
  });
});