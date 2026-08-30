# Quiz App Backend - Spring Boot

A simple REST API backend for a Quiz Application.

## Features

- Admin can create a quiz.
- Admin can add questions to a quiz.
- User can view all quizzes.
- User can select a quiz and view its questions.
- User can submit answers.
- User receives the final score and percentage.
- MySQL is used as the SQL database.
- APIs use path variables for resource IDs. No request parameters are required for the application APIs.
- No authentication/security layer is included so the project stays simple.

## Technology Stack

- Java 17
- Spring Boot 3.5.16
- Spring Web
- Spring Data JPA / Hibernate
- MySQL
- Maven
- Jakarta Validation

Spring Boot 3.5.16 is used because it is a released Spring Boot version available from Maven Central. Spring's release notes identify 3.5.16 as the final OSS release of the 3.5.x generation.

## Project Structure

```text
quiz-app-backend/
├── database/
│   └── quiz_app.sql
├── src/
│   ├── main/
│   │   ├── java/com/example/quizapp/
│   │   │   ├── controller/
│   │   │   │   ├── AdminQuizController.java
│   │   │   │   └── UserQuizController.java
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── QuizAppApplication.java
│   │   └── resources/application.properties
├── pom.xml
└── README.md
```

## Database Setup

### 1. Install MySQL

Make sure MySQL Server is running.

### 2. Create the database

Run:

```sql
source database/quiz_app.sql;
```

Or open `database/quiz_app.sql` in MySQL Workbench and execute it.

The script creates:

- `quiz_app` database
- `quizzes` table
- `questions` table
- sample Java quiz and questions

### 3. Configure MySQL credentials

Open:

```text
src/main/resources/application.properties
```

Change these values if required:

```properties
spring.datasource.username=root
spring.datasource.password=root
```

The default configuration expects MySQL on port `3306`.

## Run the Project

From the project root:

```bash
mvn clean install
```

Then:

```bash
mvn spring-boot:run
```

The API starts on:

```text
http://localhost:8080
```

Or run the packaged JAR:

```bash
java -jar target/quiz-app-backend-0.0.1-SNAPSHOT.jar
```

## API Flow

The normal application flow is:

```text
1. Admin creates quiz
        ↓
2. Admin adds questions to quiz
        ↓
3. User gets all quizzes
        ↓
4. User selects one quiz
        ↓
5. User gets questions
        ↓
6. User submits answers
        ↓
7. Backend calculates score
        ↓
8. User receives score
```

# API Documentation

## 1. Admin - Create Quiz

### Request

```http
POST /api/admin/quizzes
Content-Type: application/json
```

### JSON

```json
{
  "title": "Java Basics Quiz",
  "topic": "Java",
  "description": "Test your basic Java knowledge"
}
```

### Response

```json
{
  "id": 1,
  "title": "Java Basics Quiz",
  "topic": "Java",
  "description": "Test your basic Java knowledge",
  "questions": []
}
```

---

## 2. Admin - Add Question

The quiz ID is passed using a path variable.

### Request

```http
POST /api/admin/quizzes/1/questions
Content-Type: application/json
```

### JSON

```json
{
  "questionText": "Which keyword is used to create a class in Java?",
  "optionA": "class",
  "optionB": "struct",
  "optionC": "define",
  "optionD": "new",
  "correctOption": "A"
}
```

### Response

```json
{
  "id": 1,
  "questionText": "Which keyword is used to create a class in Java?",
  "optionA": "class",
  "optionB": "struct",
  "optionC": "define",
  "optionD": "new",
  "correctOption": "A",
  "quiz": {
    "id": 1,
    "title": "Java Basics Quiz",
    "topic": "Java",
    "description": "Test your basic Java knowledge",
    "questions": []
  }
}
```

> In a production API, the entity response should normally be replaced with a DTO so the correct answer is never exposed. The user-facing GET API in this project already hides `correctOption`.

---

## 3. Admin - Delete Quiz

### Request

```http
DELETE /api/admin/quizzes/1
```

No request parameter is required. The quiz ID is a path variable.

### Response

```text
HTTP 204 No Content
```

---

## 4. User - Get All Quizzes

### Request

```http
GET /api/quizzes
```

### Response

```json
[
  {
    "id": 1,
    "title": "Java Basics Quiz",
    "topic": "Java",
    "description": "Test your basic Java knowledge"
  },
  {
    "id": 2,
    "title": "Spring Boot Quiz",
    "topic": "Spring Boot",
    "description": "Test your Spring Boot knowledge"
  }
]
```

---

## 5. User - Get Selected Quiz

The user selects a quiz using the quiz ID in the URL.

### Request

```http
GET /api/quizzes/1
```

### Response

```json
{
  "id": 1,
  "title": "Java Basics Quiz",
  "topic": "Java",
  "description": "Test your basic Java knowledge",
  "questions": [
    {
      "id": 1,
      "questionText": "Which keyword is used to create a class in Java?",
      "optionA": "class",
      "optionB": "struct",
      "optionC": "define",
      "optionD": "new"
    },
    {
      "id": 2,
      "questionText": "Which method is the entry point of a Java application?",
      "optionA": "start()",
      "optionB": "main()",
      "optionC": "run()",
      "optionD": "init()"
    }
  ]
}
```

Notice that `correctOption` is not returned to the user.

---

## 6. User - Submit Quiz

The quiz ID is a path variable. Only the selected options are required in the JSON request body.

### Request

```http
POST /api/quizzes/1/submit
Content-Type: application/json
```

### JSON

```json
{
  "userName": "Sudarshan",
  "options": ["A", "B", "C"]
}
```

> The options array should match the order of questions in the quiz. Each element represents the selected option for the corresponding question.

### Response

```json
{
  "userName": "Sudarshan",
  "quizId": 1,
  "totalQuestions": 3,
  "correctAnswers": 3,
  "wrongAnswers": 0,
  "score": 3,
  "percentage": 100.0
}
```

## API Summary

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/admin/quizzes` | Admin creates quiz |
| POST | `/api/admin/quizzes/{quizId}/questions` | Admin adds question |
| DELETE | `/api/admin/quizzes/{quizId}` | Admin deletes quiz |
| GET | `/api/quizzes` | User views all quizzes |
| GET | `/api/quizzes/{quizId}` | User views selected quiz |
| POST | `/api/quizzes/{quizId}/submit` | User submits answers and gets score |

## Important Design Choice

There are no `@RequestParam` APIs in this project. IDs needed to identify a resource are path variables such as:

```text
/api/admin/quizzes/1/questions
/api/quizzes/1
/api/quizzes/1/submit
```

The answer data is sent as JSON in the request body because it contains multiple answers.

## Error Examples

### Quiz not found

```json
{
  "timestamp": "2026-08-28T10:00:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Quiz not found",
  "path": "/api/quizzes/999"
}
```

### Invalid correct option

`correctOption` must be one of:

```text
A
B
C
D
```

### Question does not belong to quiz

If the user sends a question ID from another quiz, the API returns `400 Bad Request`.

## Testing with Postman

Recommended order:

1. `POST /api/admin/quizzes`
2. `POST /api/admin/quizzes/1/questions` - repeat for each question
3. `GET /api/quizzes`
4. `GET /api/quizzes/1`
5. `POST /api/quizzes/1/submit`

## Maven Commands

Run all tests:

```bash
mvn test
```

Build the project:

```bash
mvn clean package
```

Run the application:

```bash
mvn spring-boot:run
```

Run without tests:

```bash
mvn clean package -DskipTests
```
