package com.eventhub.api.util;

import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class OutputValidator {

    private static final String FALLBACK =
            "Unable to process request. Please try again.";

    private static final Pattern SENSITIVE_PATTERN = Pattern.compile(
            "(?i)(api_key|password|secret|system_prompt|instruction)"
    );

    /*
     * Returns the response if safe, otherwise returns fallback message.
     */
    public String validate(String response) {
        if (response == null || response.isBlank()) return FALLBACK;
        if (SENSITIVE_PATTERN.matcher(response).find()) return FALLBACK;
        return response;
    }

    /*
     * Returns true if the response passes validation.
     */
    public boolean isValid(String response) {
        if (response == null || response.isBlank()) return false;
        return !SENSITIVE_PATTERN.matcher(response).find();
    }
}