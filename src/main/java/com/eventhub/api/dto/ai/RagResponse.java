package com.eventhub.api.dto.ai;

import java.util.List;

public class RagResponse {

    private String answer;
    private List<FaqEntry> sources;
    private boolean contextFound;

    public RagResponse() {
    }

    public RagResponse(String answer, List<FaqEntry> sources, boolean contextFound) {
        this.answer = answer;
        this.sources = sources;
        this.contextFound = contextFound;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<FaqEntry> getSources() {
        return sources;
    }

    public void setSources(List<FaqEntry> sources) {
        this.sources = sources;
    }

    public boolean isContextFound() {
        return contextFound;
    }

    public void setContextFound(boolean contextFound) {
        this.contextFound = contextFound;
    }
}