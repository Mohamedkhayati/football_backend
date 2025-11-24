package com.football.assistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.football.assistant.model.ImageAnalysisResponse;
import com.football.assistant.service.ImageAnalysisService;

@RestController
@RequestMapping("/api/image")
public class ImageController {
    
    @Autowired
    private ImageAnalysisService imageAnalysisService;
    
    @PostMapping("/analyze")
    public ResponseEntity<ImageAnalysisResponse> analyzeImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam(value = "description", required = false) String userDescription) {
        try {
            ImageAnalysisResponse response = imageAnalysisService.analyzeFootballImage(image, userDescription);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ImageAnalysisResponse error = new ImageAnalysisResponse();
            error.setSuccess(false);
            error.setError("Error analyzing image: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
