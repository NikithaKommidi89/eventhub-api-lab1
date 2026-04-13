package com.eventhub.api.service;

import com.eventhub.api.dto.ai.FaqEntry;
import com.eventhub.api.dto.ai.RagResponse;
import com.eventhub.api.util.PiiSanitizer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class FaqServiceImpl implements FaqService {

    private static final String NO_CONTEXT_RESPONSE =
            "I couldn't find relevant information in our FAQ. Please contact our support team at support@eventhub.com for assistance.";

    private static final List<FaqData> FAQ_DATA = List.of(
            new FaqData("faq-1", "registration", "How do I register for an event?",
                    "To register, navigate to the event page and click Register. Provide your name, email, and select your ticket type. You will receive a confirmation email with your ticket and QR code."),

            new FaqData("faq-2", "registration", "Can I register multiple people at once?",
                    "Yes, during checkout click Add Another Attendee to include additional people. Each attendee will receive their own confirmation email and ticket."),

            new FaqData("faq-3", "refund", "What is your refund policy?",
                    "Refunds are available up to 7 days before the event for a full refund. Cancellations 3-7 days before receive a 50% refund. No refunds within 3 days of the event."),

            new FaqData("faq-4", "refund", "How do I cancel my registration?",
                    "Log into your account, go to My Registrations, find the event and click Cancel Registration. Refund eligibility depends on how close you are to the event date."),

            new FaqData("faq-5", "venue", "Is parking available at the venue?",
                    "Most venues offer on-site parking. Check the event page for parking details and costs. We recommend arriving early for popular events."),

            new FaqData("faq-6", "venue", "Are your venues wheelchair accessible?",
                    "Yes, all EventHub partner venues are wheelchair accessible with ramps, accessible restrooms, and designated seating. Contact us 48 hours before the event for special accommodations."),

            new FaqData("faq-7", "schedule", "What time should I arrive at the event?",
                    "We recommend arriving 30-60 minutes before the event start time. Doors typically open 1 hour before the scheduled start time."),

            new FaqData("faq-8", "schedule", "Will there be breaks during multi-session events?",
                    "Yes, multi-session events include scheduled breaks. Typically 15 minutes between sessions and 30-60 minutes for lunch on full-day events."),

            new FaqData("faq-9", "dresscode", "What should I wear to the event?",
                    "Dress code varies by event type. Business casual is appropriate for conferences. Outdoor festivals allow casual attire. Formal events will specify dress code on the event page."),

            new FaqData("faq-10", "dresscode", "What should I bring to the event?",
                    "Bring your ticket (printed or digital), a valid ID, and any materials mentioned on the event page. For outdoor events bring sunscreen and weather-appropriate gear."),

            new FaqData("faq-11", "support", "How do I contact customer support?",
                    "You can reach support via email at support@eventhub.com or through live chat on our website. Response time is typically within 24 hours for email inquiries."),

            new FaqData("faq-12", "support", "I have not received my confirmation email, what should I do?",
                    "First check your spam folder. If not found, log into your account to verify your registration and resend the confirmation email from your registration page."),

            new FaqData("faq-13", "payment", "What payment methods do you accept?",
                    "We accept all major credit cards (Visa, MasterCard, American Express), PayPal, Apple Pay, and Google Pay. All transactions are secured with SSL encryption."),

            new FaqData("faq-14", "transfer", "Can I transfer my ticket to someone else?",
                    "Yes, ticket transfers are allowed up to 24 hours before the event. Go to My Registrations, select the ticket, and click Transfer Ticket to enter the new attendee's email.")
    );

    private final VectorStore vectorStore;
    private final ChatClient chatClient;
    private final PiiSanitizer piiSanitizer;

    /*
     * Creates the service using the vector store, chat client, and PII sanitizer.
     */
    public FaqServiceImpl(VectorStore vectorStore, ChatClient.Builder chatClientBuilder, PiiSanitizer piiSanitizer) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder.build();
        this.piiSanitizer = piiSanitizer;
    }

    @Override
    /*
     * Loads all FAQ entries into the vector store.
     */
    public int loadFaqs() {
        List<Document> documents = FAQ_DATA.stream()
                .map(faq -> new Document(
                        faq.question() + " " + faq.answer(),
                        Map.of(
                                "id",       faq.id(),
                                "category", faq.category(),
                                "question", faq.question(),
                                "answer",   faq.answer()
                        )
                ))
                .toList();
        vectorStore.add(documents);
        return documents.size();
    }

    @Override
    /*
     * Searches FAQs using semantic similarity.
     */
    public List<FaqEntry> searchFaqs(String query, int topK, double minSimilarity) {
        String sanitized = piiSanitizer.sanitize(query);
        List<Document> results = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(sanitized)
                        .topK(topK)
                        .similarityThreshold(minSimilarity)
                        .build()
        );
        if (results.isEmpty()) return Collections.emptyList();
        return results.stream()
                .map(doc -> new FaqEntry(
                        (String) doc.getMetadata().get("id"),
                        (String) doc.getMetadata().get("category"),
                        (String) doc.getMetadata().get("question"),
                        (String) doc.getMetadata().get("answer"),
                        doc.getScore()
                ))
                .toList();
    }

    @Override
    /*
     * Answers a question using RAG with the FAQ knowledge base.
     */
    public RagResponse answerQuestion(String question) {
        String sanitized = piiSanitizer.sanitize(question);
        List<FaqEntry> relevantFaqs = searchFaqs(sanitized, 3, 0.7);

        if (relevantFaqs.isEmpty()) {
            return new RagResponse(NO_CONTEXT_RESPONSE, Collections.emptyList(), false);
        }

        StringBuilder context = new StringBuilder();
        for (FaqEntry faq : relevantFaqs) {
            context.append("Q: ").append(faq.getQuestion()).append("\n");
            context.append("A: ").append(faq.getAnswer()).append("\n\n");
        }

        String prompt = String.format("""
                Answer the question using only this FAQ context.
                If unsure, say you don't know.

                Context:
                %s

                Question: %s
                """, context, sanitized);

        String answer = chatClient.prompt()
                .user(prompt)
                .options(GoogleGenAiChatOptions.builder()
                        .temperature(0.3)
                        .maxOutputTokens(500)
                        .build())
                .call()
                .content();

        return new RagResponse(answer, relevantFaqs, true);
    }

    /*
     * Internal record to hold FAQ data.
     */
    private record FaqData(String id, String category, String question, String answer) {}
}