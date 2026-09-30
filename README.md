# Min Journal – Backend

Min Journal is a journaling app where users create an account and save short journal entries. Each entry has a free-text note, a status (how you feel) and the date and time it was created.

This repository contains the **Spring Boot backend** (REST API + MySQL). The frontend (Angular) lives in a separate repository: [Min-Journal-frontend](https://github.com/firpen/Min-Journal-frontend). Both are needed to run the app.

## Features

- **Register and log in.** Passwords are hashed with BCrypt before they are stored.
- **Session-based authentication.** On login the user is stored in a server-side session, and the browser gets a `JSESSIONID` cookie that it sends with every request.
- **Protected endpoints.** Everything except `/auth/**` requires a logged-in user, and every note query is scoped to the logged-in user, so users can only reach their own entries.
- **Create, list, edit and delete** journal entries.
- **Filter** entries between a start and an end date.
- **Statistics.** For a date range, the percentage of entries per status is calculated.
- **Validation and error handling** with custom exceptions and suitable HTTP status codes.

## Tech stack

- Java 21
- Spring Boot 4 (Web MVC, Data JPA, Security, Validation)
- MySQL
- Maven (via the included Maven wrapper)

## Requirements

- Java 21 and a Java IDE (e.g. IntelliJ IDEA or VS Code)
- A running MySQL server
- Git
- [Node.js](https://nodejs.org/) for the frontend

Maven does not need to be installed. The project includes a Maven wrapper (`mvnw` / `mvnw.cmd`).

## Running the project locally

### 1. Clone both repositories

```bash
git clone https://github.com/firpen/Min-Journal-backend.git
git clone https://github.com/firpen/Min-Journal-frontend.git
```

### 2. Set up the database

Create an empty MySQL database. The name must match the one you use in `DB_URL` below.

The tables are created automatically by the backend on first start.

### 3. Configure the backend

In the root of the backend project (next to `pom.xml`) there is a file called `.env example`. Copy it to a new file named `.env` in the same folder and fill in your own database details:

```
DB_URL=jdbc:mysql://localhost:3306/your_database_name
DB_USERNAME=your_username
DB_PASSWORD=your_password
```

`.env` is gitignored, so your credentials are never committed.

### 4. Start the backend

Open the backend project in your IDE and run `MinJournalBackendApplication`, or run it from a terminal in the backend folder:

```bash
./mvnw spring-boot:run
```

The backend starts on `http://localhost:8080`.

### 5. Start the frontend

Follow the instructions in the [frontend README](https://github.com/firpen/Min-Journal-frontend).

> The backend only accepts cross-origin requests from `http://localhost:4200`, so the frontend must run on that port.

## API

All endpoints except `/auth/**` require a logged-in session. Requests from the browser must be sent with credentials (the `JSESSIONID` cookie).

### Auth

| Method | Endpoint | Description | Success |
|---|---|---|---|
| POST | `/auth/register` | Create an account. Body: `{ "username", "password" }` | `201 Created` |
| POST | `/auth/login` | Log in and start a session. Body: `{ "username", "password" }` | `200 OK` + `JSESSIONID` cookie |
| POST | `/auth/logout` | End the session | `200 OK` |
| GET | `/auth/user` | Get the logged-in user: `{ "username" }` | `200 OK` |

### Notes

| Method | Endpoint | Description | Success |
|---|---|---|---|
| POST | `/notes` | Create an entry. Body: `{ "note", "status" }` | `201 Created` |
| GET | `/notes` | Get all entries for the logged-in user | `200 OK` |
| GET | `/notes/notesbetweendates?start=...&end=...` | Get entries between two dates | `200 OK` |
| PUT | `/notes/{id}` | Update an entry. Body: `{ "note", "status" }` | `200 OK` |
| DELETE | `/notes/{id}` | Delete an entry | `204 No Content` |
| GET | `/notes/stats?start=...&end=...` | Percentage of entries per status between two dates | `200 OK` |

`status` is one of `HAPPY`, `SAD`, `TIRED`, `STRESSED`, `CALM`, `ANGRY`.
Dates are sent in ISO format, e.g. `2026-09-01T00:00:00`.

Example response from `/notes/stats`:

```json
{ "HAPPY": 50.0, "SAD": 25.0, "TIRED": 25.0, "STRESSED": 0.0, "CALM": 0.0, "ANGRY": 0.0 }
```

### Error codes

| Status | When |
|---|---|
| `400 Bad Request` | The input does not meet the requirements (username 3–10 characters, password 4–64 characters, note 1–1000 characters, status required) |
| `401 Unauthorized` | Wrong username or password, or `/auth/user` without a session |
| `403 Forbidden` | A protected endpoint is called without being logged in |
| `404 Not Found` | The note does not exist or does not belong to the logged-in user |
| `409 Conflict` | The username is already taken |

## Project structure

```
src/main/java/com/minjournal/min_journal_backend/
  configuration/   SecurityConfig (security rules, CORS, password encoder)
  controllers/     AuthController, NoteController (REST endpoints)
  services/        AuthService, NoteService (business logic, e.g. statistics)
  repositories/    UserRepository, NoteRepository (database access with Spring Data JPA)
  models/          User, Note (entities), Status (enum)
  dtos/            Request and response objects
  exceptions/      Custom exceptions and GlobalExceptionHandler
```
