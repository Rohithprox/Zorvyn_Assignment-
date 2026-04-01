# Controller Module

Controllers expose REST APIs and delegate business logic to services.

## Endpoint to Controller Mapping

- `AuthController.java` -> `/api/auth`
  - `POST /register`: register user and return token
  - `POST /login`: authenticate user and return token

- `UserController.java` -> `/api/users` (ADMIN only)
  - `GET /`: list users
  - `GET /{id}`: user details
  - `PUT /{id}/role`: update role
  - `PUT /{id}/status`: activate/deactivate user

- `FinanceRecordController.java` -> `/api/records`
  - `GET /`: list with filters and pagination
  - `GET /{id}`: get single record
  - `POST /`: create record (ANALYST/ADMIN)
  - `PUT /{id}`: partial update (ANALYST/ADMIN)
  - `DELETE /{id}`: soft delete (ADMIN)

- `DashboardController.java` -> `/api/dashboard`
  - `GET /summary`
  - `GET /by-category`
  - `GET /monthly-trend`
  - `GET /recent`

## Business Logic Boundary

Controllers should:
- validate and accept request DTOs
- call service methods
- return response DTOs

Controllers should not contain DB logic or heavy business calculations.
