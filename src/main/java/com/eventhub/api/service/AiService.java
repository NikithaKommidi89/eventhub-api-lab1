// CPRG 220 Assignment 4
// EventHub
// 2026-03-28

package com.eventhub.api.service;

import com.eventhub.api.dto.ai.EventDescriptionRequest;
import com.eventhub.api.dto.ai.EventSchedule;
import com.eventhub.api.dto.ai.EventTagSuggestions;
import com.eventhub.api.dto.ai.ScheduleRequest;
import com.eventhub.api.dto.ai.StructuredEventDescription;

public interface AiService {

    /*
     * Checks if the AI service is responsive.
     */
    boolean isHealthy();

    /*
     * Sends a simple text prompt to the AI.
     */
    String chat(String prompt);

    /*
     * Generates a marketing description for an event.
     */
    String generateDescription(EventDescriptionRequest request);

    /*
     * Generates structured event description as a typed object.
     */
    StructuredEventDescription generateStructuredDescription(EventDescriptionRequest request);

    /*
     * Suggests relevant tags for an event.
     */
    EventTagSuggestions suggestTags(EventDescriptionRequest request);

    /*
     * Generates a suggested schedule for a multi-session event.
     */
    EventSchedule generateSchedule(ScheduleRequest request);
}