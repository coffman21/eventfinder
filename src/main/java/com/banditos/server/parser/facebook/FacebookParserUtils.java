package com.banditos.server.parser.facebook;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FacebookParserUtils {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(FacebookParserUtils.class);

    private static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
    private static final DateTimeFormatter FORMATTER_DAY_OF_WEEK = DateTimeFormatter.ofPattern("EEEE");

    private static final String TODAY = "Today";
    private static final String TOMORROW = "Tomorrow";
    private static final String AT_SEPARATOR = " at ";
    private static final String END_TIME_SEPARATOR = " – ";
    private static final String PM = "PM";

    static LocalDateTime parseBeginDate(String eventDateString) {
//        eventDateString = eventDateString.toUpperCase();
        LocalDateTime date;

        String[] split = eventDateString.split(AT_SEPARATOR);
        if (split.length == 2) {
            String datePart = split[0];
            String timePart = split[1];
            String beginTime = timePart
                    .split(END_TIME_SEPARATOR)[0];
            String endTime = timePart
                    .split(END_TIME_SEPARATOR)[1];

            if (datePart.equals(TODAY)) {
                if (endTime.contains(PM)) {
                    // Today at 8 PM – 11 PM
                    date = LocalDateTime
                            .now()
                            .truncatedTo(ChronoUnit.DAYS)
                            .plus(amountOfHours(beginTime), ChronoUnit.HOURS);
                } else {
                    // Today at 11 PM – 5 AM
                    date = LocalDateTime
                            .now()
                            .truncatedTo(ChronoUnit.DAYS)
                            .minus(1, ChronoUnit.DAYS)
                            .plus(amountOfHours(beginTime), ChronoUnit.HOURS);
                }
                // todo: events that ends at the next day after midday will lead to wrong results
                return date;
                
            } else if (datePart.equals(TOMORROW)) {
                // Tomorrow at 8 PM – 11 PM
                date = LocalDateTime
                        .now()
                        .truncatedTo(ChronoUnit.DAYS)
                        .plus(1, ChronoUnit.DAYS)
                        .plus(amountOfHours(beginTime), ChronoUnit.HOURS);
                return date;
            } else {
                if (datePart.contains(",")) {
                    // next week or later
                    // Friday, May 10, 2019 at 10 PM – 5 AM
                    try {
                        return LocalDate
                                .parse(datePart, FORMATTER_DATE)
                                .atTime(amountOfHours(beginTime), 0);
                    } catch (DateTimeParseException e) {
                        LOGGER.error("Cannot parse date : {}", datePart, e);
                    }
                } else  {
                    // this week - a day after tomorrow or even yesterday
                    // Thursday at 8 PM – 11 PM
                    try {
                        return LocalDateTime
                                .now()
                                .truncatedTo(ChronoUnit.DAYS)
                                .with(DayOfWeek
                                        .from(FORMATTER_DAY_OF_WEEK
                                                .parse(datePart)))
                                .plus(amountOfHours(beginTime), ChronoUnit.HOURS);
                    } catch (DateTimeParseException e) {
                        LOGGER.error("Cannot parse date : {}", datePart, e);
                    }
                }
            }

        } else {
            LOGGER.error("Cannot split datetime by ' at ' part : {}",
                    eventDateString);
        }
        return null;
    }

    static LocalDateTime parseEndDate() {
        throw new UnsupportedOperationException("not implemented");
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
            LOGGER.error("Cannot split time by ' ' : {}", timeStr);
        }
        return 0;
    }
}
