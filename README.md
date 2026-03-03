# EventHub API - Lab 1

Spring Boot REST API for managing events using in-memory storage.

## Technology Stack
- Java 21
- Spring Boot 3
- Maven
- Jackson (JSON serialization)
- Postman (API testing)

---

## Active Profiles
- dev
- test
- prod

Default port: 8080

---

## API Endpoints

### 1. Get All Events
GET /api/events  
Returns list of all events.

### 2. Get Event by ID
GET /api/events/{id}  
Returns a single event by ID.

### 3. Create Event
POST /api/events  
Creates a new event.

### 4. Update Event
PUT /api/events/{id}  
Updates an existing event.

### 5. Delete Event
DELETE /api/events/{id}  
Deletes an event by ID.

### 6. Health Check
GET /api/health  
Returns application health status.

Example Response:
{
  "status": "UP",
  "version": "1.0.0",
  "environment": "Development"
}

---

## JSON Handling
- Dates formatted using Jackson
- Null values excluded
- DTO used for API responses

---

## Testing
All endpoints tested using Postman.
Screenshots and collection file included in submission.
