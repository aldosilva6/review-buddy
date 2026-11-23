# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Stack

Java 25, Spring Boot 4.2, Spring Modulith 2.2 (milestone), Maven 3.10 (versions pinned in `.sdkmanrc`; `sdk env install`). PostgreSQL + Flyway, JPA/Hibernate, server-rendered Thymeleaf with the layout dialect, HTMX (`htmx-spring-boot-thymeleaf`), Tailwind via the Play CDN (theme in `static/js/tailwind.config.js`), Font Awesome/HTMX served as WebJars through `webjars-locator-lite` (versionless `/webjars/...` paths).

## Commands

```shell
./mvnw spotless:apply                      # format (Palantir Java Format) — run before building
./mvnw clean verify                        # full build: tests + JaCoCo coverage gate
./mvnw test -Dtest=ClassName               # single test class
./mvnw test -Dtest='ClassName#methodName'  # single test method
./mvnw spring-boot:run -Dspring-boot.run.profiles=local   # run against compose.yaml Postgres
./mvnw spring-boot:test-run                # run via TestReviewBuddyApplication (Testcontainers Postgres)
```

`Taskfile.yml` wraps these: `task test` (format + `clean verify`), `task build_image` (Buildpacks image `docker.io/aldosilva6/review-buddy:<version>`), `task start`/`stop` (runs the image with compose profile `app`).

Build gotchas:
- `spotless:check` is bound to the `compile` phase, so unformatted code fails every build, not just CI.
- `verify` enforces a JaCoCo minimum of 40% line coverage (`jacoco.minimum.coverage` in `pom.xml`).
- Tests use Testcontainers (`TestcontainersConfiguration`), so Docker must be running. Docker Compose v2 (`docker compose`) is required for `spring-boot-docker-compose`.

## Running locally

- `spring-boot-docker-compose` starts the `postgres` service from `compose.yaml` on startup (`lifecycle-management=start_only`) and overrides the `spring.datasource.*` defaults in `application.properties`. The `review-buddy` service in the same file is behind the `app` profile and is only for running the built image.
- The `local` profile (`application-local.properties`) enables lazy init, exposes all actuator endpoints, turns on Hibernate statistics and BootUI (dev dashboard at `/bootui`, MCP endpoint at `/bootui/api/mcp`, localhost-only).
- `FlywayConfig` (local profile only) **cleans the database and re-runs all migrations if a migration fails**. Never rely on local data surviving a bad migration.
- `.env.properties` in the working directory is optionally imported for secrets/overrides.

## Architecture

Spring Modulith application rooted at `dev.aldosilva.reviewbuddy`. Each direct subpackage is an application module; currently `users`, plus the non-domain `config` and `shared` packages.

- **Module boundaries:** other modules must only use a module's top-level package (its public API). For `users` that is `UsersAPI` (current user id/name, lookup by id); `users.domain`, `users.config`, `users.web` are internal. Cross-module side effects should go through Modulith events — the JDBC event publication registry is enabled (`event_publication` table, completion mode `update`, outstanding events republished on restart).
- **Module layout convention** (see `users`): `domain/` (entity, repository, service, `models/` with `*Cmd` inputs and `*Dto` outputs as records, `exceptions/`), `web/` (MVC controllers + request records), `config/` (module-specific beans), and the `*API` facade at the module root. Services map entities to DTOs; entities don't leave the domain package.
- **Persistence:** schema is owned by Flyway (`db/migration/V###__*.sql`); Hibernate runs with `ddl-auto=validate`, so every entity change needs a migration. IDs use per-table Postgres sequences with `increment by 50` to match the `pooled-lo` optimizer. Entities extend `shared.BaseEntity` (`created_at`/`updated_at` auditing + `@Version`); `PersistenceConfig` supplies the auditor from `UsersAPI`. Hikari auto-commit is off and open-in-view is disabled, so all data access must happen inside service-layer `@Transactional` methods.
- **Security:** form login at `/login`, backed by `SecurityUserDetailsProvider` → `UserService.findCredentialsByEmail`. Route rules live in `config/WebSecurityConfig` (`PUBLIC_RESOURCES` + everything else authenticated); role hierarchy comes from `Role.getRoleHierarchy()`. Use `AuthUtils.getCurrentUserId()`/`getCurrentUserIdOrThrow()` within `users`, `UsersAPI` from other modules. V001 seeds an admin and a demo user.
- **Errors:** `config/GlobalExceptionHandler` renders `templates/error/{403,404,500}.html`; throw `shared.ResourceNotFoundException` / `BadRequestException` from services.
- **Views:** pages decorate `templates/layout.html` via the layout dialect; UI strings come from `messages.properties`.

## Planned work (from README)

The README describes features not yet in the code: products with public pages (`/products`, `/products/{id}` are already permitted in `WebSecurityConfig`), user reviews, and Spring AI–generated review summaries via OpenAI (`OPENAI_API_KEY`, optional `OPENAI_MODEL`, default `gpt-4o-mini`). Intended behavior: after a review commits, a background task summarizes all reviews for the product once it has ≥3; send only feedback text (no reviewer identities); retry invalid structured output twice via Spring AI's validation advisor but don't retry provider failures; keep the previous summary on failure; persist summaries per product and never let an older snapshot overwrite one based on more reviews; show "AI summary not available yet." when none exists; reading summaries requires no login; tests must stub AI responses.
