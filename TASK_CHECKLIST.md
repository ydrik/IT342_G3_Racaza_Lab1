# AnniMemo - Task Checklist

**Project:** AnniMemo - Pet Health Management System  
**Version:** 2.0  
**Last Updated:** February 14, 2026

---

## ✅ DONE

### Phase 1: Core Features Implementation
- [x] **User Authentication System** - Register, login, logout, profile management
  - Implemented JWT-based authentication
  - Created AuthController, AuthService, UserService
  - Added password hashing with PasswordEncoder
  - Commit: Spring Boot backend setup and JWT security configuration

- [x] **Pet Management Features** - Create, read, update, delete pet profiles
  - Implemented PetController, PetService, PetRepository
  - Added pet profile DTOs (PetRequest, PetResponse)
  - Commit: Pet management REST API endpoints

- [x] **Health Metrics Tracking** - Log and display health metrics
  - Implemented HealthMetricController, HealthMetricService
  - Created HealthMetricRequest and HealthMetricResponse DTOs
  - Commit: Health metrics API and database models

### Phase 2: Bug Fixes & Frontend Enhancement
- [x] **Recent Activities Display** - Fixed activity aggregation logic
  - Created ActivityService with localStorage backend
  - Integrated activity logging from 3 sources (API metrics, pet creation, local actions)
  - Commit: Activity logging and aggregation service

- [x] **Profile Image Upload** - Fixed image upload functionality
  - Implemented image preview in profile and pet components
  - Added local storage for profile photos
  - Commit: Image upload and preview features

- [x] **Pet Creation with Photos** - Fixed pet creation with image support
  - Added image upload in AddPet component
  - Integrated image display in PetList and Dashboard
  - Commit: Pet photo support and storage

### Phase 3: Theme Implementation & UI Enhancements
- [x] **Dark/Light Mode Toggle** - Implemented theme switching
  - Created ThemeToggle component with fixed positioning
  - Added localStorage persistence for theme preference
  - Applied CSS variables across all components
  - Commit: Theme toggle and CSS variable system

- [x] **CSS Variable System** - Established consistent color tokens
  - Created 34+ CSS variables for both light and dark modes
  - Updated all components to use CSS variables
  - Implemented proper color contrast for accessibility
  - Commit: CSS variables and light/dark mode color system

- [x] **Dynamic Recent Activities** - Replaced static activity display
  - Implemented real-time activity logging
  - Added dynamic timestamps (Today, Yesterday, specific dates)
  - Created activity aggregation from multiple sources
  - Commit: Dynamic activity timestamps and aggregation

- [x] **Pet Image Display** - Added image support with fallbacks
  - Implemented 64×64px image containers in Dashboard
  - Added emoji fallbacks for pets without images
  - Integrated image preview in AddPet and EditPet
  - Commit: Pet image display and emoji fallbacks

- [x] **Footer Link Pages** - Created public information pages
  - Implemented PrivacyPage, TermsPage, CookiesPage, SupportPage
  - Added proper routing with React Router Link components
  - Applied theme styling to all footer pages
  - Commit: Footer pages and public routes

- [x] **Syntax Error Fixes** - Fixed all JSX compilation errors
  - Fixed AddPet.js (section/sectionTitle objects)
  - Fixed Dashboard.js (petName/cardTitle/WebkitBackgroundClip)
  - Fixed EditPet.js (textarea border styling)
  - Fixed UserProfile.js (fontSize/color properties)
  - Commit: JSX syntax error fixes and validation

### Phase 4: Bug Fixes & UI Polish
- [x] **Text Visibility in Light Mode** - Fixed contrast issues
  - Fixed UserProfile page headers (My Profile, Back to Dashboard)
  - Updated color values for better WCAG AA compliance
  - Changed hardcoded colors to CSS variables
  - Commit: Text color visibility fixes for light mode

- [x] **Your Pets Section Redesign** - Improved card layout
  - Increased grid columns from 220px to 280px minimum width
  - Enhanced card padding (16px to 20px)
  - Larger pet images (140px to 160px height)
  - Improved button sizing and visibility
  - Commit: Your Pets card redesign and sizing improvements

