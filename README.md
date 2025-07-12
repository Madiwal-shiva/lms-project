# Learning Management System (LMS)

A comprehensive Learning Management System built with Spring Boot backend and React frontend, featuring role-based access control, progress tracking, and analytics.

## Features

### Instructor Features
- Create and manage courses
- Upload course content and materials
- Create different learning paths
- Track individual and group student progress
- View analytics with filters (date, month, year, location, city, country)
- Manage course enrollments
- Monitor student performance

### Student Features
- Browse and search available courses
- Register and enroll in courses
- Access course materials
- Save progress and resume later
- Track learning progress
- View enrolled courses dashboard

### Authentication & Security
- Google OAuth2 integration
- Role-based access control (Instructor/Student)
- Secure API endpoints
- Session management

### Technical Features
- Comprehensive exception handling
- Standard HTTP error codes
- Input validation (frontend & backend)
- Unit tests with 80%+ code coverage
- API documentation with Swagger/OpenAPI
- Externalized configuration
- Database-driven user management
- RESTful API design
- Static code analysis ready
- Reusable and extensible architecture

## Technology Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.2.0** - Application framework
- **Spring Security** - Authentication & authorization
- **Spring Data JPA** - Data persistence
- **OAuth2** - Google authentication
- **MySQL 8.0** - Database
- **Maven** - Build tool
- **JUnit 5** - Testing framework
- **Mockito** - Mocking framework
- **JaCoCo** - Code coverage
- **Swagger/OpenAPI** - API documentation
- **Bean Validation** - Input validation

### Frontend
- **React 18** - UI framework
- **TypeScript** - Type safety
- **Material-UI (MUI)** - Component library
- **React Router** - Navigation
- **Axios** - HTTP client
- **Vite** - Build tool
- **Jest** - Testing framework
- **React Testing Library** - Component testing

## Prerequisites

- **Java 17+** - Required for Spring Boot 3.x
- **Node.js 16+** - For React development
- **MySQL 8.0+** - Database server
- **Maven 3.6+** - Build automation
- **Git** - Version control
- **Google Cloud Account** - For OAuth2 setup

## Quick Start

### 1. Database Setup
```sql
-- Create database
CREATE DATABASE lms_db;

-- Create user (optional)
CREATE USER 'lms_user'@'localhost' IDENTIFIED BY 'lms_password';
GRANT ALL PRIVILEGES ON lms_db.* TO 'lms_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Google OAuth2 Setup
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create/select project
3. Enable Google+ API
4. Create OAuth 2.0 credentials
5. Add authorized redirect URI: `http://localhost:8080/api/login/oauth2/code/google`
6. Note Client ID and Client Secret

### 3. Environment Configuration
Create `.env` file in project root:
```env
DB_USERNAME=lms_user
DB_PASSWORD=lms_password
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
JWT_SECRET=your-jwt-secret-key-minimum-32-characters
```

### 4. Backend Setup
```bash
cd backend
mvn clean install
mvn spring-boot:run
```
Backend runs on: `http://localhost:8080`

### 5. Frontend Setup
```bash
cd frontend
npm install
npm run dev
```
Frontend runs on: `http://localhost:5173`

## API Documentation

Access Swagger UI: `http://localhost:8080/swagger-ui/index.html`

### Key API Endpoints

#### Authentication
- `GET /auth/me` - Get current user
- `POST /auth/register` - Register new user
- `POST /auth/logout` - Logout

#### Courses
- `GET /courses` - Get all courses
- `GET /courses/published` - Get published courses
- `POST /courses` - Create course (Instructor only)
- `PUT /courses/{id}` - Update course
- `DELETE /courses/{id}` - Delete course

#### Enrollments
- `POST /enrollments/enroll/{courseId}` - Enroll in course
- `GET /enrollments/my-courses` - Get enrolled courses
- `DELETE /enrollments/unenroll/{courseId}` - Unenroll

#### Progress Tracking
- `POST /progress` - Save progress
- `GET /progress/course/{courseId}` - Get course progress
- `GET /progress/analytics/location` - Progress by location
- `GET /progress/analytics/monthly/{year}` - Monthly progress

## Testing

### Backend Tests
```bash
cd backend

# Run tests
mvn test

# Generate coverage report
mvn test jacoco:report

# View coverage: target/site/jacoco/index.html
```

### Frontend Tests
```bash
cd frontend

# Run tests
npm test

# Run with coverage
npm test -- --coverage

# View coverage: coverage/lcov-report/index.html
```

### Code Quality
```bash
# Backend static analysis
mvn spotbugs:check

# Frontend linting
npm run lint
```

## Project Structure

```
lms-project/
├── backend/
│   ├── src/main/java/com/lms/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # JPA entities
│   │   ├── enums/          # Enumerations
│   │   ├── exception/      # Exception handling
│   │   ├── repository/     # Data repositories
│   │   ├── service/        # Business logic
│   │   └── LmsApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   ├── src/test/           # Unit tests
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── components/     # Reusable components
│   │   ├── context/        # React contexts
│   │   ├── pages/          # Page components
│   │   ├── services/       # API services
│   │   ├── types/          # TypeScript types
│   │   ├── utils/          # Utility functions
│   │   └── App.tsx
│   ├── __tests__/          # Unit tests
│   └── package.json
├── database/
│   └── init.sql           # Database initialization
├── setup.md               # Detailed setup guide
└── README.md
```

## Key Features Implementation

### 1. Role-Based Access Control
- Instructors can create courses and track progress
- Students can enroll and access courses
- Secure endpoints with proper authorization

### 2. Progress Tracking
- Individual student progress monitoring
- Group analytics and reporting
- Resume functionality for interrupted learning

### 3. Analytics & Filtering
- Filter by date ranges (day, month, year)
- Location-based analytics (city, country)
- Performance metrics and statistics

### 4. Validation & Error Handling
- Comprehensive input validation
- Proper HTTP status codes
- User-friendly error messages
- Global exception handling

### 5. Testing & Quality
- Unit tests for all major components
- Integration tests for API endpoints
- Code coverage reports
- Static code analysis

## Deployment

### Production Configuration
1. Update `application.properties` for production
2. Configure production database
3. Set up proper OAuth2 redirect URIs
4. Enable HTTPS
5. Configure CORS for production domains

### Docker Deployment (Optional)
```bash
# Build and run with Docker Compose
docker-compose up --build
```

## Troubleshooting

### Common Issues
1. **Database Connection**: Verify MySQL is running and credentials are correct
2. **OAuth2 Issues**: Check Google Cloud Console configuration
3. **CORS Errors**: Verify frontend/backend URLs in configuration
4. **Port Conflicts**: Ensure ports 8080 and 5173 are available

### Support
For issues and questions:
1. Check the setup guide: `setup.md`
2. Review API documentation
3. Check application logs
4. Verify environment configuration

## Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

### Development Guidelines
- Follow existing code style
- Add unit tests for new features
- Update documentation
- Ensure all tests pass
- Maintain code coverage above 80%

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Spring Boot team for the excellent framework
- React team for the powerful UI library
- Material-UI for beautiful components
- Google for OAuth2 integration
#   l m s - p r o j e c t 
 
 
