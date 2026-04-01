# Response DTOs

These classes define API response shapes sent back to clients.

## Files

- `AuthResponse` -> token + user identity after auth
- `UserResponse` -> user details for admin management APIs
- `FinanceRecordResponse` -> finance record API response
- `DashboardSummaryResponse` -> totals and net balance
- `CategorySummaryResponse` -> grouped totals by category
- `MonthlyTrendResponse` -> month-wise trend values
- `ApiErrorResponse` -> standardized error response body

## Design Purpose

- Keep responses clean and frontend-friendly
- Avoid direct serialization of JPA entities
- Ensure consistent error and success contracts
