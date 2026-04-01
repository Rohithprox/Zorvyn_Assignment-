# Request DTOs

These classes model incoming request bodies and query payloads.

## Files

- `RegisterRequest` -> register input
- `LoginRequest` -> login input
- `CreateRecordRequest` -> create finance record input
- `UpdateRecordRequest` -> partial update input
- `UpdateUserRoleRequest` -> admin role update input
- `UpdateUserStatusRequest` -> admin status update input

## Validation

Validation rules are declared here using Jakarta Validation annotations.
Controllers use `@Valid` to enforce these constraints automatically.
