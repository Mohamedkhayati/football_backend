package com.football.assistant.controller;

import com.football.assistant.model.ChatRequest;
import com.football.assistant.model.ChatResponse;
import com.football.assistant.service.FootballAgentService;
import com.football.assistant.service.RagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
public class FootballChatController {

    @Autowired
    private FootballAgentService agentService;

    @Autowired
    private RagService ragService;

    /**
     * Main chat endpoint: retrieves relevant PDF info using RagService,
     * sends user question + context to agentService for answer.
     */
    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        try {
            String conversationId =
                request.getConversationId() != null
                    ? request.getConversationId()
                    : UUID.randomUUID().toString();

            // 1. Find relevant context from PDF with user query
            String context = ragService.findRelevantContext(request.getMessage(), 3);

            // 2. Send both context and user query to the AI agent
            String answer = agentService.chat(request.getMessage(), context);

            // 3. Build response
            ChatResponse chatResponse = new ChatResponse(answer, conversationId);
            chatResponse.setSuccess(true);
            return ResponseEntity.ok(chatResponse);

        } catch (Exception e) {
            ChatResponse errorResponse = new ChatResponse();
            errorResponse.setSuccess(false);
            errorResponse.setError("Error processing request: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * File upload endpoint: for expanding PDF data at runtime.
     * (Enable ragService.addDocument if you implement runtime indexing.)
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            // Enable dynamic PDF ingestion if implemented
            // ragService.addDocument(content);

            return ResponseEntity.ok("Document uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                .body("Error uploading document: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Football Assistant API is running!");
    }
}
