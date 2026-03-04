# API Testing Guide

This guide demonstrates how to test the User Service API using various tools.

## Table of Contents

1. [Swagger UI (Interactive)](#swagger-ui-interactive)
2. [cURL Commands](#curl-commands)
3. [Postman Collection](#postman-collection)
4. [Testing Scenarios](#testing-scenarios)

## Swagger UI (Interactive)

Once the application is running, the easiest way to test is through the Swagger UI:

1. Open your browser and navigate to: `http://localhost:8080/swagger-ui.html`
2. You'll see all available endpoints with documentation
3. Click on any endpoint to expand it
4. Click "Try it out" button
5. Enter the required parameters/body
6. Click "Execute" to send the request

## cURL Commands

### 1. Register a New User

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "first_name": "John",
    "last_name": "Doe",
    "email": "john.doe@example.com",
    "password": "SecurePass123!",
    "phone": "+1-555-123-4567"
  }'
```

**Expected Response (201 Created):**

```json
{
  "id": 1,
  "first_name": "John",
  "last_name": "Doe",
  "email": "john.doe@example.com",
  "phone": "+1-555-123-4567",
  "role": "USER",
  "status": "ACTIVE",
  "created_at": "2026-03-03T10:30:00",
  "updated_at": "2026-03-03T10:30:00"
}
```

---

### 2. Register Multiple Users for Testing

```bash
# User 2
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "first_name": "Jane",
    "last_name": "Smith",
    "email": "jane.smith@example.com",
    "password": "AnotherPass456!",
    "phone": "+1-555-987-6543"
  }'

# User 3
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "first_name": "Bob",
    "last_name": "Johnson",
    "email": "bob.johnson@example.com",
    "password": "BobPass789!",
    "phone": "+1-555-456-7890"
  }'
```

---

### 3. Login User and Get JWT Token

```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "SecurePass123!"
  }'
```

**Expected Response (200 OK):**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsInJvbGUiOiJVU0VSIiwiZW1haWwiOiJqb2huLmRvZUBleGFtcGxlLmNvbSIsInN1YiI6IjEiLCJpYXQiOjE3NDA4NzQ2MDAsImV4cCI6MTc0MDk2MDk5OX0.ABC123...",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "user": {
    "id": 1,
    "first_name": "John",
    "last_name": "Doe",
    "email": "john.doe@example.com",
    "phone": "+1-555-123-4567",
    "role": "USER",
    "status": "ACTIVE",
    "created_at": "2026-03-03T10:30:00",
    "updated_at": "2026-03-03T10:30:00"
  }
}
```

**Note:** Save the `token` value for authenticated requests.

---

### 4. Get User by ID

```bash
curl -X GET http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json"
```

**Expected Response (200 OK):**

```json
{
  "id": 1,
  "first_name": "John",
  "last_name": "Doe",
  "email": "john.doe@example.com",
  "phone": "+1-555-123-4567",
  "role": "USER",
  "status": "ACTIVE",
  "created_at": "2026-03-03T10:30:00",
  "updated_at": "2026-03-03T10:30:00"
}
```

---

### 5. Get All Users

```bash
curl -X GET http://localhost:8080/api/users \
  -H "Content-Type: application/json"
```

**Expected Response (200 OK):**

```json
[
  {
    "id": 1,
    "first_name": "John",
    "last_name": "Doe",
    "email": "john.doe@example.com",
    "phone": "+1-555-123-4567",
    "role": "USER",
    "status": "ACTIVE",
    "created_at": "2026-03-03T10:30:00",
    "updated_at": "2026-03-03T10:30:00"
  },
  {
    "id": 2,
    "first_name": "Jane",
    "last_name": "Smith",
    "email": "jane.smith@example.com",
    "phone": "+1-555-987-6543",
    "role": "USER",
    "status": "ACTIVE",
    "created_at": "2026-03-03T10:31:00",
    "updated_at": "2026-03-03T10:31:00"
  }
]
```

---

## Testing Error Scenarios

### 1. Register with Duplicate Email

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "first_name": "John",
    "last_name": "Duplicate",
    "email": "john.doe@example.com",
    "password": "Password123!",
    "phone": "+1-555-111-1111"
  }'
```

**Expected Response (400 Bad Request):**

```
Email already registered
```

---

### 2. Login with Invalid Credentials

```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "WrongPassword"
  }'
```

**Expected Response (401 Unauthorized):**

```
Invalid email or password
```

---

### 3. Get Non-Existent User

```bash
curl -X GET http://localhost:8080/api/users/99999 \
  -H "Content-Type: application/json"
```

**Expected Response (404 Not Found):**

```
User not found
```

---

### 4. Register with Missing Required Fields

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "first_name": "John",
    "email": "john.test@example.com"
  }'
```

**Expected Response (400 Bad Request):**

```
Password is required
```

---

## Postman Collection

You can import the following collection JSON into Postman:

```json
{
  "info": {
    "name": "User Service API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Register User",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"first_name\": \"John\",\n  \"last_name\": \"Doe\",\n  \"email\": \"john.doe@example.com\",\n  \"password\": \"SecurePass123!\",\n  \"phone\": \"+1-555-123-4567\"\n}"
        },
        "url": {
          "raw": "http://localhost:8080/api/users/register",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "users", "register"]
        }
      }
    },
    {
      "name": "Login User",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"email\": \"john.doe@example.com\",\n  \"password\": \"SecurePass123!\"\n}"
        },
        "url": {
          "raw": "http://localhost:8080/api/users/login",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "users", "login"]
        }
      }
    },
    {
      "name": "Get User by ID",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "http://localhost:8080/api/users/1",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "users", "1"]
        }
      }
    },
    {
      "name": "Get All Users",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "http://localhost:8080/api/users",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "users"]
        }
      }
    }
  ]
}
```

---

## Testing Scenarios Checklist

- [ ] **User Registration**
    - [ ] Register with valid data
    - [ ] Attempt to register with duplicate email
    - [ ] Register without required fields
    - [ ] Verify user is created with default role "USER" and status "ACTIVE"

- [ ] **User Login**
    - [ ] Login with correct credentials
    - [ ] Login with incorrect password
    - [ ] Login with non-existent email
    - [ ] Verify JWT token is returned with correct structure

- [ ] **Get User by ID**
    - [ ] Get existing user
    - [ ] Attempt to get non-existent user
    - [ ] Verify all user fields are returned

- [ ] **Get All Users**
    - [ ] Verify all registered users are returned
    - [ ] Verify response is a list
    - [ ] Verify pagination (if implemented)

---

## Performance Testing

### Load Testing with Apache Bench

```bash
# Test registration endpoint (10 requests, 5 concurrent)
ab -n 10 -c 5 -T application/json \
   -p register.json \
   http://localhost:8080/api/users/register

# Test login endpoint
ab -n 10 -c 5 -T application/json \
   -p login.json \
   http://localhost:8080/api/users/login

# Test GET all users
ab -n 100 -c 10 http://localhost:8080/api/users
```

---

## Troubleshooting

### Connection Refused

- Ensure the application is running: `./gradlew bootRun`
- Verify it's running on port 8080

### Database Connection Error

- Verify MySQL is running
- Check database credentials in `application.properties`
- Ensure `user_service_db` database exists

### Validation Errors

- Check that email format is valid
- Ensure password meets any complexity requirements
- Verify all required fields are provided

---

## API Documentation

Access the OpenAPI documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs
- **OpenAPI YAML**: http://localhost:8080/api-docs.yaml

