package com.football.assistant.tools;

import dev.langchain4j.agent.tool.Tool;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.apache.pdfbox.Loader;

import java.io.File;
import java.io.IOException;

@Component
public class PdfReaderTool {
    
    @Tool("Read and extract text content from a PDF file about football")
    public String readPdfFile(String fileName) {
        try {
            String resourcePath = "src/main/resources/data/" + fileName;
            File file = new File(resourcePath);
            
            if (!file.exists()) {
                return "PDF file not found: " + fileName;
            }
            
            try (PDDocument document = Loader.loadPDF(file)) {
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);
                
                // Limit to first 2000 characters for context
                return text.length() > 2000 ? text.substring(0, 2000) : text;
            }
        } catch (IOException e) {
            return "Error reading PDF: " + e.getMessage();
        }
    }
}
