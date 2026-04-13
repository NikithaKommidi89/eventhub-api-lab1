package com.eventhub.api.controller;

import com.eventhub.api.dto.ai.ChatRequest;
import com.eventhub.api.dto.ai.EventDescriptionRequest;
import com.eventhub.api.dto.ai.EventSchedule;
import com.eventhub.api.dto.ai.EventTagSuggestions;
import com.eventhub.api.dto.ai.FaqEntry;
import com.eventhub.api.dto.ai.FaqSearchRequest;
import com.eventhub.api.dto.ai.RagResponse;
import com.eventhub.api.dto.ai.ScheduleRequest;
import com.eventhub.api.dto.ai.StructuredEventDescription;
import com.eventhub.api.service.AiService;
import com.eventhub.api.service.FaqService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI", description = "AI-powered event features")
public class AiController {

    private final AiService aiService;
    private final FaqService faqService;

    public AiController(AiService aiService, FaqService faqService) {
        this.aiService = aiService;
        this.faqService = faqService;
    }

    @GetMapping("/health")
    @Operation(summary = "Check AI service health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", aiService.isHealthy() ? "healthy" : "unhealthy",
                "provider", "gemini"
        ));
    }

    @PostMapping("/chat")
    @Operation(summary = "General AI chat")
    public ResponseEntity<Map<String, String>> chat(@Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(Map.of("response", aiService.chat(request.getPrompt())));
    }

    @PostMapping("/events/description")
    @Operation(summary = "Generate event description")
    public ResponseEntity<Map<String, String>> generateDescription(@Valid @RequestBody EventDescriptionRequest request) {
        return ResponseEntity.ok(Map.of("description", aiService.generateDescription(request)));
    }

    @PostMapping("/events/description/structured")
    @Operation(summary = "Generate structured event description")
    public ResponseEntity<StructuredEventDescription> generateStructuredDescription(@Valid @RequestBody EventDescriptionRequest request) {
        return ResponseEntity.ok(aiService.generateStructuredDescription(request));
    }

    @PostMapping("/events/tags")
    @Operation(summary = "Suggest event tags")
    public ResponseEntity<EventTagSuggestions> suggestTags(@Valid @RequestBody EventDescriptionRequest request) {
        return ResponseEntity.ok(aiService.suggestTags(request));
    }

    @PostMapping("/events/schedule")
    @Operation(summary = "Generate event schedule")
    public ResponseEntity<EventSchedule> generateSchedule(@Valid @RequestBody ScheduleRequest request) {
        return ResponseEntity.ok(aiService.generateSchedule(request));
    }

    @PostMapping("/faq/load")
    @Operation(summary = "Load FAQ entries into vector store")
    public ResponseEntity<Map<String, Object>> loadFaqs() {
        int count = faqService.loadFaqs();
        return ResponseEntity.ok(Map.of("message", "FAQs loaded successfully", "count", count));
    }

    @PostMapping("/faq/search")
    @Operation(summary = "Search FAQs semantically")
    public ResponseEntity<Object> searchFaqs(@Valid @RequestBody FaqSearchRequest request) {
        List<FaqEntry> results = faqService.searchFaqs(request.getQuery(), request.getTopK(), request.getThreshold());
        if (results.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "message", "No FAQs found. Try different keywords or contact support.",
                    "results", results
            ));
        }
        return ResponseEntity.ok(Map.of("results", results));
    }

    @PostMapping("/faq/ask")
    @Operation(summary = "Ask a question using RAG")
    public ResponseEntity<RagResponse> askQuestion(@Valid @RequestBody FaqSearchRequest request) {
        return ResponseEntity.ok(faqService.answerQuestion(request.getQuery()));
    }

}