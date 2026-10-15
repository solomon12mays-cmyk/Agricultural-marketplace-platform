# Backend — Agricultural Marketplace Platform

Java 21 / Spring Boot 3.5 REST API. The application lives under the
`com.agricultural.marketplace` package and is organized by bounded context.

## Layout

```
com.agricultural.marketplace
├── shared/          cross-cutting concerns
│   ├── config/      database, redis, jackson, openapi, async
│   ├── security/    JWT filter, token service, user details, password hashing
│   ├── exception/   typed exceptions + global handler
│   ├── response/    ApiResponse, ApiError, PageResponse
│   ├── logging/     correlation-id and request logging filters
│   ├── audit/       audit trail (event, action, service, repository)
│   ├── idempotency/ idempotency keys for safe retries
│   ├── locking/     distributed locks
│   ├── events/      domain event publishing
│   ├── validation/  shared validation helpers
│   ├── pagination/  pagination contracts
│   └── util/        general utilities
├── auth/            accounts, roles, sessions, login/registration
├── farmer/          farmer profiles and verification
├── farm/            farms and locations
├── organization/    organizations + verification state machine
├── personalbuyer/   personal buyers
├── driver/          drivers and vehicles
├── product/         products and grain varieties
├── inventory/       stock accounts, reservations, ledger
├── listing/         listings + listing state machine
├── marketplace/     search and ranking
├── order/           orders + order state machine
├── payment/         payments, gateway, refunds + payment state machine
├── ledger/          double-entry journal
├── fee/             fee rules and calculations
├── payout/          farmer/driver payouts
├── logistics/       driver matching, assignments, maps
├── pickup/          pickup records
├── delivery/        delivery records
├── notification/    notifications + SMS/USSD/push providers
├── operator/        operator tasks
├── compliance/      compliance cases and disputes
├── document/        document upload and object storage
├── verification/    identity/bank/document verification providers
├── admin/           back-office endpoints
└── health/          health endpoint
```

## Database

Flyway migrations live in `src/main/resources/db/migration` (V1–V26) and
create all tables in the `marketplace` schema. Hibernate runs with
`ddl-auto: validate`.

## Profiles

- `application.yml` — defaults (H2 in-memory for local runs without Docker)
- `application-dev.yml` — local development
- `application-test.yml` — automated tests
- `application-prod.yml` — production (PostgreSQL via `SPRING_DATASOURCE_*`)

## Run

```bash
./mvnw spring-boot:run
```

## Test

```bash
./mvnw test
```

Farmer phone-verification OTP lifecycle, API, configuration, and delivery behavior
are documented in [`docs/otp-phone-verification.md`](docs/otp-phone-verification.md).
