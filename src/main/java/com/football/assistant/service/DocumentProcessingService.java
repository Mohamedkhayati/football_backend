package com.football.assistant.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.pdfbox.Loader;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentProcessingService {
    
    @Autowired
    private EmbeddingModel embeddingModel;
    
    @Autowired
    private EmbeddingStore<TextSegment> embeddingStore;
    
    @Value("${rag.chunk-size}")
    private int chunkSize;
    
    @Value("${rag.chunk-overlap}")
    private int chunkOverlap;
    
    @PostConstruct
    public void loadDocuments() {
        System.out.println("Loading football documents...");
        File dataDir = new File("src/main/resources/data");
        
        if (!dataDir.exists() || !dataDir.isDirectory()) {
            System.out.println("Data directory not found. Creating it...");
            dataDir.mkdirs();
            return;
        }
        
        File[] pdfFiles = dataDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
        
        if (pdfFiles == null || pdfFiles.length == 0) {
            System.out.println("No PDF files found in data directory.");
            return;
        }
        
        List<Document> documents = new ArrayList<>();
        
        for (File pdfFile : pdfFiles) {
            try {
                String content = extractTextFromPdf(pdfFile);
                documents.add(new Document(content));
                System.out.println("Loaded: " + pdfFile.getName());
            } catch (IOException e) {
                System.err.println("Error loading " + pdfFile.getName() + ": " + e.getMessage());
            }
        }
        
        if (!documents.isEmpty()) {
            ingestDocuments(documents);
            System.out.println("Successfully loaded " + documents.size() + " documents.");
        }
    }
    
    private String extractTextFromPdf(File pdfFile) throws IOException {
        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
    
    private void ingestDocuments(List<Document> documents) {
        DocumentSplitter splitter = DocumentSplitters.recursive(chunkSize, chunkOverlap);
        
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(splitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        
        ingestor.ingest(documents);
    }
    
    public void addDocument(String content) {
        Document document = new Document(content);
        ingestDocuments(List.of(document));
    }
}
