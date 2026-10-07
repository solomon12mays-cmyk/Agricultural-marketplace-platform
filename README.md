# Ethiopian Agricultural Marketplace

Phase 1 establishes the frontend, API, local infrastructure, and automated checks. Phase 2 adds the initial marketplace catalog and core product domain, with seed listings for cereal, vegetable, and fruit categories. The current commerce layer adds explicit payment-state tracking alongside order and shipment lifecycle updates so the prototype can represent settlement and fulfillment more realistically without yet integrating a real payment gateway.

## Stack

- Frontend: React, TypeScript, Vite, and Tailwind CSS
- Backend: Java 21, Spring Boot, Spring Security, Spring Data JPA, and Hibernate
- Database and cache: PostgreSQL and Redis
- API docs: OpenAPI / Swagger UI
- Containers and proxy: Docker Compose and Nginx
- CI: GitHub Actions
- Tests: Vitest / Testing Library and JUnit / Mockito; Testcontainers dependencies are ready for database integration tests

## Run locally

Requirements: Docker Compose, Node.js 20.19+ or 22.12+, and Java 21 with Maven if running services outside Docker.

1. Create a `.env` file in the repository root with a local-only database password:

   ```dotenv
   POSTGRES_PASSWORD=replace-with-a-local-password
   APP_AUTH_OPERATOR_USERNAME=operator
   APP_AUTH_OPERATOR_PASSWORD=use-a-unique-password-of-at-least-10-characters
   ```

2. Start the services:

   ```powershell
   docker compose up --build
   ```

3. Open the application at <http://localhost:8080>. The API health endpoint is <http://localhost:8080/api/v1/health>, and Swagger UI is <http://localhost:8080/swagger-ui/index.html>.

PostgreSQL and Redis are reachable only inside the Compose network. The example passwords are intentionally not committed. Do not reuse local credentials in a deployed environment.

The API health route, catalog, farmer directory, seller directory, orders, and shipments remain public for this prototype. Farmer accounts can be created from the home-page registration form using a full name, unique national ID / FAN number, phone number, and password. Passwords are stored using BCrypt; a password must be 10-72 characters.

Set `APP_AUTH_OPERATOR_USERNAME` and `APP_AUTH_OPERATOR_PASSWORD` in `.env` to bootstrap an operator account on first startup. Operators can sign in using that username and provision farmer accounts. The API returns the generated temporary password once to the authenticated operator; the operator must relay it privately by phone or text. Farmers are required to change that password on first sign-in. No SMS provider is integrated.

Account sign-in uses a server-side session cookie and CSRF protection. Set `APP_SESSION_COOKIE_SECURE=true` when serving over HTTPS. The operator account is only seeded if the configured username does not already exist; keep its password secret and unique. Unmatched API routes remain denied. For an identity provider that publishes a JWT JWK Set endpoint, configure `JWT_JWK_SET_URI`; token issuer and audience validation must also be configured before production use.

## Run checks without Docker

```powershell
cd frontend
npm ci
npm test -- --run
npm run build
```

With Java 21 and Maven installed:

```powershell
cd backend
mvn test
```

The database migrations create an isolated `marketplace` schema, including the farmer account and profile tables.
