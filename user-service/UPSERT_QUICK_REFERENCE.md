# Upsert Endpoint - Quick Reference

## Summary

The `/api/users/upsert` endpoint provides a unified way to **insert** (create) or **update** a user in a single
operation using Hibernate merge pattern.

## When to Use

| Scenario           | Use This Endpoint                                             |
|--------------------|---------------------------------------------------------------|
| Create new user    | ✅ POST to `/api/users/register` OR PUT to `/api/users/upsert` |
| Update user        | ✅ PUT to `/api/users/upsert`                                  |
| Change password    | ✅ PUT to `/api/users/upsert`                                  |
| Update profile     | ✅ PUT to `/api/users/upsert`                                  |
| Change role/status | ✅ PUT to `/api/users/upsert`                                  |

## Quick Commands

### Create New User

```bash
curl -X PUT http://localhost:8080/api/users/upsert \
  -H "Content-Type: application/json" \
  -d '{
    "first_name": "John",
    "last_name": "Doe",
    "email": "john@example.com",
    "password": "Password123!",
    "phone": "+1-555-0000"
  }'
```

**Response**: `201 Created`

---

### Update Existing User

```bash
curl -X PUT http://localhost:8080/api/users/upsert \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "first_name": "John",
    "last_name": "Smith",
    "email": "john.smith@example.com",
    "phone": "+1-555-1111"
  }'
```

**Response**: `200 OK`

---

### Update Password

```bash
curl -X PUT http://localhost:8080/api/users/upsert \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "first_name": "John",
    "last_name": "Smith",
    "email": "john.smith@example.com",
    "password": "NewPassword456!"
  }'
```

**Response**: `200 OK`

---

### Promote User to Admin

```bash
curl -X PUT http://localhost:8080/api/users/upsert \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "first_name": "John",
    "last_name": "Smith",
    "email": "john.smith@example.com",
    "role": "ADMIN"
  }'
```

**Response**: `200 OK`

---

### Deactivate User

```bash
curl -X PUT http://localhost:8080/api/users/upsert \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "first_name": "John",
    "last_name": "Smith",
    "email": "john.smith@example.com",
    "status": "INACTIVE"
  }'
```

**Response**: `200 OK`

---

## Request Fields

| Field      | Type   | Insert                  | Update      | Notes                       |
|------------|--------|-------------------------|-------------|-----------------------------|
| id         | Long   | ❌ Omit                  | ✅ Required  | Determines insert vs update |
| first_name | String | ✅ Required              | ✅ Required  | User's first name           |
| last_name  | String | ⚠️ Optional             | ⚠️ Optional | User's last name            |
| email      | String | ✅ Required              | ✅ Required  | Must be unique              |
| password   | String | ✅ Required              | ⚠️ Optional | Only if changing password   |
| phone      | String | ⚠️ Optional             | ⚠️ Optional | Phone number                |
| role       | String | ⚠️ Defaults to "USER"   | ⚠️ Optional | USER, ADMIN, etc.           |
| status     | String | ⚠️ Defaults to "ACTIVE" | ⚠️ Optional | ACTIVE, INACTIVE, etc.      |

---

## Response Examples

### Successful Insert (201 Created)

```json
{
  "id": 5,
  "first_name": "John",
  "last_name": "Doe",
  "email": "john@example.com",
  "phone": "+1-555-0000",
  "role": "USER",
  "status": "ACTIVE",
  "created_at": "2026-03-03T14:30:00",
  "updated_at": "2026-03-03T14:30:00"
}
```

### Successful Update (200 OK)

```json
{
  "id": 5,
  "first_name": "John",
  "last_name": "Smith",
  "email": "john.smith@example.com",
  "phone": "+1-555-1111",
  "role": "ADMIN",
  "status": "ACTIVE",
  "created_at": "2026-03-03T14:30:00",
  "updated_at": "2026-03-03T14:35:00"
}
```

### Error Response (400 Bad Request)

```json
"Email already registered"
```

---

## Common Errors

| Error                                | Cause                               | Solution             |
|--------------------------------------|-------------------------------------|----------------------|
| `First name is required`             | Missing first_name                  | Add first_name field |
| `Email is required`                  | Missing email                       | Add email field      |
| `Email already registered`           | Email exists or duplicate on update | Use different email  |
| `Password is required for new users` | Creating user without password      | Add password field   |
| `User not found with ID: X`          | Updating non-existent user          | Check user ID exists |

---

## How It Works

```
PUT /api/users/upsert
  │
  ├─ Is "id" provided and > 0?
  │  ├─ YES → UPDATE existing user (merge)
  │  └─ NO → INSERT new user (persist)
  │
  ├─ Validate required fields
  ├─ Check email uniqueness
  ├─ Hash password (if provided)
  ├─ Update/Insert in database
  └─ Return user response
```

---

## Key Features

✅ **Smart Merge**: Automatically detects insert vs update based on ID  
✅ **Partial Updates**: Update only the fields you provide (for updates)  
✅ **Consistent Response**: Same response format for both insert and update  
✅ **Automatic Timestamps**: `created_at` and `updated_at` managed automatically  
✅ **Password Security**: Passwords are hashed with BCrypt  
✅ **Email Validation**: Email uniqueness enforced at database level

---

## Notes

- **No ID field** → Creates new user, returns `201 Created`
- **With ID field** → Updates existing user, returns `200 OK`
- **Password optional on update** → Only change if provided
- **Role/Status optional on update** → Only change if provided
- **Timestamps auto-managed** → `updated_at` updates on every change
- **Password never returned** → For security reasons

---

## Comparison with Other Endpoints

| Feature          | Register | Login | Upsert |
|------------------|----------|-------|--------|
| Create User      | ✅        | ❌     | ✅      |
| Update User      | ❌        | ❌     | ✅      |
| Change Password  | ❌        | ❌     | ✅      |
| Change Role      | ❌        | ❌     | ✅      |
| Generate JWT     | ❌        | ✅     | ❌      |
| Single Operation | ✅        | ✅     | ✅      |

---

**Endpoint**: `PUT /api/users/upsert`  
**Authentication**: Not required  
**Content-Type**: `application/json`  
**Documentation**: See `UPSERT_ENDPOINT_GUIDE.md`

