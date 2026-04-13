// CPRG 220 Assignment 4
// EventHub
// 2026-03-28

package com.eventhub.api.dto.ai;

public class FaqEntry {

    private String id;
    private String category;
    private String question;
    private String answer;
    private double similarity;

    public FaqEntry() {
    }

    public FaqEntry(String id, String category, String question, String answer, double similarity) {
        this.id = id;
        this.category = category;
        this.question = question;
        this.answer = answer;
        this.similarity = similarity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }
}