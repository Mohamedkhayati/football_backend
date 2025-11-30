package com.football.assistant.controller;

import com.football.assistant.service.PdfRagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping("/api/pdf")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PdfController {

    private final PdfRagService pdfRagService;

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String pdfId = UUID.randomUUID().toString();

        Path dir = Path.of("uploaded-pdfs");
        Files.createDirectories(dir);
        Path path = dir.resolve(pdfId + "-" + file.getOriginalFilename());
        Files.write(path, file.getBytes());

        pdfRagService.indexPdf(path, pdfId);

        return ResponseEntity.ok(new UploadResponse(pdfId));
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        String answer = pdfRagService.ask(request.pdfId(), request.question());
        return ResponseEntity.ok(new ChatResponse(answer));
    }

    public record UploadResponse(String pdfId) {}
    public record ChatRequest(String pdfId, String question) {}
    public record ChatResponse(String answer) {}
}
