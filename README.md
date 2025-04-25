# Custom OpenSearch Plugin

## Prerequisites

Ensure the following are installed:

- **Java 17**
- **Gradle**
- **Docker**

Note 
- Make sure JAVA_HOME is set correctly on your system.

---

## 🐧 Linux (Using Makefile)

The Makefile simplifies all operations. Use the following commands:

| Command              | Description                                   |
|----------------------|-----------------------------------------------|
| `make all`           | Builds the plugin and starts Docker containers |
| `make build`         | Compiles the project using Gradle              |
| `make docker-up`     | Builds and runs the Docker containers          |
| `make docker-down`   | Stops and removes the Docker containers        |

---

## 🪟 Windows (Manual Steps)

1. **Build with Gradle**

   ```bash
   ./gradlew clean build --no-daemon

2. **Build and Run Docker**

   ```bash
   docker-compose up -d

3. **docker-compose down**
   ```bash
   docker-compose down

---

## 🪟 Opensearch Validation Steps

1. **Create a New Index**

   ```bash
   PUT /products
    {
    "mappings": {
    "properties": {
    "title": {
    "type": "text"
    },
    "price": {
    "type": "double",
    "doc_values": true
    }
    }
    }
    }

2. ** Ingest sample Data **

   ```bash
   POST /products/_bulk
   { "index": { "_id": 1 } }
   { "title": "Wireless Mouse", "price": 100.0 }
   { "index": { "_id": 2 } }
   { "title": "Wireless Mouse", "price": 50.0 }
   { "index": { "_id": 3 } }
   { "title": "Wireless Mouse", "price": 25.0 }
   { "index": { "_id": 4 } }
   { "title": "Gaming Mouse", "price": 80.0 }
   { "index": { "_id": 5 } }
   { "title": "Wireless Mouse Holder", "price": 1.0 }
   { "index": { "_id": 6 } }
   { "title": "Wireless Mouse", "price": 1.0 }
   { "index": { "_id": 7 } }
   { "title": "Wireless Mouse" }
   { "index": { "_id": 8 } }
   { "title": "Wireless Mouse", "price": null }


3. ** Validation Query *
   ```bash
   POST /products/_search
    {
    "query": {
    "custom_match": {
    "price_field": "price",
    "query": {
    "match": {
    "title": "wireless mouse"
    }
    }
    }
    }
    }
