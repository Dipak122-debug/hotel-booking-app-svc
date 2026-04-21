# Upsert Endpoint Documentation

## Overview

The Upsert endpoint provides a unified way to either insert a new user or update an existing user using a single
endpoint. This leverages Hibernate's merge functionality for efficient database operations.

## Endpoint Details

**HTTP Method**: `PUT`  
**URL**: `/api/users/upsert`  
**Authentication**: Not required (can be configured)  
**Content-Type**: `application/json`

## Request

### Request Body

```json
{
  "id": null,
  "first_name": "John",
  "last_name": "Doe",
  "email": "john.doe@example.com",
  "password": "SecurePassword123!",
  "phone": "+1-555-123-4567",
  "role": "USER",
  "status": "ACTIVE"
}
```

### Field Descriptions

| Field      | Type   | Required    | Description                                                                             |
|------------|--------|-------------|-----------------------------------------------------------------------------------------|
| id         | Long   | No          | User ID. If provided and exists, updates the user. If null/omitted, creates a new user. |
| first_name | String | Yes         | User's first name                                                                       |
| last_name  | String | No          | User's last name                                                                        |
| email      | String | Yes         | User's email address (must be unique)                                                   |
| password   | String | Conditional | Required for new users. Optional for updates (if provided, updates password).           |
| phone      | String | No          | User's phone number                                                                     |
| role       | String | No          | User role. Defaults to "USER" for new users if not specified.                           |
| status     | String | No          | User status. Defaults to "ACTIVE" for new users if not specified.                       |

## Usage Scenarios

### Scenario 1: Create New User (Insert)

Omit the `id` field to create a new user.

**Request**:

```bash
curl -X PUT http://localhost:8080/api/users/upsert \
  -H "Content-Type: application/json" \
  -d '{
    "first_name": "Jane",
    "last_name": "Smith",
    "email": "jane.smith@example.com",
    "password": "NewPassword456!",
    "phone": "+1-555-987-6543",
    "role": "USER",
    "status": "ACTIVE"
  }'
```

**Response** (201 Created):

```json
{
  "id": 5,
  "first_name": "Jane",
  "last_name": "Smith",
  "email": "jane.smith@example.com",
  "phone": "+1-555-987-6543",
  "role": "USER",
  "status": "ACTIVE",
  "created_at": "2026-03-03T14:30:00",
  "updated_at": "2026-03-03T14:30:00"
}
```

---

### Scenario 2: Update Existing User

Provide the user `id` to update an existing user. Password is optional during updates.

**Request** (Update without changing password):

```bash
curl -X PUT http://localhost:8080/api/users/upsert \
  -H "Content-Type: application/json" \
  -d '{
    "id": 5,
    "first_name": "Jane",
    "last_name": "Johnson",
    "email": "jane.johnson@example.com",
    "phone": "+1-555-111-2222",
    "role": "ADMIN",
    "status": "ACTIVE"
  }'
```

**Response** (200 OK):

```json
{
  "id": 5,
  "first_name": "Jane",
  "last_name": "Johnson",
  "email": "jane.johnson@example.com",
  "phone": "+1-555-111-2222",
  "role": "ADMIN",
  "status": "ACTIVE",
  "created_at": "2026-03-03T14:30:00",
  "updated_at": "2026-03-03T14:35:00"
}
```

---

### Scenario 3: Update with Password Change

Provide both `id` and new `password` to update both user information and password.

**Request**:

```bash
curl -X PUT http://localhost:8080/api/users/upsert \
  -H "Content-Type: application/json" \
  -d '{
    "id": 5,
    "first_name": "Jane",
    "last_name": "Johnson",
    "email": "jane.johnson@example.com",
    "password": "NewSecurePass789!",
    "phone": "+1-555-111-2222",
    "role": "ADMIN",
    "status": "ACTIVE"
  }'
```

**Response** (200 OK):

```json
{
  "id": 5,
  "first_name": "Jane",
  "last_name": "Johnson",
  "email": "jane.johnson@example.com",
  "phone": "+1-555-111-2222",
  "role": "ADMIN",
  "status": "ACTIVE",
  "created_at": "2026-03-03T14:30:00",
  "updated_at": "2026-03-03T14:35:00"
}
```

