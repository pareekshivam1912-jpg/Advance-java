package com.shopsphere.util;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * RTU Advanced Java Syllabus Practical Demonstration:
 * Internationalization (i18n) and Localization (l10n).
 *
 * Demonstrates:
 * 1. java.util.Locale instantiation (English, Hindi, India).
 * 2. java.util.ResourceBundle reading messages_en.properties and messages_hi.properties.
 * 3. java.text.NumberFormat for Indian Rupee (INR) currency formatting.
 * 4. java.time.format.DateTimeFormatter / java.text.DateFormat for localized dates.
 * 5. java.text.MessageFormat for dynamic positional token substitution ({0}, {1}).
 */
public class I18nDemo {

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("   SHOPSPHERE — RTU ADVANCED JAVA SYLLABUS: i18n & l10n DEMONSTRATION     ");
        System.out.println("==========================================================================");

        Locale enLocale = Locale.of("en", "IN");
        Locale hiLocale = Locale.of("hi", "IN");

        // 1. ResourceBundle Demonstration
        System.out.println("\n--- [PART 1] java.util.ResourceBundle: English vs Hindi UI Keys ---");
        demonstrateResourceBundle(enLocale, "ENGLISH (en_IN)");
        demonstrateResourceBundle(hiLocale, "HINDI (hi_IN)");

        // 2. NumberFormat Currency Demonstration
        System.out.println("\n--- [PART 2] java.text.NumberFormat: Currency Localization ---");
        BigDecimal amount = new BigDecimal("149999.50");
        demonstrateCurrencyFormatting(amount, enLocale, "English India");
        demonstrateCurrencyFormatting(amount, hiLocale, "Hindi India");
        demonstrateCurrencyFormatting(amount, Locale.US, "US Dollar");

        // 3. Date & Time Formatting
        System.out.println("\n--- [PART 3] java.time.format.DateTimeFormatter: Localized Timestamps ---");
        LocalDateTime now = LocalDateTime.now();
        demonstrateDateTimeFormatting(now, enLocale, "English India");
        demonstrateDateTimeFormatting(now, hiLocale, "Hindi India");

        // 4. MessageFormat Positional Parameter Demonstration
        System.out.println("\n--- [PART 4] java.text.MessageFormat: Dynamic String Interpolation ---");
        demonstrateMessageFormat(enLocale);
        demonstrateMessageFormat(hiLocale);

        // 5. Academic Theory & Viva Summary
        printSyllabusExplanation();
    }

    private static void demonstrateResourceBundle(Locale locale, String label) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        System.out.println(">>> ResourceBundle for " + label + ":");
        System.out.println("    - App Name:        " + bundle.getString("app.name"));
        System.out.println("    - Nav Home:        " + bundle.getString("nav.home"));
        System.out.println("    - Nav Products:    " + bundle.getString("nav.products"));
        System.out.println("    - Nav Cart:        " + bundle.getString("nav.cart"));
        System.out.println("    - Add to Cart:     " + bundle.getString("btn.add_to_cart"));
        System.out.println("    - Place Order:     " + bundle.getString("btn.place_order"));
    }

    private static void demonstrateCurrencyFormatting(BigDecimal amount, Locale locale, String label) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        String formatted = currencyFormatter.format(amount);
        System.out.printf("    %-18s : %s%n", label, formatted);
    }

    private static void demonstrateDateTimeFormatting(LocalDateTime dateTime, Locale locale, String label) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);
        System.out.printf("    %-18s : %s%n", label, dateTime.format(formatter));
    }

    private static void demonstrateMessageFormat(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        String pattern = bundle.getString("msg.order_placed_success");
        String formatted = MessageFormat.format(pattern, "Amit Sharma", "ORD-2026-9042");
        System.out.println("    [" + locale.getLanguage() + "] " + formatted);
    }

    private static void printSyllabusExplanation() {
        System.out.println("\n==========================================================================");
        System.out.println("   VIVA & SYLLABUS REFERENCE: INTERNATIONALIZATION (i18n) CONCEPTS        ");
        System.out.println("==========================================================================");
        System.out.println("Q: What is Internationalization (i18n) vs Localization (l10n)?");
        System.out.println("A: - i18n: Designing software so it can be adapted to various languages and");
        System.out.println("     regions without engineering changes (ResourceBundle, UTF-8, fmt tags).");
        System.out.println("   - l10n: The actual process of adapting software for a specific locale by");
        System.out.println("     translating text and configuring local currency, numbers, and dates.");
        System.out.println("Q: How does Java resolve fallback bundles when a key is missing in messages_hi?");
        System.out.println("A: Lookup order: messages_hi_IN -> messages_hi -> messages (base default).");
        System.out.println("Q: Which JSTL tag library handles i18n in JSP?");
        System.out.println("A: Jakarta JSTL Formatting taglib (<%@ taglib prefix='fmt' uri='jakarta.tags.fmt' %>)");
        System.out.println("   using <fmt:setLocale>, <fmt:setBundle>, and <fmt:message key='...'>.");
        System.out.println("==========================================================================");
    }
}
