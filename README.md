# EventHub API - Assignment 2

Spring Boot REST API for managing events, categories, users, and registrations with MySQL database integration and Flyway migrations.

---

## Technology Stack

- Java 21
- Spring Boot 3
- Maven
- Spring Data JPA + MySQL
- Caffeine Cache
- Jackson (JSON serialization)
- Swagger / OpenAPI (API documentation)
- Postman (API testing)
- Flyway (database migrations)

---

## Active Profiles

- dev
- test
- prod

Default port: 8080

---

## Setup Instructions

1. Create MySQL database:
   ```
   CREATE DATABASE eventhubdb;
   ```

2. Update credentials in `src/main/resources/application.properties`:
   ```
   spring.datasource.username=root
   spring.datasource.password=***********
   ```

3. Run the application — Flyway will automatically run all migrations:
   ```
   mvn spring-boot:run
   ```

---

## Database Migrations (Flyway)

Migration scripts are located in `src/main/resources/db/migration/`:

- `V1__initial_schema.sql` — creates all tables (categories, events, users, registrations, registration_items)
- `V2__seed_data.sql` — inserts sample categories, events, users, and registrations
- `V3__add_indexes.sql` — adds indexes for performance on commonly queried columns

---

## Entity Relationships

- Category → Events (One-to-Many)
- User → Registrations (One-to-Many)
- Registration → RegistrationItems (One-to-Many)
- RegistrationItem → Event (Many-to-One)

---

## API Endpoints

### Events

**1. Get All Events**
GET /api/v1/events
Returns a paginated list of events. Supports filtering by category, price range, date range, and sorting.

**2. Get Event by ID**
GET /api/v1/events/{id}
Returns a single event by ID.

**3. Create Event**
POST /api/v1/events
Creates a new event.

**4. Update Event**
PUT /api/v1/events/{id}
Updates an existing event.

**5. Delete Event**
DELETE /api/v1/events/{id}
Deletes an event by ID.

---

### Categories

**6. Get All Categories**
GET /api/categories
Returns all event categories.

**7. Get Category by ID**
GET /api/categories/{id}
Returns a single category by ID.

**8. Create Category**
POST /api/categories
Creates a new category.

**9. Update Category**
PUT /api/categories/{id}
Updates an existing category.

**10. Delete Category**
DELETE /api/categories/{id}
Deletes a category by ID.

---

### Users

**11. Get All Users**
GET /api/users
Returns all users.

**12. Get User by ID**
GET /api/users/{id}
Returns a single user by ID.

**13. Create User**
POST /api/users
Creates a new user.

**14. Update User**
PUT /api/users/{id}
Updates an existing user.

**15. Delete User**
DELETE /api/users/{id}
Deletes a user by ID.

---

### Registrations

**16. Get All Registrations**
GET /api/registrations
Returns all registrations.

**17. Get Registration by ID**
GET /api/registrations/{id}
Returns a single registration by ID.

**18. Get Registrations by User**
GET /api/registrations/user/{userId}
Returns all registrations for a specific user.

**19. Create Registration**
POST /api/registrations
Creates a new registration with one or more event tickets.

**20. Update Registration Status**
PATCH /api/registrations/{id}/status?status=CONFIRMED
Updates the status of a registration (PENDING, CONFIRMED, CANCELLED).

**21. Delete Registration**
DELETE /api/registrations/{id}
Deletes a registration by ID.

---

### Health Check

**22. Health Check**
GET /api/health
Returns application health status.

Example Response:
```json
{
  "status": "UP",
  "version": "2.0.0",
  "environment": "Development"
}
```

---

## Swagger UI

Interactive API documentation available at:
http://localhost:8080/swagger-ui.html

---

## JSON Handling

- Dates formatted using Jackson (yyyy-MM-dd HH:mm:ss)
- Null values excluded from responses
- DTOs used for all API requests and responses

---

## Testing

All endpoints tested using Postman. Screenshots and collection file included in submission.
