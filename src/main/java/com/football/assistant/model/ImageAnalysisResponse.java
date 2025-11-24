package com.football.assistant.model;

import java.time.LocalDateTime;

public class ImageAnalysisResponse {
    private String analysis;
    private String imageUrl;
    private boolean success;
    private String error;
    private LocalDateTime timestamp = LocalDateTime.now();
    
    // Getters and setters
    public String getAnalysis() { return analysis; }
    public void setAnalysis(String analysis) { this.analysis = analysis; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
