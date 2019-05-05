package com.banditos.server.parser.facebook;

import com.banditos.server.model.pojo.EventDate;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.UnsupportedTemporalTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FacebookParserUtils {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(FacebookParserUtils.class);

    private static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter
            .ofPattern("[[EEEE][, ][MMMM d[, yyyy]]]");
    private static final DateTimeFormatter FORMATTER_TIME = DateTimeFormatter
            .ofPattern("[h[:mm] a]");

    private static final String TODAY = "Today";
    private static final String TOMORROW = "Tomorrow";
    private static final String AT_SEPARATOR = " at ";
    private static final String DASH_SEPARATOR = " – ";
    private static final String PM = "PM";
    private static final String AM = "AM";

    static EventDate parseEventDate(String eventDateString) {

        String[] split = eventDateString.split(DASH_SEPARATOR);
        if (split.length == 2) {
            // May 10 at 10:59 PM
            String beginPart = split[0];
            String endPart = split[1];
            String beginDatePart = beginPart
                    .split(AT_SEPARATOR)[0];
            String beginTimePart = beginPart
                    .split(AT_SEPARATOR)[1];
            LocalDateTime beginDate = parseDate(beginDatePart, beginTimePart);

            LocalDateTime endDate;
            if (endPart.contains(AT_SEPARATOR)) {
                // May 10 at 10:59 PM – May 11 at 12 PM
                String endDatePart = endPart
                        .split(AT_SEPARATOR)[0];
                String endTimePart = endPart
                        .split(AT_SEPARATOR)[1];
                endDate = parseDate(endDatePart, endTimePart);
            } else {
                // Friday, May 10, 2019 at 10 PM – 5 AM
                endDate = parseDate(beginDatePart, endPart);
                if (endPart.contains(AM)) {
                    endDate = endDate.plus(1, ChronoUnit.DAYS);
                }
            }

            return new EventDate()
                    .setBeginDate(beginDate)
                    .setEndDate(endDate);
        } else {
            LOGGER.error("Cannot split datetime by {} part : {}",
                    DASH_SEPARATOR, eventDateString);
        }
        return new EventDate();
    }

    private static LocalDateTime parseDate(String datePart, String timePart) {
        LocalDateTime date = null;
        if (datePart.equals(TODAY)) {
            // Today at 8 PM – 11 PM

            if (getTime(timePart).isAfter(LocalTime.now())) {
                date = LocalDateTime
                        .now()
                        .truncatedTo(ChronoUnit.DAYS)
                        .with(getTime(timePart));

            } else {
                // now() is later than event date: event already started
                // Today at 11 PM – 5 AM
                date = LocalDateTime
                        .now()
                        .truncatedTo(ChronoUnit.DAYS)
                        .minus(1, ChronoUnit.DAYS)
                        .with(getTime(timePart));
            }

        } else if (datePart.equals(TOMORROW)) {
            // Tomorrow at 8 PM – 11 PM
            date = LocalDateTime
                    .now()
                    .truncatedTo(ChronoUnit.DAYS)
                    .plus(1, ChronoUnit.DAYS)
                    .with(getTime(timePart));
        } else {
            if (datePart.contains(",")) {
                // next week or later
                // Friday, May 10, 2019 at 10 PM – 5 AM
                try {
                    date = LocalDate
                            .parse(datePart, FORMATTER_DATE)
                            .atTime(getTime(timePart));
                } catch (DateTimeParseException e) {
                    LOGGER.error("Cannot parse date : {}", datePart, e);
                }
            } else {
                // this week - a day after tomorrow or even yesterday
                // Thursday at 8 PM – 11 PM
                try {
                    TemporalAccessor parse = FORMATTER_DATE.parse(datePart);
                    DayOfWeek dayOfWeek;
                    if (parse.isSupported(ChronoField.DAY_OF_WEEK)) {
                        dayOfWeek = DayOfWeek.from(parse);
                        date = LocalDateTime
                                .now()
                                .truncatedTo(ChronoUnit.DAYS)
                                .with(dayOfWeek)
                                .with(getTime(timePart));
                    } else {
                        date = LocalDateTime
                                .now()
                                .with(ChronoField.MONTH_OF_YEAR, parse.get(ChronoField.MONTH_OF_YEAR))
                                .with(ChronoField.DAY_OF_MONTH, parse.get(ChronoField.DAY_OF_MONTH))
                                .with(getTime(timePart));
                    }

                } catch (DateTimeParseException | UnsupportedTemporalTypeException e) {
                    LOGGER.error("Cannot parse date : {}", datePart, e);
                }
            }
        }
        return date;
    }

    private static LocalTime getTime(String timeStr) {
        try {
            return LocalTime.parse(timeStr, FORMATTER_TIME);
        } catch (DateTimeParseException e) {
            LOGGER.error("Cannot parse time: {}", timeStr, e);
        }
        return LocalTime.of(0, 0);
    }
}
