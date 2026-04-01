# Exception Module

Centralized error handling and custom exception types.

## Files

- `BadRequestException.java` -> 400 scenarios
- `ForbiddenException.java` -> 403 business scenarios
- `ResourceNotFoundException.java` -> 404 for missing entities
- `GlobalExceptionHandler.java`
  - Converts exceptions into standard API error response format
  - Handles validation, auth, permission, not found, and generic failures

## API Error Contract

Error response includes:
- timestamp
- status
- error
- message
- path
- optional field-level validation errors
