# User Service Microservice

A Spring Boot microservice for user management with JWT-based authentication.

## Features

- **User Registration**: Register new users with validation
- **User Authentication**: Login with email and password
- **JWT Token Generation**: Generate JWT tokens for authenticated users
- **Role Management**: Support for user roles (USER, ADMIN, etc.)
- **User Profile Management**: Retrieve user information and list all users
- **OpenAPI/Swagger Documentation**: Interactive API documentation

## Tech Stack

- **Framework**: Spring Boot 4.0.3
- **Database**: MySQL
- **Authentication**: Spring Security with JWT (jjwt)
- **ORM**: Spring Data JPA with Hibernate
- **API Documentation**: SpringDoc OpenAPI 3.0.2
- **Password Encoding**: BCrypt

## Project Structure

```
src/
├── main/
│   ├── java/com/project/user_service/
│   │   ├── UserServiceApplication.java          # Main application class
│   │   ├── config/
│   │   │   ├── DatabaseConfig.java              # Database & Password Encoder configuration
│   │   │   ├── OpenAPIConfig.java               # OpenAPI/Swagger configuration
│   │   │   └── SecurityConfig.java              # Spring Security configuration
│   │   ├── controller/
│   │   │   └── UserController.java              # REST API endpoints
│   │   ├── dto/
│   │   │   ├── UserRegisterRequest.java         # Registration request DTO
│   │   │   ├── UserLoginRequest.java            # Login request DTO
│   │   │   ├── UserResponse.java                # User response DTO
│   │   │   └── LoginResponse.java               # Login response DTO with token
│   │   ├── entity/
│   │   │   └── User.java                        # User JPA entity
│   │   ├── exception/
│   │   │   └── GlobalExceptionHandler.java      # Global exception handling
│   │   ├── repository/
│   │   │   └── UserRepository.java              # User repository interface
│   │   ├── security/
│   │   │   └── JwtTokenProvider.java            # JWT token generation & validation
│   │   └── service/
│   │       ├── UserService.java                 # User service interface
│   │       └── impl/
│   │           └── UserServiceImpl.java          # User service implementation
│   └── resources/
│       └── application.properties                # Application configuration
├── test/
│   └── java/com/project/user_service/
│       └── UserServiceApplicationTests.java     # Integration tests
└── schema.sql                                   # Database schema
```

## API Endpoints

### 1. User Registration

**POST** `/api/users/register`

Register a new user account.

**Request Body:**

```json
{
  "first_name": "John",
  "last_name": "Doe",
  "email": "john.doe@example.com",
  "password": "password123",
  "phone": "+1234567890"
}
```

**Response:** `201 Created`

```json
{
  "id": 1,
  "first_name": "John",
  "last_name": "Doe",
  "email": "john.doe@example.com",
  "phone": "+1234567890",
  "role": "USER",
  "status": "ACTIVE",
  "created_at": "2026-03-03T10:30:00",
  "updated_at": "2026-03-03T10:30:00"
}
```

### 2. User Login

**POST** `/api/users/login`

Authenticate user and receive JWT token.

**Request Body:**

```json
{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

**Response:** `200 OK`

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "user": {
    "id": 1,
    "first_name": "John",
    "last_name": "Doe",
    "email": "john.doe@example.com",
    "phone": "+1234567890",
    "role": "USER",
    "status": "ACTIVE",
    "created_at": "2026-03-03T10:30:00",
    "updated_at": "2026-03-03T10:30:00"
  }
}
```

### 3. Get User by ID

**GET** `/api/users/{id}`

Retrieve a specific user by their ID.

**Response:** `200 OK`

```json
{
  "id": 1,
  "first_name": "John",
  "last_name": "Doe",
  "email": "john.doe@example.com",
  "phone": "+1234567890",
  "role": "USER",
  "status": "ACTIVE",
  "created_at": "2026-03-03T10:30:00",
  "updated_at": "2026-03-03T10:30:00"
}
```

### 4. Get All Users

**GET** `/api/users`

Retrieve a list of all users.

**Response:** `200 OK`

