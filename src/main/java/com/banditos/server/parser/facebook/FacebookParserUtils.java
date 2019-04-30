package com.banditos.server.parser.facebook;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacebookParserUtils {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(FacebookParserUtils.class);

    private static final String TODAY = "TODAY";
    private static final String TOMORROW = "TOMORROW";
    private static final String AT_SEPARATOR = " AT ";
    private static final String TIME_SEPARATOR = " – ";
    private static final String PM = "PM";

    static LocalDateTime parseDate(String eventDateString) {
        eventDateString = eventDateString.toUpperCase();
        LocalDateTime date;

        String[] split = eventDateString.split(AT_SEPARATOR);
        if (split.length == 2) {
            String datePart = split[0];
            String timePart = split[1];

            if (datePart.equals(TODAY)) {
                date = LocalDateTime
                        .now()
                        .truncatedTo(ChronoUnit.DAYS)
                        .plus(amountOfHours(timePart
                                .split(TIME_SEPARATOR)[0]), ChronoUnit.HOURS);
                return date;
            } else if (datePart.equals(TOMORROW)) {
                date = LocalDateTime
                        .now()
                        .truncatedTo(ChronoUnit.DAYS)
                        .plus(1, ChronoUnit.DAYS)
                        .plus(amountOfHours(timePart
                                .split(TIME_SEPARATOR)[0]), ChronoUnit.HOURS);
                return date;
            }

        } else {
            LOGGER.error("Cannot split date by ' at ' part : {}",
                    eventDateString);
        }
        return null;
    }

    private static int amountOfHours(String timeStr) {
        String[] split = timeStr.split(" ");
        if (split.length == 2) {
            int time = Integer.valueOf(split[0]);
            String period = split[1];
            if (period.equals(PM)) {
                time += 12;
            }
            return time;
        } else {
            LOGGER.error("Cannot split time by space: {}", timeStr);
        }
        return 0;
    }
}
