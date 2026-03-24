# EventHub API - Lab 1

Spring Boot REST API for managing events and categories with MySQL database.

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
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. Run the application:
   ```
   mvn spring-boot:run
   ```

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

### Health Check

**11. Health Check**  
GET /api/health  
Returns application health status.

Example Response:
```json
{
  "status": "UP",
  "version": "1.0.0",
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