```json
[
  {
    "id": 1,
    "first_name": "John",
    "last_name": "Doe",
    "email": "john.doe@example.com",
    "phone": "+1234567890",
    "role": "USER",
    "status": "ACTIVE",
    "created_at": "2026-03-03T10:30:00",
    "updated_at": "2026-03-03T10:30:00"
  }
]
```

## Setup Instructions

### Prerequisites

- Java 21+
- MySQL 5.7+
- Gradle 8.0+

### 1. Database Setup

Execute the SQL script to create the database and tables:

```sql
-- Option 1: Run the schema.sql file
mysql -u root -p < schema.sql

-- Option 2: Execute manually
CREATE DATABASE user_service_db;
USE user_service_db;

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100),
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(50) DEFAULT 'USER',
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);
```

### 2. Configure Database Connection

Edit `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/user_service_db
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. Update JWT Secret

Edit `application.properties` and set a strong secret key:

```properties
jwt.secret=your_super_secret_key_change_this_in_production_minimum_32_chars
jwt.expiration=86400000
```

### 4. Build the Application

```bash
./gradlew clean build
```

### 5. Run the Application

```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

## Access Swagger UI

Once the application is running, access the interactive API documentation:

**Swagger UI**: http://localhost:8080/swagger-ui.html

**OpenAPI JSON**: http://localhost:8080/api-docs

## Example Usage

### Register a User

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "first_name": "Jane",
    "last_name": "Smith",
    "email": "jane.smith@example.com",
    "password": "securepass123",
    "phone": "+9876543210"
  }'
```

### Login User

```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "jane.smith@example.com",
    "password": "securepass123"
  }'
```

### Get User by ID

```bash
curl -X GET http://localhost:8080/api/users/1
```

### Get All Users

```bash
curl -X GET http://localhost:8080/api/users
```

## Database Schema

### Users Table

| Column     | Type         | Constraints                 | Description                          |
|------------|--------------|-----------------------------|--------------------------------------|
| id         | BIGINT       | PRIMARY KEY, AUTO_INCREMENT | User unique identifier               |
| first_name | VARCHAR(100) | NOT NULL                    | User's first name                    |
| last_name  | VARCHAR(100) | NULLABLE                    | User's last name                     |
| email      | VARCHAR(150) | NOT NULL, UNIQUE            | User's email address                 |
| password   | VARCHAR(255) | NOT NULL                    | Encrypted password (BCrypt)          |
| phone      | VARCHAR(20)  | NULLABLE                    | User's phone number                  |
| role       | VARCHAR(50)  | DEFAULT 'USER'              | User role (USER, ADMIN, etc.)        |
| status     | VARCHAR(20)  | DEFAULT 'ACTIVE'            | User status (ACTIVE, INACTIVE, etc.) |
| created_at | TIMESTAMP    | NOT NULL                    | Account creation timestamp           |
| updated_at | TIMESTAMP    | NOT NULL                    | Last update timestamp                |

**Indexes:**

- `idx_users_email` on `email` column for faster email lookups

## Configuration Properties

### Database Configuration

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/user_service_db
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

### JWT Configuration

```properties
jwt.secret=your_secret_key_here
jwt.expiration=86400000  # 24 hours in milliseconds
```

### OpenAPI/Swagger Configuration

```properties
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true
```

## Security Considerations

1. **JWT Secret**: Change the default JWT secret in production to a strong, randomly generated key
2. **Password Encoding**: Passwords are hashed using BCrypt with a strength of 10
3. **HTTPS**: Use HTTPS in production environments
4. **Database Credentials**: Store database credentials in environment variables, not in code
5. **Token Expiration**: Default token expiration is 24 hours; adjust as needed

## Error Handling

The API returns appropriate HTTP status codes and error messages:

- **400 Bad Request**: Invalid input data
- **401 Unauthorized**: Authentication failed
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server-side errors

## Future Enhancements

- [ ] Refresh token mechanism
- [ ] Email verification on registration
- [ ] Two-factor authentication (2FA)
- [ ] User role-based access control (RBAC)
- [ ] User profile update endpoint
- [ ] Password reset functionality
- [ ] Activity logging and audit trail
- [ ] Rate limiting and throttling

## License

MIT License

## Support

For issues and questions, please contact the development team.

