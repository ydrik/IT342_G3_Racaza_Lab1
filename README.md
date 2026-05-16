# AnniMemo - Pet Health Management System

A modern, centralized digital workspace for pet owners to organize and monitor the health, nutrition, and daily activities of their beloved pets.

**Version:** 3.0  
**Last Updated:** March 5, 2026  
**Status:** Active Development

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Architecture](#architecture)
- [Development](#development)
- [Contributing](#contributing)

---

## 🎯 Overview

**AnniMemo** is a comprehensive pet health management platform designed to help pet owners:
- Keep detailed records of their pets' health metrics
- Track vaccinations, medications, and treatments
- Monitor nutrition and dietary information
- Access pet breed information from external APIs
- Upload and manage pet photos
- Receive email notifications for important events
- Access pet information anytime, anywhere

The system consists of three components:
- **Backend:** Spring Boot REST API with JWT authentication and OAuth2
- **Frontend:** React.js responsive web interface
- **Mobile:** Android app with Kotlin (in development)

---

## ✨ Features

### ✅ Backend Features (Fully Implemented)

#### 1. Authentication & Security
- ✅ User registration with email validation
- ✅ Login with JWT token-based authentication
- ✅ Google OAuth 2.0 login integration
- ✅ BCrypt password hashing
- ✅ Protected API endpoints
- ✅ /me endpoint for current user details
- ✅ Token-based logout
- ✅ Secure password change with validation

#### 2. Role-Based Access Control (RBAC)
- ✅ Two user roles: ROLE_USER and ROLE_ADMIN
- ✅ API-level role restrictions using @PreAuthorize
- ✅ Admin-only endpoints for user management
- ✅ Role assignment and modification
- ✅ System statistics for administrators

#### 3. Pet Management (Core Business Module)
- ✅ Full CRUD operations for pets
- ✅ Pet profiles (name, species, breed, age, weight, color)
- ✅ Image upload and storage for pets
- ✅ Input validation and error handling
- ✅ User-pet relationship (One-to-Many)
- ✅ Pet-health metrics relationship (One-to-Many)

#### 4. External API Integration
- ✅ Integration with The Dog API (https://api.thedogapi.com)
- ✅ Integration with The Cat API (https://api.thecatapi.com)
- ✅ Breed information lookup (dogs and cats)
- ✅ Breed search functionality
- ✅ Random pet images from external APIs
- ✅ Data displayed in system UI

#### 5. File Upload System
- ✅ Multipart file upload support
- ✅ File storage on server (uploads/pets/)
- ✅ File download/view endpoints
- ✅ File deletion capability
- ✅ Files linked to database records (Pet.imageUrl)
- ✅ Support for images up to 10MB

#### 6. Email/SMTP Integration
- ✅ Welcome email on user registration
- ✅ Health alert email notifications
- ✅ Password change confirmation email
- ✅ Pet added notification email
- ✅ Gmail SMTP integration
- ✅ Asynchronous email sending

#### 7. Architecture & Best Practices
- ✅ Layered Architecture (N-Tier)
- ✅ Controller → Service → Repository pattern
- ✅ Data Transfer Objects (DTOs)
- ✅ Global exception handling
- ✅ RESTful API design
- ✅ Proper HTTP status codes
- ✅ CORS configuration
- ✅ Transaction management

#### 8. Database Design
- ✅ 3 main entities (User, Pet, HealthMetric)
- ✅ Proper relationships (One-to-Many, Many-to-One)
- ✅ No plain-text passwords (BCrypt)
- ✅ Database normalization (3NF)
- ✅ Audit fields (createdAt, updatedAt)
- ✅ MySQL database

### ✅ Frontend Features (Implemented)

#### 1. User Authentication & Profile Management
- Secure user registration with email validation
- Login with JWT token-based authentication
- Personal profile management (name, email, password)
- Profile photo upload and storage
- Account settings and preferences

#### 2. Pet Management
- Create and maintain detailed pet profiles
- Store pet information (species, breed, color, date of birth)
- Update pet profiles with new information
- Delete pet profiles when needed
- Pet photo upload with preview
- Emoji fallbacks for pets without images

#### 3. Health Metrics Tracking
- Log health metrics (weight, vaccines, medications)
- Record health checkup details
- Track vaccination schedules
- Monitor dietary preferences and allergies
- Create timestamped health history

#### 4. Dashboard & Activity Feed
- Home dashboard with personalized welcome
- Recent activities aggregation
- Dynamic timestamps
- Quick action cards for common tasks
- Pet cards with quick actions

#### 5. Theme System
- Dark/Light mode toggle
- Persistent theme preference
- WCAG AA compliant color contrasts
- Seamless theme transitions

#### 6. Footer & Public Pages
- Privacy Policy page
- Terms of Service page
- Cookie Policy page
- Support/Contact page

### ⚠️ Mobile Features (In Development)
- Android application using Kotlin
- XML-Based UI Layouts
- Retrofit for API integration
- JWT authentication
- Pet CRUD operations
- Role-aware UI

---
- Responsive design for desktop and mobile
- Smooth animations and transitions
- Gradient buttons and visual hierarchy
- Message alerts (success/error)
- Loading states and indicators
- Form validation and error handling

### 🔄 In Development

- Live API integration (currently using mock data)
- Database persistence testing
- File upload service for images
- Production deployment setup

### 📋 Planned Features

- Email notifications and reminders
- Data export (PDF, CSV)
- Search and filtering
- Mobile app (Android/Kotlin)
- Progressive Web App (PWA) support
- Advanced analytics
- Veterinary integration
- Multi-pet family sharing

---

## 🛠️ Technology Stack

### Backend
- **Framework:** Spring Boot 3.2.2
- **Language:** Java 25 (compatible with Java 17+)
- **Security:** Spring Security + JWT + OAuth2
- **Database:** MySQL 8.0+
- **ORM:** Spring Data JPA / Hibernate
- **Email:** Spring Mail (SMTP)
- **API Client:** RestTemplate
- **Build Tool:** Maven
- **Authentication:** JWT (io.jsonwebtoken 0.12.3)
- **Password Hashing:** BCrypt

### Frontend
- **Framework:** React.js 18.x
- **Routing:** React Router v6
- **HTTP Client:** Axios
- **State Management:** React Hooks & Context
- **Styling:** CSS-in-JS (CSS Variables)
- **Icons:** Font Awesome / Custom Emojis
### Infrastructure & External Services
- **Database:** MySQL 8.0+
- **Email:** Gmail SMTP
- **External APIs:** The Dog API, The Cat API
- **OAuth:** Google OAuth 2.0
- **File Storage:** Local filesystem
- **Version Control:** Git
- **CI/CD:** GitHub Actions (planned)

### Mobile (In Development)
- **Language:** Kotlin
- **UI:** XML Layouts (Android API 34)
- **HTTP Client:** Retrofit
- **Minimum SDK:** API Level 34 (Android 14)

---

## 📁 Project Structure

```
IT342_G3_Racaza_Lab1/
├── backend/                          # Spring Boot REST API
│   ├── src/main/java/com/annimemo/
│   │   ├── AnniMemoApplication.java  # Main application class
│   │   ├── config/                   # Configuration classes
│   │   │   ├── SecurityConfig.java   # Security & OAuth config
│   │   │   ├── AsyncConfig.java      # Async email config
│   │   │   └── RestTemplateConfig.java # External API client
│   │   ├── controller/               # REST API endpoints
│   │   │   ├── AuthController.java   # Authentication (/me endpoint)
│   │   │   ├── UserController.java   # User management
│   │   │   ├── PetController.java    # Pet CRUD
│   │   │   ├── HealthMetricController.java
│   │   │   ├── AdminController.java  # Admin-only endpoints
│   │   │   ├── FileController.java   # File upload/download
│   │   │   └── BreedController.java  # External API integration
│   │   ├── service/                  # Business logic
│   │   │   ├── AuthService.java
│   │   │   ├── UserService.java
│   │   │   ├── PetService.java
│   │   │   ├── AdminService.java
│   │   │   ├── EmailService.java     # SMTP notifications
│   │   │   ├── FileStorageService.java
│   │   │   └── ExternalApiService.java
│   │   ├── repository/               # Data access layer
│   │   ├── model/                    # Entity classes
│   │   │   ├── User.java (with roles & OAuth)
│   │   │   ├── Pet.java
│   │   │   ├── HealthMetric.java
│   │   │   └── Role.java (enum)
│   │   ├── dto/                      # Data transfer objects
│   │   ├── security/                 # JWT & OAuth security
│   │   │   ├── JwtUtils.java
│   │   │   ├── CustomOAuth2UserService.java
│   │   │   └── OAuth2AuthenticationSuccessHandler.java
│   │   └── exception/                # Global exception handling
│   ├── src/main/resources/
│   │   └── application.properties    # Configuration
│   ├── uploads/pets/                 # File upload storage
│   └── pom.xml                       # Maven dependencies
│
├── web/                              # React.js Web Frontend
│   ├── public/                       # Static files
│   ├── src/
│   │   ├── components/               # React components
│   │   │   ├── Dashboard.js
│   │   │   ├── AddPet.js
│   │   │   ├── EditPet.js
│   │   │   ├── UserProfile.js
│   │   │   ├── PetList.js
│   │   │   ├── HealthMetrics.js
│   │   │   ├── LoginPage.js
│   │   │   ├── RegisterPage.js
│   │   │   ├── LandingPage.js
│   │   │   ├── Footer.js
│   │   │   ├── ThemeToggle.js
│   │   │   ├── LogoutModal.js
│   │   │   ├── PrivacyPage.js
│   │   │   ├── TermsPage.js
│   │   │   ├── CookiesPage.js
│   │   │   └── SupportPage.js
│   │   ├── services/                 # API integration
│   │   │   ├── auth.service.js
│   │   │   └── activity.service.js
│   │   ├── App.js                    # Main app and routing
│   │   ├── index.css                 # Global styles & theme
│   │   └── index.js                  # React entry point
│   ├── package.json                  # NPM dependencies
│   └── .gitignore
│
├── mobile/                           # Android Mobile App (pending)
│
├── docs/                             # Documentation
│   ├── ARCHITECTURE.md               # System architecture & patterns
│   ├── API_DOCUMENTATION.md          # API endpoint reference
│   ├── SETUP_GUIDE.md                # Installation & setup guide
│   └── FEATURES_CHECKLIST.md         # Feature implementation status
│
├── TASK_CHECKLIST.md                 # Development tasks
├── README.md                         # This file
└── .gitignore
```

---

## 🚀 Getting Started

### Prerequisites

- **Java Development Kit (JDK) 17+**
- **Maven 3.6+**
- **MySQL Server 8.0+**
- **Node.js 16+** and npm
- **Git**

### Quick Start

For detailed setup instructions, see [docs/SETUP_GUIDE.md](docs/SETUP_GUIDE.md)

#### 1. Clone the Repository
```bash
git clone <repository-url>
cd IT342_G3_Racaza_Lab1
```

#### 2. Setup Backend (Spring Boot)

```bash
cd backend

# Configure MySQL database
# Create database: dbannimemo
# Edit: src/main/resources/application.properties
# Update database credentials, email settings, OAuth credentials

# Build and run
mvn clean install
mvn spring-boot:run
```

Backend API will start on `http://localhost:8080`

#### 3. Setup Frontend (React)
```bash
cd web
npm install
npm start
```

Frontend will open on `http://localhost:3000`

#### 4. Create Admin User (Optional)
```sql
UPDATE users SET role = 'ROLE_ADMIN' WHERE username = 'your-username';
```

---

## 📚 API Documentation

### Authentication Endpoints
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user
- `GET /api/auth/me` - Get current user (JWT required)

### User Management
- `GET /api/users/profile` - Get user profile
- `PUT /api/users/profile` - Update profile
- `PUT /api/users/password` - Change password

### Pet Management (Core CRUD)
- `GET /api/pets` - Get all user's pets
- `GET /api/pets/{id}` - Get pet by ID
- `POST /api/pets` - Create new pet
- `PUT /api/pets/{id}` - Update pet
- `DELETE /api/pets/{id}` - Delete pet

### Health Metrics
- `GET /api/health-metrics` - Get health records
- `POST /api/health-metrics` - Add health record
- `GET /api/health-metrics/pet/{petId}` - Get pet's health history

### File Upload
- `POST /api/files/upload` - Upload file (multipart/form-data)
- `GET /api/files/download/{fileName}` - Download file
- `DELETE /api/files/{fileName}` - Delete file

### External API Integration
- `GET /api/breeds/dogs` - Get all dog breeds
- `GET /api/breeds/cats` - Get all cat breeds
- `GET /api/breeds/dogs/search?name={name}` - Search dog breed
- `GET /api/breeds/cats/search?name={name}` - Search cat breed
- `GET /api/breeds/dogs/random-image` - Get random dog image
- `GET /api/breeds/cats/random-image` - Get random cat image

### Admin Endpoints (ROLE_ADMIN Required)
- `GET /api/admin/users` - Get all users
- `PUT /api/admin/users/{id}/role?role={ROLE}` - Update user role
- `DELETE /api/admin/users/{id}` - Delete user
- `GET /api/admin/stats` - Get system statistics

### OAuth2
- `GET /oauth2/authorization/google` - Initiate Google OAuth login

For detailed API documentation, see [docs/API_DOCUMENTATION.md](docs/API_DOCUMENTATION.md)

---

## 🏗️ Architecture

AnniMemo follows a **Layered Architecture (N-Tier)** pattern with clear separation of concerns:

1. **Presentation Layer** - Controllers handling HTTP requests
2. **Service Layer** - Business logic and orchestration
3. **Data Access Layer** - JPA repositories
4. **Domain Layer** - Entity models
5. **Security Layer** - JWT and OAuth2 authentication
6. **Configuration Layer** - Application configuration
7. **Exception Handling** - Global error handling

For detailed architecture documentation, see [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)

---

## 💻 Development

### Backend Development

```bash
cd backend

# Run with hot reload (Spring Boot DevTools)
mvn spring-boot:run

# Build without tests
mvn clean install -DskipTests

# Run tests
mvn test
```

### Frontend Development

```bash
cd web

# Run in development mode with hot reload
npm start

# Build for production
npm run build

# Run tests
npm test
```

### Testing

#### Test Authentication
```bash
# Register a new user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"Test123!","firstName":"Test","lastName":"User","email":"test@test.com"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"Test123!"}'

# Use the returned JWT token for authenticated requests
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 📋 Feature Implementation Status

| Feature Category | Status | Notes |
|-----------------|--------|-------|
| Authentication & Security | ✅ Complete | JWT, OAuth2, BCrypt |
| Role-Based Access Control | ✅ Complete | USER & ADMIN roles |
| Pet CRUD Operations | ✅ Complete | Full CRUD with validation |
| External API Integration | ✅ Complete | Dog & Cat APIs |
| File Upload | ✅ Complete | Image storage & retrieval |
| Email Notifications | ✅ Complete | SMTP integration |
| Global Exception Handling | ✅ Complete | Centralized error handling |
| Architecture Documentation | ✅ Complete | Layered architecture |
| Mobile Application | ⚠️ Pending | Android development needed |
| Payment Gateway | ⏭️ Optional | Not yet implemented |
| Real-time Features | ⏭️ Optional | WebSocket not implemented |

See [docs/FEATURES_CHECKLIST.md](docs/FEATURES_CHECKLIST.md) for detailed status.

---

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📝 License

This project is part of IT342 coursework.

---

## 👥 Team

**Group 3 - Racaza**  
IT342 - System Integration and Architecture

---

## 📞 Support

For issues and questions:
- Check [docs/SETUP_GUIDE.md](docs/SETUP_GUIDE.md)
- Review [docs/API_DOCUMENTATION.md](docs/API_DOCUMENTATION.md)
- Contact the development team

---

## 🎯 Project Status

**Current Version:** 3.0  
**Status:** Active Development  
**Last Updated:** March 5, 2026

**Completed:**
- ✅ Backend API with all required features
- ✅ Web frontend with responsive design
- ✅ Documentation (Architecture, API, Setup)
- ✅ Email notifications
- ✅ OAuth2 integration
- ✅ File upload system
- ✅ External API integration

**In Progress:**
- 🔄 Mobile application (Android)

**Planned:**
- 📋 Payment gateway integration
- 📋 WebSocket real-time features
- 📋 Production deployment

---

**Built with ❤️ by the AnniMemo Team**

### Available Routes

**Public Routes:**
- `/` - Landing page
- `/login` - User login
- `/register` - User registration
- `/privacy` - Privacy policy
- `/terms` - Terms of service
- `/cookies` - Cookie policy
- `/support` - Support/contact

**Protected Routes (Requires Login):**
- `/dashboard` - Home dashboard
- `/profile` - User profile settings
- `/pets` - List all pets
- `/pets/add` - Create new pet
- `/pets/edit/:id` - Edit pet profile
- `/pets/:id/health` - Pet health metrics

---

## 🐛 Known Issues & Limitations

1. **Backend Integration:** Currently using mock data for frontend development
   - Status: Testing live API integration
   - Workaround: Update endpoints when backend is ready

2. **File Upload:** Images stored in localStorage (limited capacity)
   - Solution: Implement backend file upload service

3. **Database:** Testing with MySQL required
   - Ensure MySQL server is running
   - Check connection parameters in `application.properties`

---

## 🔒 Security Features

- JWT token-based authentication
- Password hashing with BCryptPasswordEncoder
- CORS configuration for frontend communication
- SQL injection prevention via parameterized queries
- XSS protection through React's automatic escaping
- HTTPS enforcement on production (configured on deployment)

---

## 📈 Performance Optimizations

- Lazy loading of components via React.lazy()
- CSS variable system for fast theme switching
- Optimized image loading with fallbacks
- Efficient state management with React Hooks
- API response caching where applicable

## 📝 Commit Policy

All DONE tasks in TASK_CHECKLIST.md must include commit hashes for traceability and version control.

---

## 📜 License

This project is developed for **IT342 - Systems Integration and Architecture 1** at **Cebu Institute of Technology University**.

**Prepared By:** Racaza, Cydric Luis C.  
**Date:** February 7, 2026  
**Version:** 2.0 (Functional Requirements Specification)

---

## 🎉 Acknowledgments

- **Spring Boot Documentation** for backend framework guidance
- **React.js Community** for excellent documentation
- **Material Design** for UI/UX inspiration
- **CITU Faculty** for project oversight and guidance

---
