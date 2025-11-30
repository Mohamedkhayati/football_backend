package com.football.assistant.service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.apache.pdfbox.Loader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfRagService {

    private final ChatLanguageModel chatLanguageModel;
    private final EmbeddingModel embeddingModel;

    // Store embeddings with String chunks (no Document class)
    private final EmbeddingStore<Embedding> embeddingStore = new InMemoryEmbeddingStore<>();

    // Keep parallel list of (pdfId, textChunk) for filtering
    private final List<PdfChunk> chunks = new ArrayList<>();

    public String indexPdf(Path pdfPath, String pdfId) throws IOException {
        String text = extractText(pdfPath);

        // Simple character-based split into chunks of ~1000 chars
        int chunkSize = 1000;
        for (int start = 0; start < text.length(); start += chunkSize) {
            int end = Math.min(start + chunkSize, text.length());
            String part = text.substring(start, end).trim();
            if (part.isEmpty()) continue;

            Embedding embedding = embeddingModel.embed(part).content();
            embeddingStore.add(part, embedding);   // store text as the associated object
            chunks.add(new PdfChunk(pdfId, part));
        }

        return "Indexed PDF " + pdfId;
    }

    public String ask(String pdfId, String question) {
        Embedding queryEmbedding = embeddingModel.embed(question).content();

        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(10)
                .build();

        EmbeddingSearchResult<Embedding> searchResult = embeddingStore.search(request);

        StringBuilder context = new StringBuilder();
        for (EmbeddingMatch<Embedding> match : searchResult.matches()) {
            Object obj = match.embedded();  // this is the text chunk we stored
            if (obj instanceof String textChunk) {
                // Only use chunks belonging to this pdfId
                if (belongsToPdf(pdfId, textChunk)) {
                    context.append(textChunk).append("\n\n");
                }
            }
        }

        String prompt = """
                You are a football assistant.
                Answer ONLY from the following PDF context. If the answer is not in the context, say you don't know.

                Context:
                %s

                Question: %s
                """.formatted(context.toString(), question);

        return chatLanguageModel.generate(prompt);
    }

    private boolean belongsToPdf(String pdfId, String chunkText) {
        for (PdfChunk c : chunks) {
            if (c.pdfId().equals(pdfId) && c.text().equals(chunkText)) {
                return true;
            }
        }
        return false;
    }

    private String extractText(Path path) throws IOException {
        try (PDDocument document = Loader.loadPDF(path.toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private record PdfChunk(String pdfId, String text) {}
}
