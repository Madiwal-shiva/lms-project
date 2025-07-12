# LMS Setup Guide

## Prerequisites
- Java 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

## Database Setup

1. Install MySQL and start the service
2. Create database:
```sql
CREATE DATABASE lms_db;
CREATE USER 'lms_user'@'localhost' IDENTIFIED BY 'lms_password';
GRANT ALL PRIVILEGES ON lms_db.* TO 'lms_user'@'localhost';
FLUSH PRIVILEGES;
```

## Google OAuth Setup

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing
3. Enable Google+ API
4. Create OAuth 2.0 credentials
5. Add authorized redirect URI: `http://localhost:8080/api/login/oauth2/code/google`
6. Note down Client ID and Client Secret

## Environment Configuration

Create `.env` file in project root:
```
DB_USERNAME=lms_user
DB_PASSWORD=lms_password
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
JWT_SECRET=your-jwt-secret-key-at-least-32-characters
```

## Backend Setup

1. Navigate to backend directory:
```bash
cd backend
```

2. Install dependencies:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

Backend will start on `http://localhost:8080`

## Frontend Setup

1. Navigate to frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start development server:
```bash
npm run dev
```

Frontend will start on `http://localhost:5173`

## First Time Setup

1. Access `http://localhost:5173`
2. Click "Sign in with Google"
3. Complete Google OAuth flow
4. You'll be redirected to the dashboard

## Features Available

### For Students:
- Browse available courses
- Enroll in courses
- View enrolled courses
- Track progress

### For Instructors:
- Create new courses
- Manage existing courses
- View student enrollments
- Track course statistics

## API Documentation

Once backend is running, access Swagger UI at:
`http://localhost:8080/swagger-ui/index.html`

## Troubleshooting

### Database Connection Issues
- Ensure MySQL is running
- Check database credentials in application.properties
- Verify database exists

### OAuth Issues
- Check Google OAuth credentials
- Verify redirect URI matches exactly
- Ensure Google+ API is enabled

### CORS Issues
- Frontend and backend must run on specified ports
- Check CORS configuration in SecurityConfig

### Port Conflicts
- Backend: Change `server.port` in application.properties
- Frontend: Change port in vite.config.ts