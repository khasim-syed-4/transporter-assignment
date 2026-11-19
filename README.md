
# Transporter Assignment on Lanes

## Project Overview
A Spring Boot REST API that assigns transporters to lanes across India while minimizing transportation costs, maximizing transporter utilization up to a specified limit, and ensuring complete lane coverage.

## Technologies Used
- Java 25
- Spring Boot 4.1.2
- Spring Data JPA
- PostgreSQL 17
- Maven
- Swagger / OpenAPI
- JUnit and Mockito

## Main Features
- Input lanes, transporters, and transportation quotes.
- Store data in PostgreSQL.
- Optimize transporter assignments.
- Minimize total transportation cost.
- Maximize the number of selected transporters within the given limit.
- Ensure every lane receives an assignment.
- REST API documentation using Swagger.

## API Endpoints

### 1. Input Data
**POST** `/api/v1/transporters/input`

Accepts lane details and transporter quotes.

### 2. Get Optimized Assignments
**POST** `/api/v1/transporters/assignment`

Request:
```json
{
  "maxTransporters": 3
}
```

Returns:
- Assignment status
- Total transportation cost
- Lane-wise transporter assignments
- Selected transporter IDs

## Optimization Approach
The application uses Dynamic Programming to evaluate transporter combinations.

The current optimization priority is:
1. Maximize the number of distinct transporters actually used, up to the specified limit.
2. Minimize total cost among assignments using that maximum number of transporters.

Every lane must be assigned to at least one transporter.

## Database
PostgreSQL is used to store:
- Lanes
- Transporters
- Transporter quotes

## How to Run the Project

1. Install Java and PostgreSQL.
2. Create a PostgreSQL database named `transporter_db`.
3. Configure database environment variables:
   - `DB_URL`
   - `DB_USERNAME`
   - `DB_PASSWORD`
4. Open the project in Spring Tool Suite or another Java IDE.
5. Run `TransporterAssignmentApplication.java`.

## Swagger Documentation
After starting the application locally:

`http://localhost:8080/swagger-ui/index.html`

## Testing
Run the tests using Maven:

```bash
mvn clean test
```

## Author
Transporter Assignment on Lanes - Java Spring Boot Backend Project.
