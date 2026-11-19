package com.couchbase_spring_ai.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.core.io.ByteArrayResource;

class NewsDatasetTests {

    @Test
    void mapsArticlesToDocumentsWithPositionalIdsAndTitleMetadata() throws Exception {
        String json = """
                [
                  {"title": "First", "content": "Alpha body", "section": "Europe"},
                  {"title": "Second", "content": "Beta body"}
                ]
                """;

        List<Document> documents = NewsDataset.load(new ByteArrayResource(json.getBytes(StandardCharsets.UTF_8)));

        assertEquals(2, documents.size());
        assertEquals("1", documents.get(0).getId());
        assertEquals("Alpha body", documents.get(0).getText());
        assertEquals(Map.of("title", "First"), documents.get(0).getMetadata());
        assertEquals("2", documents.get(1).getId());
        assertEquals(Map.of("title", "Second"), documents.get(1).getMetadata());
    }

    @Test
    void rejectsArticlesWithoutContent() {
        String json = "[{\"title\": \"No body\"}]";

        assertThrows(JSONException.class,
                () -> NewsDataset.load(new ByteArrayResource(json.getBytes(StandardCharsets.UTF_8))));
    }

    @Test
    void loadsBundledSampleData() throws Exception {
        List<Document> documents = NewsDataset.load();

        assertFalse(documents.isEmpty());
        for (Document document : documents) {
            assertFalse(document.getText().isBlank());
            assertFalse(((String) document.getMetadata().get("title")).isBlank());
        }
    }
}
