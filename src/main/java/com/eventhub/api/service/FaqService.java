package com.eventhub.api.service;

import com.eventhub.api.dto.ai.FaqEntry;
import com.eventhub.api.dto.ai.RagResponse;

import java.util.List;

public interface FaqService {

    /*
     * Loads all FAQ entries into the vector store.
     */
    int loadFaqs();

    /*
     * Searches FAQs using semantic similarity.
     */
    List<FaqEntry> searchFaqs(String query, int topK, double minSimilarity);

    /*
     * Answers a question using RAG with the FAQ knowledge base.
     */
    RagResponse answerQuestion(String question);
}