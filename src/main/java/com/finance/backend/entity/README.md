# Entity Module

Entities define the database model.

## Files

- `User.java`
  - Represents system users
  - Fields: name, email, password, role, status, createdAt
  - Email uniqueness enforced

- `FinanceRecord.java`
  - Represents financial transactions
  - Fields: amount, type, category, date, notes, createdBy
  - Audit fields: createdAt, updatedAt
  - Soft delete flag: `isDeleted`

## Data Modeling Rules

- Use `BigDecimal` for amounts.
- Role and status are enums for strict value control.
- Record links back to creator user.
