from sentence_transformers import SentenceTransformer

print(">>> Loading embedding model...")

# Load your trained model OR a default one
model = SentenceTransformer("sentence-transformers/all-MiniLM-L6-v2")

def generate_embedding(text: str):
    """Return a Python list (float[]) for JSON serialization."""
    vector = model.encode(text)
    return vector.tolist()
