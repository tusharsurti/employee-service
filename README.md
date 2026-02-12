# Employee Management Service

A Spring Boot 3 application for managing employee records, featuring distributed tracing, filtering, and OpenAPI documentation.

## Features

- **Java 21 & Spring Boot 3.2**: Uses modern Java features like Records.
- **RESTful API**: Endpoints for creating and filtering employees.
- **Distributed Tracing**: Integrated with Micrometer Tracing and Zipkin.
- **In-Memory Database**: Uses H2 with an accessible console.
- **Monitoring**: Spring Boot Actuator endpoints enabled.
- **API Documentation**: Swagger UI for easy endpoint testing.
- **Containerization**: Docker and Docker Compose support.

## Prerequisites

- JDK 21
- Maven 3.8+
- Docker & Docker Compose (optional, for running with Zipkin)

## Getting Started

### Running Locally

1. Clone the repository.
2. Build the application:
   ```bash
   mvn clean package
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
   The application will be available at `http://localhost:8080`.

### Running with Docker Compose

To see distributed tracing in action with Zipkin:

```bash
mvn clean package -DskipTests
docker-compose up --build
```

- **App**: `http://localhost:8080`
- **Zipkin UI**: `http://localhost:9411`

## API Endpoints

### Employees

- **Create Employee**
  - `POST /api/employees`
  - Body:
    ```json
    {
      "name": "John Doe",
      "email": "john.doe@example.com",
      "department": "Engineering",
      "age": 35,
      "title": "Manager"
    }
    ```

- **Get All / Filter Employees**
  - `GET /api/employees`
  - Query Params:
    - `minAge`: Returns employees with age strictly greater than this value.
    - `title`: Filter by exact title match.
  - Example: `GET /api/employees?minAge=30&title=Manager`

### Other Endpoints

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **Actuator Health**: `http://localhost:8080/actuator/health`
- **H2 Console**: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:employeedb`, User: `sa`, Password: `password`)

## Testing

Run tests with coverage:
```bash
mvn test
```
Coverage reports are generated in `target/site/jacoco/index.html`.
