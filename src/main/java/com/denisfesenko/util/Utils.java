package com.denisfesenko.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static String escapeTgString(String s) {
        return s.replace(".", "\\.").replace("(", "\\(").replace(")", "\\)")
                .replace("-", "\\-").replace(">", "\\>").replace("<", "\\<")
                .replace("&quot;", "\"");
    }

    public static String getCurrentBelgradeDateTimeAsString() {
        // Create a LocalDateTime in UTC
        LocalDateTime utcLocalDateTime = LocalDateTime.now();

        // Convert the LocalDateTime to a ZonedDateTime in UTC
        ZoneId utcZoneId = ZoneId.of("UTC");
        ZonedDateTime utcZonedDateTime = utcLocalDateTime.atZone(utcZoneId);

        // Change the timezone to Belgrade
        ZoneId belgradeZoneId = ZoneId.of("Europe/Belgrade");
        ZonedDateTime belgradeZonedDateTime = utcZonedDateTime.withZoneSameInstant(belgradeZoneId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd\\.MM\\.yyyy HH:mm");
        return belgradeZonedDateTime.format(formatter);
    }

    private Utils() {
    }
}
