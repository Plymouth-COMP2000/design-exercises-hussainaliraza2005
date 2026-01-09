# COMP2000 Restaurant App (Assessment 2)

Android application implementing a dual-role restaurant system (Guest and Staff) with central persistence via the COMP2000 Coursework REST API, local persistence for session/preferences, and role-specific workflows for menu and reservations.

## Key Features (Assessment Requirements)

### Guest
- Browse menu items (name, description, price)
- Create reservations (date/time/party size)
- View reservation status/history (as implemented)
- User preferences (e.g., notifications toggle)

### Staff
- Menu management (CRUD: create, view, update, delete) *(as implemented)*
- View reservations to support operational oversight

### Cross-cutting
- Authentication with role separation (Guest vs Staff flows)
- REST API integration (create/read users, menu/reservations endpoints as implemented)
- Non-blocking network requests (Volley)
- Local persistence (SharedPreferences for session/preferences; SQLite/local repository where used)
- Notifications gated by user preference (where enabled)

## Tech Stack
- Android Studio
- Java (Android)
- Volley (HTTP client)
- SharedPreferences (session + preferences)
- SQLite / Repository layer (local data, where used)
- COMP2000 Coursework REST API (central persistence)

## How to Run
1. Clone the repository.
2. Open in **Android Studio**.
3. Sync Gradle.
4. Run on an emulator or Android device (Android 8.0+ recommended).

> Ensure the device can reach the COMP2000 API host (university network/VPN if required).

## Configuration (Student Namespace)
The API uses a student-scoped namespace. This project is configured to use:

- `STUDENT_ID = student_10929632`

This is set in:
- `app/src/main/java/.../CourseworkApi.java`
