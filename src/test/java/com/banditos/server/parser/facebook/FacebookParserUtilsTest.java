package com.banditos.server.parser.facebook;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.Test;

public class FacebookParserUtilsTest {

    private static final String DATE_STRING_TODAY = "Today at 8 PM – 11 PM";
    private static final String DATE_STRING_TODAY_OVER_MIDNIGHT = "Today at 11 PM – 5 AM";
    private static final String DATE_STRING_TOMORROW = "Tomorrow at 8 PM – 11 PM";
    private static final String DATE_STRING_THURSDAY = "Thursday at 8 PM – 11 PM";
    private static final String DATE_STRING_WITH_DATE = "Friday, May 10, 2019 at 10 PM – 5 AM";
    // started yesterday at 00:05
    // Tuesday at 11 PM – 5 AM

    @Test
    public void testTodayDateParse() {
        LocalDateTime date = FacebookParserUtils.parseDate(DATE_STRING_TODAY);
        LocalDateTime expected = LocalDateTime
                .now()
                .truncatedTo(ChronoUnit.DAYS)
                .plus(20, ChronoUnit.HOURS);
        assertEquals(expected, date);
    }

    @Test
    public void testTomorrowDateParse() {
        LocalDateTime date = FacebookParserUtils.parseDate(DATE_STRING_TOMORROW);
        LocalDateTime expected = LocalDateTime
                .now()
                .truncatedTo(ChronoUnit.DAYS)
                .plus(1, ChronoUnit.DAYS)
                .plus(20, ChronoUnit.HOURS);
        assertEquals(expected, date);
    }


}