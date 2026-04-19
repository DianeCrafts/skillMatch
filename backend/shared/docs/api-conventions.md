# API Conventions

## Base path
- Use `/api/v1`

## Response format
- success
- message
- data
- errorCode
- timestamp

## Error handling
- Use global exception handling per service
- Return consistent JSON error responses

## Validation
- Use Bean Validation on request DTOs