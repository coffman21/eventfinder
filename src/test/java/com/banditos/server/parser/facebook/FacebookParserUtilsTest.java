package com.banditos.server.parser.facebook;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import org.junit.Ignore;
import org.junit.Test;

public class FacebookParserUtilsTest {

    private static final String DATE_STRING_TODAY = "Today at 8 PM – 11 PM";
    private static final String DATE_STRING_TODAY_OVER_MIDNIGHT = "Today at 11 PM – 5 AM";
    private static final String DATE_STRING_TOMORROW = "Tomorrow at 8 PM – 11 PM";
    private static final String DATE_STRING_THURSDAY = "Thursday at 8 PM – 11 PM";
    private static final String DATE_STRING_WITH_DATE = "Friday, May 10, 2019 at 10 PM – 5 AM";
    private static final String DATE_STRING_WITH_TWO_AT = "May 10 at 10:59 PM – May 11 at 12 PM";
    // started yesterday at 00:05
    // Tuesday at 11 PM – 5 AM

    @Test
    public void testTodayDateParse() {
        LocalDateTime date = FacebookParserUtils.parseBeginDate(DATE_STRING_TODAY);
        LocalDateTime expected = LocalDateTime
                .now()
                .truncatedTo(ChronoUnit.DAYS)
                .plus(20, ChronoUnit.HOURS);
        assertEquals(expected, date);
    }

    @Test
    public void testTomorrowDateParse() {
        LocalDateTime date = FacebookParserUtils.parseBeginDate(DATE_STRING_TOMORROW);
        LocalDateTime expected = LocalDateTime
                .now()
                .truncatedTo(ChronoUnit.DAYS)
                .plus(1, ChronoUnit.DAYS)
                .plus(20, ChronoUnit.HOURS);
        assertEquals(expected, date);
    }

    @Test
    public void testThursdayDateParse() {
        LocalDateTime date = FacebookParserUtils.parseBeginDate(DATE_STRING_THURSDAY);
        LocalDateTime expected = LocalDateTime
                .now()
                .with(DayOfWeek.THURSDAY)
                .truncatedTo(ChronoUnit.DAYS)
                .plus(20, ChronoUnit.HOURS);
        assertEquals(expected, date);
    }

    @Test
    public void testTodayOverMidnightDateParse() {
        LocalDateTime date = FacebookParserUtils.parseBeginDate(DATE_STRING_TODAY_OVER_MIDNIGHT);
        LocalDateTime expected = LocalDateTime
                .now()
                .truncatedTo(ChronoUnit.DAYS)
                .minus(1, ChronoUnit.HOURS);
        assertEquals(expected, date);
    }

    @Test
    public void testDateWithDateParse() {
        LocalDateTime date = FacebookParserUtils.parseBeginDate(DATE_STRING_WITH_DATE);
        LocalDateTime expected = LocalDateTime.of(2019, 5, 10, 22, 0, 0);
        assertEquals(expected, date);
    }

    @Test
    @Ignore
    public void testDateWithTwoAtParts() {
        // May 10 at 10:59 PM – May 11 at 12 PM
        LocalDateTime date = FacebookParserUtils.parseBeginDate(DATE_STRING_WITH_TWO_AT);
        LocalDateTime expected = LocalDateTime
                .of(2019, 5, 10, 22, 59, 0)
                .with(ChronoField.YEAR, Instant.now().get(ChronoField.YEAR));
        assertEquals(expected, date);
    }

    @Test
    public void testDateTimeFormatter() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        String datePart = DATE_STRING_WITH_DATE.split(" at ")[0];
        LocalDateTime expected = LocalDateTime.of(2019, 5, 10, 0, 0, 0);
        LocalDateTime parsed = LocalDate
                .parse(datePart, formatter)
                .atTime(0, 0);
        assertEquals(expected, parsed);
    }
}