---

## Response Codes

| Code | Description                                          |
|------|------------------------------------------------------|
| 201  | Created - New user has been created successfully     |
| 200  | OK - Existing user has been updated successfully     |
| 400  | Bad Request - Invalid input data or validation error |
| 404  | Not Found - User ID provided but user doesn't exist  |
| 500  | Internal Server Error - Server-side error            |

## Error Responses

### Missing Required Field

```json
"First name is required"
```

### Duplicate Email on Create

```json
"Email already registered"
```

### Email Change to Existing Email

```json
"Email already registered"
```

### User Not Found (Update)

```json
"User not found with ID: 99999"
```

### Invalid Request Body

```json
"Invalid input or validation error"
```

---

## Implementation Details

### How It Works

1. **Check if ID is provided**:
    - If `id` is null or <= 0 → Insert operation
    - If `id` > 0 → Update operation

2. **For Insert Operations**:
    - Validates all required fields (first_name, email, password)
    - Checks if email already exists
    - Creates new User entity
    - Sets defaults: role = "USER", status = "ACTIVE"
    - Saves to database

3. **For Update Operations**:
    - Retrieves existing user by ID
    - Validates all required fields (first_name, email)
    - Checks if email is being changed to an already registered email
    - Updates only provided fields
    - Password is only updated if provided and non-empty
    - Role and status update only if provided and non-empty
    - Uses Hibernate merge to update the entity

### Hibernate Merge Pattern

The implementation uses Spring Data JPA's `save()` method, which leverages Hibernate's merge functionality:

```java
// For new users: Persists the entity
User savedUser = userRepository.save(user);  // INSERT

// For existing users: Merges the entity with the persistence context
User savedUser = userRepository.save(user);  // UPDATE
```

This is more efficient than separate insert/update logic because:

- ✅ Single operation for both scenarios
- ✅ Automatic timestamp management
- ✅ Change tracking automatically triggers the right SQL operation
- ✅ No manual state management needed

---

## Best Practices

### 1. Creating New Users

```json
{
  "first_name": "John",
  "last_name": "Doe",
  "email": "john@example.com",
  "password": "SecurePass123!",
  "phone": "+1-555-0000"
}
```

**Note**: Omit `id`, `role`, and `status` fields for defaults.

### 2. Updating User Profile

```json
{
  "id": 1,
  "first_name": "John",
  "last_name": "Doe",
  "email": "john.newemail@example.com",
  "phone": "+1-555-1111"
}
```

**Note**: Password is optional. Only include it if you want to change the password.

### 3. Granting Admin Role

```json
{
  "id": 1,
  "first_name": "John",
  "last_name": "Doe",
  "email": "john@example.com",
  "role": "ADMIN"
}
```

### 4. Deactivating User Account

```json
{
  "id": 1,
  "first_name": "John",
  "last_name": "Doe",
  "email": "john@example.com",
  "status": "INACTIVE"
}
```

---

## Validation Rules

| Field      | Validation                                   |
|------------|----------------------------------------------|
| first_name | Required, non-empty                          |
| last_name  | Optional                                     |
| email      | Required, non-empty, must be unique          |
| password   | Required for new users, optional for updates |
| phone      | Optional                                     |
| role       | Optional (defaults to "USER" on insert)      |
| status     | Optional (defaults to "ACTIVE" on insert)    |

---

## cURL Examples

### Example 1: Create New User

```bash
curl -X PUT http://localhost:8080/api/users/upsert \
  -H "Content-Type: application/json" \
  -d '{
    "first_name": "Alice",
    "last_name": "Wonder",
    "email": "alice@example.com",
    "password": "AlicePass123!",
    "phone": "+1-555-2222"
  }' \
  -w "\nHTTP Status: %{http_code}\n"
```

### Example 2: Update Existing User

