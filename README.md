# AnniMemo - Pet Health Management System

A modern, centralized digital workspace for pet owners to organize and monitor the health, nutrition, and daily activities of their beloved pets.

**Version:** 2.0  
**Last Updated:** February 14, 2026  
**Status:** Active Development

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Development](#development)
- [API Documentation](#api-documentation)
- [Contributing](#contributing)
- [License](#license)

---

## 🎯 Overview

**AnniMemo** is a comprehensive pet health management platform designed to help pet owners:
- Keep detailed records of their pets' health metrics
- Track vaccinations, medications, and treatments
- Monitor nutrition and dietary information
- Maintain a timeline of important pet-related activities
- Access pet information anytime, anywhere

The system consists of three components:
- **Backend:** Spring Boot REST API with JWT authentication
- **Frontend:** React.js responsive web interface
- **Mobile:** Android app with Kotlin (planned)

---

## ✨ Features

### ✅ Currently Implemented

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
- Recent activities aggregation from 3 sources:
  - Health metrics from API
  - Pet management actions
  - Local activity logging
- Dynamic timestamps (Today, Yesterday, specific dates)
- Quick action cards for common tasks
- Your Pets section with detailed cards and quick actions

#### 5. Theme System
- Dark/Light mode toggle
- Persistent theme preference (localStorage)
- 34+ CSS variables for consistent styling
- WCAG AA compliant color contrasts
- Seamless theme transitions

#### 6. Footer & Public Pages
- Privacy Policy page
- Terms of Service page
- Cookie Policy page
- Support/Contact page
- Smart routing: authenticated users → /dashboard, guests → /

#### 7. Activity Logging
- Automatic logging of user actions:
  - Pet creation/updates/deletion
  - Health metric logging
  - Profile updates
  - Photo uploads
- Real-time dashboard updates
- Activity history with timestamps

#### 8. UI/UX Enhancements
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

### Frontend
- **Framework:** React.js 18.x
- **Routing:** React Router v6
- **HTTP Client:** Axios
- **State Management:** React Hooks & Context
- **Styling:** CSS-in-JS (CSS Variables)
- **Build Tool:** Create React App / Webpack

### Backend
- **Framework:** Spring Boot 2.x
- **Language:** Java 11+
- **Database:** MySQL 8.0+
- **Security:** JWT (Spring Security)
- **ORM:** Spring Data JPA (Hibernate)
- **Build Tool:** Maven 3.6+

### Infrastructure
- **Deployment:** Render / Railway (planned)
- **Version Control:** Git
- **CI/CD:** GitHub Actions (planned)

---

## 📁 Project Structure

```
IT342_G3_Racaza_Lab1/
├── backend/                          # Spring Boot REST API
│   ├── src/main/java/com/annimemo/
│   │   ├── AnniMemoApplication.java  # Main application class
│   │   ├── config/                   # Configuration classes
│   │   ├── controller/               # REST API endpoints
│   │   ├── service/                  # Business logic
│   │   ├── repository/               # Data access layer
│   │   ├── model/                    # Entity classes
│   │   ├── dto/                      # Data transfer objects
│   │   └── security/                 # JWT & security config
│   ├── src/main/resources/
│   │   └── application.properties    # Configuration
│   └── pom.xml                       # Maven dependencies
│
├── web/                              # React.js Web Frontend
│   ├── public/                       # Static files
│   ├── src/
│   │   ├── components/               # React components
│   │   │   ├── Dashboard.js          # Home dashboard
│   │   │   ├── AddPet.js             # Create pet profile
│   │   │   ├── EditPet.js            # Edit pet profile
│   │   │   ├── UserProfile.js        # User account settings
│   │   │   ├── PetList.js            # View all pets
│   │   │   ├── HealthMetrics.js      # Track health data
│   │   │   ├── LoginPage.js          # Authentication
│   │   │   ├── RegisterPage.js       # User registration
│   │   │   ├── LandingPage.js        # Welcome page
│   │   │   ├── Footer.js             # Footer with links
│   │   │   ├── ThemeToggle.js        # Dark/Light mode
│   │   │   ├── LogoutModal.js        # Logout confirmation
│   │   │   ├── PrivacyPage.js        # Privacy policy
│   │   │   ├── TermsPage.js          # Terms of service
│   │   │   ├── CookiesPage.js        # Cookie policy
│   │   │   └── SupportPage.js        # Contact support
│   │   ├── services/                 # Business logic services
│   │   │   ├── auth.service.js       # Authentication API calls
│   │   │   └── activity.service.js   # Activity logging
│   │   ├── App.js                    # Main app and routing
│   │   ├── index.css                 # Global styles & variables
│   │   └── index.js                  # React entry point
│   ├── package.json                  # NPM dependencies
│   └── .gitignore                    # Git exclusions
│
├── docs/                             # Documentation folder
├── TASK_CHECKLIST.md                 # Development tasks & progress
├── README.md                         # This file
└── .gitignore                        # Global git exclusions
```

---

## 🚀 Getting Started

### Prerequisites

- **Node.js** 14+ and npm
- **Java** 11+ and Maven
- **MySQL** 8.0+
- **Git** for version control

### Installation & Setup

#### 1. Clone the Repository
```bash
git clone <repository-url>
cd IT342_G3_Racaza_Lab1
```

#### 2. Setup Backend (Spring Boot)
```bash
cd backend

# Configure database
# Edit: src/main/resources/application.properties
# Set: spring.datasource.url=jdbc:mysql://localhost:3306/annimemo
# Set: spring.datasource.username=root
# Set: spring.datasource.password=your_password

# Build and run
mvn clean install
mvn spring-boot:run
```

Backend will start at: `http://localhost:8080`

#### 3. Setup Frontend (React)
```bash
cd web

# Install dependencies
npm install

# Start development server
npm start
```

Frontend will open at: `http://localhost:3000`

---

## 💻 Development

### Frontend Development

```bash
cd web

# Run in development mode with hot reload
npm start

# Build for production
npm run build

# Run tests
npm test

# Check for linting errors
npm run lint
```

### Backend Development

```bash
cd backend

# Run with hot reload
mvn clean spring-boot:run

# Run tests
mvn test

# Build production JAR
mvn clean package
```

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
