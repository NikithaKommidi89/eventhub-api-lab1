package com.eventhub.api.dto.ai;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class ScheduleRequest {

    @NotBlank(message = "Event name is required")
    private String eventName;

    private int durationHours;
    private String startTime;
    private List<String> sessions;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(int durationHours) {
        this.durationHours = durationHours;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public List<String> getSessions() {
        return sessions;
    }

    public void setSessions(List<String> sessions) {
        this.sessions = sessions;
    }
}