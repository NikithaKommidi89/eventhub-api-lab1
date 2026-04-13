package com.eventhub.api.util;

import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class PiiSanitizer {

    private static final Pattern EMAIL =
            Pattern.compile("[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}",
                    Pattern.CASE_INSENSITIVE);

    private static final Pattern PHONE =
            Pattern.compile("(\\+?1[\\s.\\-]?)?(\\(?\\d{3}\\)?[\\s.\\-]?)\\d{3}[\\s.\\-]?\\d{4}");

    // Canadian SIN: 9 digits formatted as XXX-XXX-XXX
    private static final Pattern SIN =
            Pattern.compile("\\b\\d{3}[\\s\\-]?\\d{3}[\\s\\-]?\\d{3}\\b");

    // Credit card: 16 digits grouped by 4
    private static final Pattern CREDIT_CARD =
            Pattern.compile("\\b\\d{4}[\\s\\-]?\\d{4}[\\s\\-]?\\d{4}[\\s\\-]?\\d{4}\\b");

    /*
     * Removes all PII from the input before sending to AI.
     */
    public String sanitize(String input) {
        if (input == null || input.isBlank()) return "";
        String out = EMAIL.matcher(input).replaceAll("[EMAIL REMOVED]");
        out = PHONE.matcher(out).replaceAll("[PHONE REMOVED]");
        out = SIN.matcher(out).replaceAll("[SIN REMOVED]");
        out = CREDIT_CARD.matcher(out).replaceAll("[CARD REMOVED]");
        return out.trim();
    }
}