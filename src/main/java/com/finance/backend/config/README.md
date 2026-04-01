# Config Module

This module contains framework-level configuration for security and API docs.

## Files

- `SecurityConfig.java`
  - Defines the Spring Security filter chain.
  - Configures public routes (`/api/auth/**`, Swagger, H2 console).
  - Enforces role-based route access for records and users.
  - Registers `JwtAuthFilter` before username/password filter.
  - Uses stateless session policy for JWT authentication.

- `OpenApiConfig.java`
  - Configures Swagger/OpenAPI metadata.
  - Adds Bearer JWT security scheme for testing protected endpoints in Swagger UI.

## Business Impact

- Central place for access control rules.
- Misconfiguration here can block valid users or expose protected endpoints.