```bash
curl -X PUT http://localhost:8080/api/users/upsert \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "first_name": "Alice",
    "last_name": "Wonderland",
    "email": "alice.new@example.com",
    "phone": "+1-555-3333"
  }' \
  -w "\nHTTP Status: %{http_code}\n"
```

### Example 3: Change User Password

```bash
curl -X PUT http://localhost:8080/api/users/upsert \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "first_name": "Alice",
    "last_name": "Wonderland",
    "email": "alice.new@example.com",
    "password": "NewAlicePass456!"
  }' \
  -w "\nHTTP Status: %{http_code}\n"
```

---

## Postman Examples

**1. Create New User**

```
Method: PUT
URL: http://localhost:8080/api/users/upsert
Headers: Content-Type: application/json

Body:
{
  "first_name": "Bob",
  "last_name": "Builder",
  "email": "bob@example.com",
  "password": "BobPass123!",
  "phone": "+1-555-4444"
}
```

**2. Update User**

```
Method: PUT
URL: http://localhost:8080/api/users/upsert
Headers: Content-Type: application/json

Body:
{
  "id": 1,
  "first_name": "Bob",
  "last_name": "BuilderX",
  "email": "bob.x@example.com",
  "phone": "+1-555-5555",
  "role": "ADMIN"
}
```

---

## Database Operations

### Insert Operation (SQL)

```sql
INSERT INTO users (first_name, last_name, email, password, phone, role, status, created_at, updated_at)
VALUES ('John', 'Doe', 'john@example.com', 'hashed_password', '+1-555-0000', 'USER', 'ACTIVE', NOW(), NOW());
```

### Update Operation (SQL)

```sql
UPDATE users 
SET first_name = 'John', last_name = 'Smith', email = 'john.smith@example.com', 
    phone = '+1-555-1111', role = 'ADMIN', status = 'ACTIVE', updated_at = NOW()
WHERE id = 1;
```

---

## Performance Considerations

- **Single Query Operation**: Both insert and update use the same endpoint, reducing code complexity
- **Efficient Merging**: Hibernate merge only updates changed fields
- **Timestamp Management**: `updated_at` is automatically managed on every update
- **Index Utilization**: Email lookup uses the existing index on the email column
- **Transaction Safety**: All operations are wrapped in a single transaction

---

## Security Notes

✅ **Password Handling**:

- Passwords are never returned in responses
- Passwords are hashed with BCrypt before storage
- Password changes require the new password to be provided explicitly

✅ **Email Validation**:

- Email uniqueness is enforced at the database level
- Email changes trigger uniqueness validation

✅ **Data Privacy**:

- Only authorized users should be able to update other users' data
- Consider adding authorization checks in production

---

## Related Endpoints

| Method | Endpoint              | Description                     |
|--------|-----------------------|---------------------------------|
| POST   | `/api/users/register` | Register new user (insert only) |
| POST   | `/api/users/login`    | Authenticate user               |
| GET    | `/api/users/{id}`     | Get user by ID                  |
| GET    | `/api/users`          | Get all users                   |
| PUT    | `/api/users/upsert`   | Insert or update user (NEW)     |

---

## Testing Checklist

- [ ] Create new user with all fields
- [ ] Create new user with minimal required fields
- [ ] Create new user with duplicate email (should fail)
- [ ] Update existing user profile
- [ ] Update user with password change
- [ ] Update user role to ADMIN
- [ ] Update user status to INACTIVE
- [ ] Update non-existent user ID (should fail)
- [ ] Update email to existing email of another user (should fail)
- [ ] Verify timestamps are updated correctly
- [ ] Verify password is hashed and not exposed

---

## Troubleshooting

### Issue: "Email already registered" when updating

**Cause**: Trying to change email to one that already exists  
**Solution**: Use a unique email address

### Issue: "User not found with ID"

**Cause**: Provided user ID doesn't exist in the database  
**Solution**: Verify the user ID exists or create a new user (omit id)

### Issue: "Password is required for new users"

**Cause**: Creating new user without providing password  
**Solution**: Include a password field when creating new users

---

**Last Updated**: March 3, 2026  
**Version**: 1.0.0

