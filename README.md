# Finance Data Processing and Access Control Backend

Spring Boot REST API backend for a finance dashboard system with role-based access control, financial record management, and analytics endpoints.

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Framework | Spring Boot 3.2.4 |
| Language | Java 17 |
| Security | Spring Security 6 + JWT (jjwt 0.12.3) |
| Database | PostgreSQL (prod) / H2 (dev) |
| ORM | Spring Data JPA + Hibernate |
| Validation | Jakarta Bean Validation |
| Docs | Springdoc OpenAPI 2.x (Swagger UI) |
| Build | Maven |
| Utilities | Lombok |

## Project Structure

```text
src/main/java/com/finance/backend/
├── FinanceBackendApplication.java
├── config/
├── controller/
├── dto/
│   ├── request/
│   └── response/
├── entity/
├── enums/
├── exception/
├── repository/
├── security/
└── service/
```

## Module Overview (Overall -> Individual)

### 1) Application Entry Module

- `FinanceBackendApplication`
  - Main bootstrapping class.
  - Starts Spring context and auto-configures all modules.

### 2) Config Module

- `config/SecurityConfig`
  - Defines `SecurityFilterChain`.
  - Registers JWT filter, stateless session policy, and endpoint role rules.
  - Enables method-level authorization with `@EnableMethodSecurity`.

- `config/OpenApiConfig`
  - Configures OpenAPI metadata.
  - Enables Bearer token scheme for Swagger UI authentication.

### 3) Enums Module

- `enums/Role` -> `VIEWER`, `ANALYST`, `ADMIN`
- `enums/RecordType` -> `INCOME`, `EXPENSE`
- `enums/UserStatus` -> `ACTIVE`, `INACTIVE`

These enums keep domain values safe, consistent, and strongly typed.

### 4) Entity Module (Database Model)

- `entity/User`
  - Stores user identity, credentials, role, and status.
  - Uses unique email constraint.

- `entity/FinanceRecord`
  - Stores transaction-like finance records.
  - Uses `BigDecimal` for exact currency values.
  - Linked to creator (`createdBy`).
  - Implements soft delete via `isDeleted`.

### 5) Repository Module (Data Access)

- `repository/UserRepository`
  - User lookup and uniqueness checks.

- `repository/FinanceRecordRepository`
  - CRUD + soft-delete aware lookup.
  - Aggregation queries for dashboard:
    - totals by type
    - totals by category
    - monthly trend
    - recent records

- `repository/FinanceRecordSpecification`
  - Dynamic filtering (type, category, date range).
  - Central `notDeleted()` predicate to hide soft-deleted records.

### 6) DTO Module (API Contracts)

#### Request DTOs (`dto/request`)

- `RegisterRequest`, `LoginRequest`
- `CreateRecordRequest`, `UpdateRecordRequest`
- `UpdateUserRoleRequest`, `UpdateUserStatusRequest`

All request DTOs include validation annotations to enforce API input quality.

#### Response DTOs (`dto/response`)

- `AuthResponse`, `UserResponse`, `FinanceRecordResponse`
- `DashboardSummaryResponse`, `CategorySummaryResponse`, `MonthlyTrendResponse`
- `ApiErrorResponse` (standard error body + field errors)

Response DTOs keep API output clean and decoupled from JPA entities.

### 7) Security Module

- `security/JwtUtil`
  - Token generation and validation (jjwt 0.12.x API).
  - Extracts claims and verifies signature/expiration.

- `security/JwtAuthFilter`
  - Intercepts requests.
  - Validates `Authorization: Bearer <token>`.
  - Loads authenticated principal into Spring Security context.

- `security/CustomUserDetailsService`
  - Loads user by email from DB.
  - Maps role to `ROLE_*` authorities.
  - Blocks inactive users.

### 8) Service Module (Business Logic)

- `service/AuthService`
  - Register/login flow.
  - Password encoding, duplicate email checks.
  - JWT creation.

- `service/UserService`
  - Admin user management.
  - Read users, update role/status.

- `service/FinanceRecordService`
  - Record CRUD + partial update behavior.
  - Soft-delete implementation.
  - Filtered pagination via specification.

- `service/DashboardService`
  - Summary calculations.
  - Category and monthly aggregation wiring.
  - Recent activity feed.

### 9) Controller Module (REST Endpoints)

- `controller/AuthController` -> `/api/auth`
- `controller/UserController` -> `/api/users` (ADMIN)
- `controller/FinanceRecordController` -> `/api/records`
- `controller/DashboardController` -> `/api/dashboard`

Controllers are thin; they validate requests, delegate to services, and return DTO responses.

### 10) Exception Module

- `exception/BadRequestException`
- `exception/ForbiddenException`
- `exception/ResourceNotFoundException`
- `exception/GlobalExceptionHandler`

Centralized error handling returns a consistent JSON error format for all API failures.

## Layer Flow (How Modules Connect)

```text
Controller -> Service -> Repository -> Entity/DB
                 |
                 +-> DTO mapping (request/response)

Security Filter -> UserDetailsService -> Security Context -> Role checks
```

## Role Matrix

| Action | VIEWER | ANALYST | ADMIN |
|--------|--------|---------|-------|
| Login / Register | Yes | Yes | Yes |
| View records/dashboard | Yes | Yes | Yes |
| Create records | No | Yes | Yes |
| Update records | No | Yes | Yes |
| Delete records (soft delete) | No | No | Yes |
| Manage users | No | No | Yes |

## Quick Start

### Option 1: H2 (Development)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

- App: `http://localhost:8080`
- H2 Console: `http://localhost:8080/h2-console`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

### Option 2: PostgreSQL

Set environment variables:

```bash
DB_URL=jdbc:postgresql://localhost:5432/finance_db
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=<your-strong-secret>
```

Run:

```bash
mvn spring-boot:run
```

## API Groups

- Auth:
  - `POST /api/auth/register`
  - `POST /api/auth/login`
- Records:
  - `GET /api/records`
  - `GET /api/records/{id}`
  - `POST /api/records`
  - `PUT /api/records/{id}`
  - `DELETE /api/records/{id}`
- Dashboard:
  - `GET /api/dashboard/summary`
  - `GET /api/dashboard/by-category`
  - `GET /api/dashboard/monthly-trend`
  - `GET /api/dashboard/recent`
- Users (ADMIN):
  - `GET /api/users`
  - `GET /api/users/{id}`
  - `PUT /api/users/{id}/role`
  - `PUT /api/users/{id}/status`

## Configuration Files

- `src/main/resources/application.properties`
  - Production-like defaults with PostgreSQL.
- `src/main/resources/application-dev.properties`
  - Dev profile with in-memory H2.

## Build, Test, Run

```bash
mvn clean test
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Docker

```bash
docker-compose up --build
```

This starts:
- PostgreSQL container
- Spring Boot app container on port `8080`

## Notes and Best Practices

- Use `BigDecimal` for all money values.
- Keep soft-deleted records excluded in all queries.
- Use DTOs instead of returning entities directly.
- Keep roles as `hasRole('ADMIN')` style in annotations/config.
- Rotate `JWT_SECRET` for production deployments.

## Deliberate Engineering Tradeoff

For `GET /api/records`, the controller keeps four flat query params (`type`, `category`, `from`, `to`) instead of introducing a dedicated filter DTO.

Why this was intentional:
- only four optional filters right now
- faster to read and debug directly in method signature
- avoids extra wrapper object and conversion ceremony

Known downside:
- method signature is less "clean" than a single request model
- if filter complexity grows, refactor to `FilterRequest` DTO
