# Recommendation Service

The Recommendation Service provides personalized job recommendations using semantic similarity between user profiles and job descriptions.

## Architecture Overview

The service integrates:

- Profile Service → user profile data (skills, experience, summary)
- Job Service → job details
- Python Embedding Service → generates vector embeddings
- PostgreSQL (pgvector) → stores embeddings and performs similarity search
- Redis → caches recommendation results

---

## How Recommendations Work

When a user requests recommendations:

```text
GET /api/recommendations/jobs/me?limit={limit}
```
### Step-by-step flow
- Check Redis cache 
- If cache miss:
- Load or generate user embedding 
- Perform vector similarity search (pgvector)
- Retrieve job details from Job Service 
- Rank jobs by similarity score 
- Store result in Redis 
- Return recommendations

## Redis Caching
### Cache Key
recommendations:user:{userId}:limit:{limit}

Example:
- recommendations:user:1:limit:20 
- Cache Strategy 
- Cache the final recommendation response 
- TTL-based expiration (default: 10 minutes)
- Cache invalidated when:
- User embedding is recomputed 
- Job embeddings are updated 
- Performance Improvement 
- Measurement Method

Measured using:
```text
curl -o /dev/null -s -w "Total time: %{time_total}s\n" \
-H "Authorization: Bearer <TOKEN>" \
"http://localhost:8087/api/recommendations/jobs/me?limit=20"
```
## Results
Before Redis
- limit=20 → ~19ms – 55ms 
- limit=50 → ~24ms – 38ms 
- After Redis (cache hit)
- limit=20 → ~9ms 
- limit=50 → ~9ms 
- Improvement 
- limit=20 → ~53% faster 
- limit=50 → ~76% faster
Why This Matters

Without caching, each request performs:

- vector similarity search 
- database access 
- inter-service HTTP calls

With Redis:

Repeated requests → O(1) lookup

This significantly reduces:

### latency
- database load 
- inter-service traffic 
- Technologies Used 
- Spring Boot (microservice)
- PostgreSQL + pgvector 
- Redis 
- FastAPI (Python embedding service)
- REST-based microservice communication