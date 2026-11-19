package com.couchbase_spring_ai.demo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.ai.document.Document;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Turns the bundled BBC news sample data into Spring AI {@link Document}s.
 *
 * <p>The resource is read as a stream rather than as a {@link java.io.File}, so loading works both
 * from the IDE and from the packaged Spring Boot jar, where classpath resources are not files.
 */
public final class NewsDataset {

    public static final String DEFAULT_LOCATION = "bbc_news_data.json";

    private NewsDataset() {
    }

    /** Reads the sample data bundled with the application. */
    public static List<Document> load() throws IOException {
        return load(new ClassPathResource(DEFAULT_LOCATION));
    }

    /**
     * Reads a JSON array of articles. Each article needs {@code content} and {@code title}; the
     * document id is the article's 1-based position so reloading overwrites instead of duplicating.
     */
    public static List<Document> load(Resource resource) throws IOException {
        String json;
        try (InputStream in = resource.getInputStream()) {
            json = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
        JSONArray articles = new JSONArray(json);
        List<Document> documents = new ArrayList<>(articles.length());
        for (int i = 0; i < articles.length(); i++) {
            JSONObject article = articles.getJSONObject(i);
            documents.add(new Document(
                    String.valueOf(i + 1),
                    article.getString("content"),
                    Map.of("title", article.getString("title"))));
        }
        return documents;
    }
}
