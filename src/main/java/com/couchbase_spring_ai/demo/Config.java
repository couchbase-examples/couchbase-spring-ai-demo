package com.couchbase_spring_ai.demo;


import com.couchbase.client.java.Cluster;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.CouchbaseSearchVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.CouchbaseSearchVectorStore.CouchbaseSearchVectorStoreConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootConfiguration
@EnableAutoConfiguration
public class Config {
    @Value("${spring.couchbase.connection-string}")
    private String connectionUrl;
    @Value("${spring.couchbase.username}")
    private String username;
    @Value("${spring.couchbase.password}")
    private String password;
    @Value("${spring.ai.openai.api-key}")
    private String openaiKey;

    public Config() {
    }

    @Bean
    public Cluster cluster() {
        return Cluster.connect(this.connectionUrl, this.username, this.password);
    }

    @Bean
    public CouchbaseSearchVectorStore.CouchbaseSearchVectorStoreConfig configuration() {
        return CouchbaseSearchVectorStoreConfig.builder().withBucketName("test").withScopeName("test").withCollectionName("test").build();
    }

    @Bean
    public Boolean initializeSchema() {
        return true;
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return new OpenAiEmbeddingModel(new OpenAiApi(this.openaiKey));
    }

    @Bean
    public VectorStore couchbaseSearchVectorStore(Cluster cluster, CouchbaseSearchVectorStore.CouchbaseSearchVectorStoreConfig configuration, EmbeddingModel embeddingModel, Boolean initializeSchema) {
        return CouchbaseSearchVectorStore.builder(cluster, configuration, embeddingModel).initializeSchema(true).build();
    }
}
