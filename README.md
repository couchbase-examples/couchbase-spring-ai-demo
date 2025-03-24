# Spring AI with Couchbase Vector Store Quickstart Guide

## Overview

This project demonstrates how to implement vector search capabilities using Spring AI with Couchbase as a vector database. It showcases:

- Setting up a Spring Boot application with Spring AI
- Configuring Couchbase as a vector store
- Using OpenAI to generate embeddings
- Creating REST endpoints for loading and searching vector data
- Performing similarity searches on news article content

The application uses a sample dataset of BBC news articles to demonstrate how to store document embeddings and perform semantic searches based on natural language queries.

### What is Spring AI?

Spring AI is an extension of the Spring Framework that simplifies the integration of AI capabilities into Spring applications. It provides abstractions and integrations for working with various AI services and models, making it easier for developers to incorporate AI functionality without having to manage low-level implementation details.

Key features of Spring AI include:
- **Model integrations**: Pre-built connectors to popular AI models (like OpenAI)
- **Prompt engineering**: Tools for crafting and managing prompts
- **Vector stores**: Abstractions for storing and retrieving vector embeddings
- **Document processing**: Utilities for working with unstructured data

### Why Use Spring AI?

Spring AI brings several benefits to Java developers:
1. **Familiar programming model**: Uses Spring's dependency injection and configuration
2. **Abstraction layer**: Provides consistent interfaces across different AI providers
3. **Enterprise-ready**: Built with production use cases in mind
4. **Simplified development**: Reduces boilerplate code for AI integrations



## Project Structure

```
src/main/java/com/couchbase_spring_ai/demo/
├── Config.java                         # Application configuration
├── Controller.java                     # REST API endpoints
└── CouchbaseSpringAiDemoApplications.java  # Application entry point

src/main/resources/
├── application.properties              # Application settings
└── bbc_news_data.json                  # Sample data
```

## Setup and Configuration

### Prerequisites

- Java 21
- Maven
- Couchbase Server running locally
- OpenAI API key

### Configuration Details

The application is configured in `application.properties`:

```properties
spring.application.name=spring-ai-demo
spring.ai.openai.api-key=your-openai-api-key
spring.couchbase.connection-string=couchbase://127.0.0.1
spring.couchbase.username=Administrator
spring.couchbase.password=password
```

The `Config` class sets up:
1. Couchbase cluster connection
2. OpenAI embedding model
3. Couchbase vector store configuration

## Key Components

### Configuration Class (`Config.java`)

This class creates the necessary beans for:
- Connecting to Couchbase cluster
- Setting up the OpenAI embedding model
- Configuring the Couchbase vector store

The vector store is configured to use:
- Bucket: "test"
- Scope: "test"
- Collection: "test"

### Controller Class (`Controller.java`)

Provides REST API endpoints:
- `/tutorial/load`: Loads sample BBC news data into Couchbase
- `/tutorial/search`: Performs a semantic search for sports-related news articles

### Vector Store Integration

The application uses `CouchbaseSearchVectorStore`, which:
- Stores document embeddings in Couchbase
- Provides similarity search capabilities
- Maintains metadata alongside vector embeddings

## Using the Application

### Loading Data

1. Start the application
2. Make a GET request to `http://localhost:8080/tutorial/load`
3. This loads BBC news articles from the included JSON file into Couchbase, creating embeddings via OpenAI

### Performing Similarity Searches

1. Make a GET request to `http://localhost:8080/tutorial/search`
2. The application will search for documents semantically similar to "Give me some sports news"
3. Results are returned with content and metadata, sorted by similarity score

## Implementation Details

### Vector Embedding Process

1. The application reads BBC news data from a JSON file
2. Each article is converted to a Spring AI `Document` object
3. The embedding model generates vector representations of the document text
4. Documents and their vectors are stored in Couchbase

### Similarity Search

The application uses a semantic search approach:
1. User query is converted to a vector using the same embedding model
2. Vector similarity search is performed against stored document vectors
3. Results above a similarity threshold (0.75) are returned
4. Up to 15 results are included (topK parameter)

## Resources

- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/index.html)
- [Couchbase Vector Search](https://docs.couchbase.com/server/current/fts/vector-search.html)
- [OpenAI Embeddings Documentation](https://platform.openai.com/docs/guides/embeddings)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
