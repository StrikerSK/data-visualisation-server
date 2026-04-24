# Repository Guidelines

## Project Structure & Module Organization
This repository is a multi-module Maven project targeting Java 17. Shared domain logic lives in `core-module/src/main/java/com/charts`, including API entities, repositories, services, CSV import/export, Liquibase migrations, and shared configuration. Chart-specific adapters live in `apex-module`, `nivo-module`, and `recharts-module`. The runnable Spring Boot artifact is assembled in `jar-distribution`, with the main class at `jar-distribution/src/main/java/com/charts/main/JavaPidApplication.java`. Tests follow the same package layout under each module’s `src/test/java`. Sample requests and CSV fixtures live in `requests/` and `src/test/resources/`.

## Build, Test, and Development Commands
Use Maven from the repository root:

- `mvn compile` compiles all modules.
- `mvn test` runs the TestNG-based test suite.
- `mvn verify jacoco:report` runs verification and generates coverage output, matching CI.
- `mvn install` builds and installs all module artifacts locally.
- `mvn -pl jar-distribution spring-boot:run` starts the application from the distribution module.
- `docker compose -f docker-compose.yaml up --build` runs the packaged app with Docker.

## Coding Style & Naming Conventions
Follow the existing Spring Boot structure: `controller`, `service`, `repository`, `entity`, `utils`, and `config` packages under `com.charts.*`. Class names use PascalCase, methods and fields use camelCase, and constants use UPPER_SNAKE_CASE. Prefer 4-space indentation in new code and keep package names lowercase. Use Lombok where the module already does. There is no enforced formatter in the build, so keep imports tidy and match surrounding style before submitting.

## Testing Guidelines
Tests use TestNG with `spring-boot-starter-test`, Mockito, and JaCoCo. Name tests with the subject under test plus `Test`, for example `CouponRepositoryTest` or `CsvProcessorTest`. Add focused unit tests in the same module as the code you change, and keep test data in `src/test/resources` when fixtures are needed. Run `mvn test` locally before opening a PR; run `mvn verify jacoco:report` for changes that affect filtering, grouping, persistence, or file import/export.

## Commit & Pull Request Guidelines
Recent history favors short, imperative commit subjects such as `Add new Apex tests` or `Docker improvements`; keep the first line concise and specific. Pull requests should describe the functional change, list impacted modules, mention any config or schema updates, and include request examples when an endpoint or CSV format changes. Link the related issue when available and note the Maven command used for verification.

## Configuration & Data Notes
Application profiles and datasource settings are in `core-module/src/main/resources/application*.properties`. Liquibase changelogs are under `core-module/src/main/resources/config/liquibase`, and seed SQL lives in `core-module/src/main/resources/config/sql`. Do not commit secrets from `.env`; use local overrides instead.