- [x] **Logout Bug on Public Routes** - Fixed session handling
  - Replaced HTML anchor tags with React Router Link in Footer
  - Prevented full page reloads that cleared localStorage
  - Maintained token persistence during navigation
  - Commit: Footer navigation and token persistence fixes

- [x] **Smart Route Redirection** - Fixed authentication flow
  - Updated footer pages to check for authentication token
  - Redirects authenticated users to /dashboard instead of /
  - Redirects unauthenticated users to /
  - Commit: Intelligent route redirection logic

- [x] **LogoutModal Redesign** - Updated modal styling
  - Changed from gradient to card-based design
  - Updated to use CSS variables for theme compatibility
  - Improved button styling and layout
  - Enhanced accessibility and visual hierarchy
  - Commit: LogoutModal redesign and theming

- [x] **PetList Text Visibility** - Fixed light mode text colors
  - Changed Back to Dashboard button color
  - Applied CSS variables for theme-aware visibility
  - Commit: PetList text color fixes

- [x] **EditPet Text Visibility** - Multiple text color fixes
  - Fixed Back to My Pets button (light mode)
  - Fixed Edit Pet Profile title and subtitle
  - Fixed Notes textarea input color (dark mode)
  - Removed hardcoded backgroundColor conflicts
  - Commit: EditPet text color and textarea fixes

- [x] **Project Cleanup** - Removed unnecessary files
  - Deleted 6 Java crash/replay log files
  - Commit: Project cleanup and log file removal

---

## 🔄 IN-PROGRESS

### Phase 5: API Integration & Backend Connection
- [ ] **Backend API Testing** - Complete API endpoint testing
  - Current status: Using mock data for frontend development
  - Next: Connect to live Spring Boot backend endpoints
  - Endpoints needed: /api/auth/*, /api/pets/*, /api/health-metrics/*, /api/users/*

- [ ] **Database Integration** - Full MySQL connection
  - Current status: Database schema created
  - Next: Test data persistence and retrieval
  - Pending: Deploy database to production

- [ ] **File Upload to Server** - Pet and profile image persistence
  - Current: Images stored in localStorage (temporary)
  - Next: Implement file upload to backend server
  - Storage solution: Cloud storage or server filesystem

---

## 📋 TODO

### Phase 6: Advanced Features
- [ ] **Email Notifications** - Alert users for health metrics
  - Send reminders for vaccine dates
  - Notify for upcoming health checkups
  - Daily digest of pet activities

- [ ] **Data Export** - Export pet records
  - PDF export of complete pet health history
  - CSV export for veterinary records
  - Vaccination certificates generation

- [ ] **Search & Filter** - Pet management enhancements
  - Search pets by name or type
  - Filter health metrics by date range
  - Sort activities by date or type

### Phase 7: Performance & Security
- [ ] **API Rate Limiting** - Prevent abuse
  - Implement rate limiting on backend endpoints
  - Add request throttling for frontend

- [ ] **Data Encryption** - Enhanced security
  - Encrypt sensitive user data in transit
  - Implement field-level encryption for passwords
  - Secure file uploads with virus scanning

- [ ] **Audit Logging** - Track system activities
  - Log all user actions for compliance
  - Track data modifications
  - Generate audit reports

### Phase 8: Mobile & Deployment
- [ ] **Android Mobile App** - Kotlin implementation
  - Replicate web features in mobile app
  - Native mobile-specific features
  - Offline support with local database

- [ ] **Cloud Deployment** - Production deployment
  - Deploy to Render or Railway
  - Configure environment variables
  - Set up CI/CD pipeline (GitHub Actions)

- [ ] **Progressive Web App (PWA)** - Web app enhancement
  - Offline support
  - Install as desktop app
  - Push notifications

### Phase 9: Testing & Documentation
- [ ] **Unit Tests** - Backend test coverage
  - Service layer tests
  - Repository layer tests
  - Controller tests with MockMvc

- [ ] **Integration Tests** - Full API testing
  - End-to-end application workflow
  - Database integration tests
  - Authentication flow verification

- [ ] **API Documentation** - Swagger/OpenAPI setup
  - Auto-generated API documentation
  - Interactive API testing interface
  - Clear endpoint descriptions and examples

---
