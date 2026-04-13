package com.eventhub.api.dto.ai;

import java.util.List;

public class EventSchedule {

    private String eventName;
    private String date;
    private List<SessionItem> sessions;
    private String notes;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<SessionItem> getSessions() {
        return sessions;
    }

    public void setSessions(List<SessionItem> sessions) {
        this.sessions = sessions;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}