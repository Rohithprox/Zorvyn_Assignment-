# Security Module

Security module handles authentication and authorization plumbing.

## Files

- `JwtUtil.java`
  - Generates and validates JWTs
  - Extracts claims and subject (email)
  - Checks expiration and signature

- `JwtAuthFilter.java`
  - Runs once per request
  - Reads Authorization header
  - Validates token and sets Spring Security context

- `CustomUserDetailsService.java`
  - Loads user by email
  - Maps app role to Spring authority format (`ROLE_ADMIN`, etc.)
  - Blocks inactive users

## Security Flow

1. Client sends `Authorization: Bearer <token>`
2. `JwtAuthFilter` validates token
3. User identity loaded and attached to security context
4. Route and method role checks are enforced by Spring Security
