# Local Setup

## 1. PostgreSQL
Create these databases:
- skillmatch_auth
- skillmatch_profile
- skillmatch_job
- skillmatch_application
- skillmatch_file
- skillmatch_notification

## 2. Environment
Create `.env` from `.env.example`

## 3. Redis
Run:
`docker compose up -d`

## 4. Run services
Open `services/` in IntelliJ and run services individually.

## 5. Health checks
- http://localhost:8081/actuator/health
- http://localhost:8082/actuator/health