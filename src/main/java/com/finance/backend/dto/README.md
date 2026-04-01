# DTO Module

DTOs are API contracts between clients and backend.

## Submodules

- `request/`
  - Input DTOs used by controllers
  - Includes validation annotations (`@NotNull`, `@Email`, etc.)

- `response/`
  - Output DTOs returned by APIs
  - Keeps API output independent from database entities

## Why DTO Layer

- Prevents exposing internal entity structures directly
- Improves API stability and versioning
- Makes validation and response shaping explicit
