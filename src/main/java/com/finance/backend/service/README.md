# Service Module

Services hold business logic and orchestrate repositories, security context, and DTO mapping.

## Files and Responsibilities

- `AuthService.java`
  - Register and login workflows
  - Password encoding
  - Duplicate email checks
  - Token generation via `JwtUtil`

- `UserService.java`
  - Admin operations for users
  - Read all users
  - Update role/status

- `FinanceRecordService.java`
  - Create/read/update/delete (soft delete) finance records
  - Filtered list logic via specification
  - Current user resolution from security context
  - Entity-to-response mapping

- `DashboardService.java`
  - Summary totals
  - Category aggregations
  - Monthly trend aggregation
  - Recent transactions

## Business Logic Rules Implemented

- Money uses `BigDecimal`.
- Delete operation is soft delete (`isDeleted=true`).
- Partial update updates only non-null fields.
- Dashboard numbers exclude soft-deleted records.
