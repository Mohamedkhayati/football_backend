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
    
    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        try {
            String conversationId = request.getConversationId() != null ? 
                    request.getConversationId() : UUID.randomUUID().toString();
            
            // Get relevant context from RAG
            String context = ragService.findRelevantContext(request.getMessage(), 3);
            
            // Get response from AI agent
            String response = agentService.chat(request.getMessage(), context);
            
            ChatResponse chatResponse = new ChatResponse(response, conversationId);
            chatResponse.setSuccess(true);
            
            return ResponseEntity.ok(chatResponse);
            
        } catch (Exception e) {
            ChatResponse errorResponse = new ChatResponse();
            errorResponse.setSuccess(false);
            errorResponse.setError("Error processing request: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }
            
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            // Process and add to RAG
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
