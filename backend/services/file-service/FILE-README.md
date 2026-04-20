# SkillMatch File Service

File Service is responsible for resume upload, storage, metadata management, and secure access for the owning job seeker.

## Features

- Upload resume
- Replace existing resume
- View current resume metadata
- Download current resume
- Delete current resume
- JWT-based stateless authentication
- Local filesystem storage
- PostgreSQL metadata persistence
- Swagger/OpenAPI documentation

## Tech Stack

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- JJWT
- Springdoc OpenAPI

## API Endpoints

- `POST /api/files/resume` → upload or replace resume
- `GET /api/files/resume` → get current resume metadata
- `GET /api/files/resume/download` → download current resume
- `DELETE /api/files/resume` → delete current resume

## Rules

- Only `JOB_SEEKER` can manage resumes
- Only PDF files are allowed
- Max size is 5 MB
- Only one active resume per user

## Storage Design

Physical files are stored locally under:

uploads/resumes/user-{userId}/

Metadata is stored in PostgreSQL.

## Future Improvements

- Amazon S3 storage implementation
- Internal recruiter access for applicant resumes
- Signed download URLs
- Stronger MIME/content inspection