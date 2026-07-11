# Backend Developer Guide

## 1. IDE Recommendation

It is recommended to use **Visual Studio Code** + **Extension Pack for Java** (by Microsoft) for a better development experience.

## 2. Starting the Backend

To start the Spring Boot project, run the following command under the `piggymemo/` directory:

```bash
mvn spring-boot:run -DskipTests
```
For packed fat jars, when running the .jar package, directory of application-local.properties must be assigned explicitly:

```bash
java -jar [jar package name].jar --spring.config.location=[directory/to/application-local.properties]
```

## 3. Common Structure for Spring Boot Project

The project follows a standard layered Spring Boot structure:

```
piggy-memo-backend/
└── piggymemo/
    ├── src/
    │   ├── main/
    │   │   ├── java/com/csye6225/piggymemo/
    │   │   │   ├── config/          # Application & Spring configuration classes
    │   │   │   ├── controller/      # REST controllers (API endpoints)
    │   │   │   ├── dto/             # Data Transfer Objects (request/response models)
    │   │   │   ├── entity/          # JPA entities (database models)
    │   │   │   ├── exception/       # Custom exceptions & global exception handlers
    │   │   │   ├── repository/      # Spring Data JPA repositories
    │   │   │   ├── service/         # Business logic layer
    │   │   │   └── PiggymemoApplication.java   # Application entry point
    │   │   └── resources/
    │   │       ├── application.properties        # Default application configuration
    │   │       └── application-local.properties  # Local environment overrides
    │   └── test/
    │       └── java/                # Unit and integration tests
    ├── target/                      # Compiled build output (generated, not tracked in Git)
    ├── pom.xml                      # Maven project configuration & dependencies
    ├── .gitignore
    ├── LICENSE
    └── README.md
```

### Package Descriptions

| Package | Responsibility |
|---|---|
| `config` | Bean definitions, security config, CORS, Swagger/OpenAPI setup, etc. |
| `controller` | Exposes REST endpoints; handles HTTP requests/responses. |
| `dto` | Defines request/response payload shapes used at the API boundary. |
| `entity` | Maps to database tables via JPA annotations. |
| `exception` | Custom exception classes and `@ControllerAdvice` global handlers. |
| `repository` | Interfaces extending `JpaRepository` for database access. |
| `service` | Contains core business logic, called by controllers. |

### Configuration Files

- **`application.properties`** — shared/default configuration (e.g., server port, common settings).
- **`application-local.properties`** — local-only overrides (e.g., local DB credentials); typically excluded from version control or used with the `local` Spring profile. **WARNING! NEVER PUBLISH THIS FILE!**
