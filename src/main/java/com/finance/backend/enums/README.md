# Enums Module

Enums define fixed domain values used across entities, DTOs, and authorization logic.

## Files

- `Role.java`
  - `VIEWER`, `ANALYST`, `ADMIN`
  - Used for access control and Spring authorities

- `RecordType.java`
  - `INCOME`, `EXPENSE`
  - Used in records and dashboard aggregations

- `UserStatus.java`
  - `ACTIVE`, `INACTIVE`
  - Used to allow/deny login

## Why This Module Matters

Centralized constants reduce magic strings and avoid invalid domain values.
