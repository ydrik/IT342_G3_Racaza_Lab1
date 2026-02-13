# AnniMemo Backend

Spring Boot REST API for the AnniMemo Pet Health Tracking System.

## Tech Stack

- **Java**: 17
- **Spring Boot**: 3.2.2
- **Spring Security**: JWT Authentication
- **Spring Data JPA**: Database ORM
- **MySQL**: Database
- **Maven**: Build tool
- **Lombok**: Reduce boilerplate code

## Prerequisites

1. **Java 17** or higher installed
2. **Maven** installed
3. **MySQL** server running on `localhost:3306`
4. Create database: `CREATE DATABASE dbannimemo;`

## Configuration

Database and JWT settings are in `src/main/resources/application.properties`:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/dbannimemo
spring.datasource.username=root
spring.datasource.password=

# JWT
jwt.secret=anniMemoSecretKeyForJWTTokenGenerationAndValidation2024
jwt.expiration=86400000
```

## Running the Application

### Using Maven

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The server will start on `http://localhost:8080`

### Using IDE

Open the project in IntelliJ IDEA or Eclipse and run `AnniMemoApplication.java`

## API Endpoints

### Authentication (Public)

- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login and get JWT token

### User Profile (Protected)

- `GET /api/users/profile` - Get current user profile
- `PUT /api/users/profile` - Update profile information
- `PUT /api/users/password` - Change password

### Pet Management (Protected)

- `GET /api/pets` - Get all pets for current user
- `GET /api/pets/{id}` - Get specific pet
- `POST /api/pets` - Create new pet
- `PUT /api/pets/{id}` - Update pet
- `DELETE /api/pets/{id}` - Delete pet

### Health Metrics (Protected)

- `GET /api/pets/{petId}/health` - Get all health metrics for a pet
- `GET /api/pets/{petId}/health?type={type}` - Get health metrics by type (weight, medication, vaccination, vet_visit)
- `POST /api/pets/{petId}/health` - Add new health metric
- `GET /api/activities/recent` - Get recent health activities

## Authentication

Protected endpoints require a JWT token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

Token is returned after successful login/registration.

## Project Structure

```
backend/
├── src/main/java/com/annimemo/
│   ├── AnniMemoApplication.java        # Main application class
│   ├── config/
│   │   └── SecurityConfig.java         # Spring Security & CORS configuration
│   ├── controller/
│   │   ├── AuthController.java         # Authentication endpoints
│   │   ├── UserController.java         # User profile endpoints
│   │   ├── PetController.java          # Pet management endpoints
│   │   └── HealthMetricController.java # Health metric endpoints
│   ├── dto/
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   ├── AuthResponse.java
│   │   ├── UserProfileResponse.java
│   │   ├── UpdateProfileRequest.java
│   │   ├── ChangePasswordRequest.java
│   │   ├── PetRequest.java
│   │   ├── PetResponse.java
│   │   ├── HealthMetricRequest.java
│   │   ├── HealthMetricResponse.java
│   │   └── MessageResponse.java
│   ├── model/
│   │   ├── User.java                   # User entity
│   │   ├── Pet.java                    # Pet entity
│   │   └── HealthMetric.java           # Health metric entity
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── PetRepository.java
│   │   └── HealthMetricRepository.java
│   ├── security/
│   │   ├── JwtUtils.java               # JWT token generation/validation
│   │   ├── AuthTokenFilter.java        # JWT authentication filter
│   │   ├── AuthEntryPointJwt.java      # Unauthorized handler
│   │   ├── UserDetailsImpl.java        # UserDetails implementation
│   │   └── UserDetailsServiceImpl.java # Load user for authentication
│   └── service/
│       ├── AuthService.java            # Authentication business logic
│       ├── UserService.java            # User profile business logic
│       ├── PetService.java             # Pet management business logic
│       └── HealthMetricService.java    # Health metric business logic
├── src/main/resources/
│   └── application.properties          # Application configuration
└── pom.xml                             # Maven dependencies
```

## Database Schema

The application uses Spring Data JPA with `ddl-auto=update`, so tables will be created automatically on first run:

- `users` - User accounts
- `pets` - Pet profiles
- `health_metrics` - Health tracking records

## CORS Configuration

CORS is enabled for `http://localhost:3000` (React frontend).

## Notes

- Passwords are hashed using BCrypt
- JWT tokens expire after 24 hours
- Session management is stateless
- All protected endpoints validate JWT tokens
