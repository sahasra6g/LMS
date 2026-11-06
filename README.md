# Library Management System

A Spring Boot REST API project for Eclipse + PostgreSQL.

## Included features

- One configured librarian account (created automatically on first run)
- Member signup and login
- JWT authentication
- Add, view and delete books
- Issue and return books with issue/due/return dates
- Available-copy count updated on issue and return
- Member can view only their own borrowing history
- Swagger UI documentation

## Requirements

- JDK 21 or later
- Eclipse IDE with Maven support (or Spring Tools)
- PostgreSQL
- Internet connection on first Maven build so Maven can download dependencies

The project uses Spring Boot 3.5.7 and Java 21 bytecode. A newer JDK can run it.

## 1. Create the database

In pgAdmin, create a database named `LibraryManagementDB`.
Open its Query Tool and execute `database/schema.sql`.

If you already have the earlier `books` table with `no_of_copies`, run this migration instead:

```sql
ALTER TABLE books RENAME COLUMN no_of_copies TO total_copies;
ALTER TABLE books ADD COLUMN available_copies INT NOT NULL DEFAULT 0;
UPDATE books SET available_copies = total_copies;
ALTER TABLE books ADD CONSTRAINT chk_available_copies
CHECK (available_copies >= 0 AND available_copies <= total_copies);
```

Do not run the migration if you already renamed the column and added `available_copies`.

## 2. Configure database credentials

Open `src/main/resources/application.properties`.

Set `spring.datasource.username` and `spring.datasource.password`, or set environment variables:
- `DB_USERNAME`
- `DB_PASSWORD`

Optional:
- `DB_URL` (defaults to `jdbc:postgresql://localhost:5432/LibraryManagementDB`)
- `LIBRARIAN_USERNAME` (defaults to `librarian`)
- `LIBRARIAN_PASSWORD` (defaults to `ChangeMe123!`)
- `JWT_SECRET` (use a long Base64-encoded secret outside local practice)

The default librarian is created in the `users` table automatically. Change the default password before real use.

## 3. Import into Eclipse

1. Extract the ZIP.
2. In Eclipse choose **File → Import → Maven → Existing Maven Projects**.
3. Browse to the extracted `LibraryManagementSystem` folder.
4. Select `pom.xml`, then Finish.
5. Wait for Maven dependencies to download.
6. Right-click the project → **Maven → Update Project** if Eclipse requests it.
7. Run `LibraryManagementSystemApplication.java` as **Java Application** or **Spring Boot App**.

The application uses `spring.jpa.hibernate.ddl-auto=validate`: Hibernate checks the schema but does not create or modify tables.

## 4. Open Swagger

After the application starts:
`http://localhost:8080/swagger-ui/index.html`

## 5. Test API flow

### Member signup
`POST /api/auth/signup`
```json
{
  "username": "member1",
  "password": "Member123!",
  "fullName": "Member One",
  "email": "member1@example.com",
  "phone": "9876543210"
}
```

### Login (librarian or member)
`POST /api/auth/login`
```json
{
  "username": "librarian",
  "password": "ChangeMe123!"
}
```
Copy the `token` from the response. In Swagger click **Authorize** and enter the token (Swagger's Bearer scheme expects the token value; do not add `Bearer ` unless the UI asks for it). In Postman use:
`Authorization: Bearer <token>`

### Librarian operations
- `POST /api/books` — `{"name":"Clean Code","totalCopies":4}`
- `GET /api/books`
- `DELETE /api/books/{id}`
- `POST /api/borrows/issue` — `{"studentId":1,"bookId":1,"dueDate":"2026-11-17"}`
- `PUT /api/borrows/{borrowId}/return`

### Member operation
- `GET /api/borrows/my-history`

`GET /api/books` is public. Book creation/deletion and issue/return require a librarian token. Member history requires a member token.

## Notes

- Issue date and return date are recorded by the server using the current date.
- Due date is supplied when the librarian issues the book.
- A book cannot be deleted while it has an active borrow record.
- Reservations, fines, fine payments, and frontend UI are not included because they are outside the current agreed scope.
- This is a complete starter project, but the first local Maven build and PostgreSQL configuration are still required. It has not been executed against your local machine/database.
