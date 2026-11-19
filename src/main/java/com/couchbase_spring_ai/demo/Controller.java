package com.couchbase_spring_ai.demo;


import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/tutorial"})
public class Controller {
    @Autowired
    private VectorStore couchbaseSearchVectorStore;

    public Controller() {
    }

    @Operation(
            summary = "Load data into the couchbase bucket"
    )
    @GetMapping({"/load"})
    public String loadDocuments() {
        try {
            List<Document> docL = NewsDataset.load();
            this.couchbaseSearchVectorStore.add(docL);
            return "Documents added successfully!\n";
        } catch (Exception e) {
            return "Problem Reading File " + e.getMessage();
        }
    }

    @Operation(
            summary = ""
    )
    @GetMapping({"/search"})
    public List<Map<String, Object>> searchDocuments() throws NullPointerException {
        List<Document> results = this.couchbaseSearchVectorStore.similaritySearch(SearchRequest.builder().query("Give me some sports news").similarityThreshold((double)0.75F).topK(15).build());
        return (List)results.stream().map((doc) -> Map.of("content", doc.getText(), "metadata", doc.getMetadata())).collect(Collectors.toList());
    }
}