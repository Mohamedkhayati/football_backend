package com.football.assistant.service;

import com.football.assistant.model.ImageAnalysisResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageAnalysisService {
    
    @Autowired
    private FootballAgentService agentService;
    
    @Autowired
    private RagService ragService;
    
    private static final String UPLOAD_DIR = "uploads/images/";
    
    public ImageAnalysisResponse analyzeFootballImage(MultipartFile image, String userDescription) throws IOException {
        // Save image
        String imageUrl = saveImage(image);
        
        // Create analysis prompt
        String prompt = buildAnalysisPrompt(image.getOriginalFilename(), userDescription);
        
        // Get context from documents
        String context = ragService.findRelevantContext(prompt, 3);
        
        // Get AI analysis
        String analysis = agentService.chat(prompt, context);
        
        // Build response
        ImageAnalysisResponse response = new ImageAnalysisResponse();
        response.setAnalysis(analysis);
        response.setImageUrl(imageUrl);
        response.setSuccess(true);
        
        return response;
    }
    
    private String saveImage(MultipartFile image) throws IOException {
        // Create upload directory if not exists
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        // Generate unique filename
        String filename = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + filename);
        
        // Save file
        Files.write(filePath, image.getBytes());
        
        return "/uploads/images/" + filename;
    }
    
    private String buildAnalysisPrompt(String filename, String userDescription) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analyze this football-related image");
        
        if (userDescription != null && !userDescription.isEmpty()) {
            prompt.append(". User says: ").append(userDescription);
        }
        
        prompt.append(". Provide detailed information about:");
        prompt.append("\n- Player identification (if recognizable)");
        prompt.append("\n- Team/jersey details");
        prompt.append("\n- Playing position and style");
        prompt.append("\n- Notable achievements or statistics");
        prompt.append("\n- Historical context");
        
        return prompt.toString();
    }
}
