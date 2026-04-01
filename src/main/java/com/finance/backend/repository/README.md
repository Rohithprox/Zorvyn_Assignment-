# Repository Module

Repositories handle persistence and query logic.

## Files

- `UserRepository.java`
  - User lookup by email
  - Existence checks for registration
  - Optional role-based user listing

- `FinanceRecordRepository.java`
  - Soft-delete aware reads
  - Aggregation queries for dashboard:
    - sum by type
    - active count
    - totals by category
    - monthly trend
    - recent records

- `FinanceRecordSpecification.java`
  - Dynamic filtering predicates:
    - type
    - category
    - date range
    - not deleted

## Query Design Notes

- All business-facing reads should ignore deleted records.
- Aggregation methods are designed for dashboard endpoints.
