package com.banditos.server.parser.facebook;

import static org.junit.Assert.assertEquals;

import com.banditos.server.model.pojo.EventDate;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import org.junit.Test;

public class FacebookParserUtilsTest {

    private static final String DATE_STRING_TODAY = "Today at 8 PM – 11 PM";
    private static final String DATE_STRING_WITH_TWO_AT = "May 10 at 10:59 PM – May 11 at 12 PM";
    // started yesterday
    private static final String DATE_STRING_TODAY_OVER_MIDNIGHT = "Today at 11 PM – 5 AM";
    private static final String DATE_STRING_TOMORROW = "Tomorrow at 8 PM – 11 PM";
    private static final String DATE_STRING_THURSDAY = "Thursday at 8 PM – 11 PM";
    private static final String DATE_STRING_WITH_DATE = "Friday, May 10, 2019 at 10 PM – 5 AM";

    @Test
    public void testTodayDateParse() {
        EventDate date = FacebookParserUtils.parseEventDate(DATE_STRING_TODAY);
        LocalDateTime expectedBegin = LocalDateTime
                .now()
                .truncatedTo(ChronoUnit.DAYS)
                .plus(20, ChronoUnit.HOURS);
        LocalDateTime expectedEnd = LocalDateTime
                .now()
                .truncatedTo(ChronoUnit.DAYS)
                .plus(23, ChronoUnit.HOURS);

        assertEquals(expectedBegin, date.getBeginDate());
        assertEquals(expectedEnd, date.getEndDate());
    }

    @Test
    public void testTomorrowDateParse() {
        EventDate date = FacebookParserUtils.parseEventDate(DATE_STRING_TOMORROW);
        LocalDateTime expectedBegin = LocalDateTime
                .now()
                .truncatedTo(ChronoUnit.DAYS)
                .plus(1, ChronoUnit.DAYS)
                .plus(20, ChronoUnit.HOURS);
        LocalDateTime expectedEnd = LocalDateTime
                .now()
                .truncatedTo(ChronoUnit.DAYS)
                .plus(1, ChronoUnit.DAYS)
                .plus(23, ChronoUnit.HOURS);

        assertEquals(expectedBegin, date.getBeginDate());
        assertEquals(expectedEnd, date.getEndDate());
    }

    @Test
    public void testThursdayDateParse() {
        EventDate date = FacebookParserUtils.parseEventDate(DATE_STRING_THURSDAY);
        LocalDateTime expectedBegin = LocalDateTime
                .now()
                .with(DayOfWeek.THURSDAY)
                .truncatedTo(ChronoUnit.DAYS)
                .plus(20, ChronoUnit.HOURS);
        LocalDateTime expectedEnd = LocalDateTime
                .now()
                .with(DayOfWeek.THURSDAY)
                .truncatedTo(ChronoUnit.DAYS)
                .plus(23, ChronoUnit.HOURS);

        assertEquals(expectedBegin, date.getBeginDate());
        assertEquals(expectedEnd, date.getEndDate());
    }

    @Test
    public void testTodayOverMidnightDateParse() {
        EventDate date = FacebookParserUtils.parseEventDate(DATE_STRING_TODAY_OVER_MIDNIGHT);
        LocalDateTime expectedBegin = LocalDateTime
                .now()
                .truncatedTo(ChronoUnit.DAYS)
                .plus(1, ChronoUnit.DAYS)
                .minus(1, ChronoUnit.HOURS);
        LocalDateTime expectedEnd = LocalDateTime
                .now()
                .truncatedTo(ChronoUnit.DAYS)
                .plus(1, ChronoUnit.DAYS)
                .plus(5, ChronoUnit.HOURS);
        assertEquals(expectedBegin, date.getBeginDate());
        assertEquals(expectedEnd, date.getEndDate());
    }

    @Test
    public void testDateWithDateParse() {
        EventDate date = FacebookParserUtils.parseEventDate(DATE_STRING_WITH_DATE);
        LocalDateTime expectedBegin = LocalDateTime.of(2019, 5, 10, 22, 0, 0);
        LocalDateTime expectedEnd = LocalDateTime.of(2019, 5, 11, 5, 0, 0);
        assertEquals(expectedBegin, date.getBeginDate());
        assertEquals(expectedEnd, date.getEndDate());
    }

    @Test
    public void testDateWithTwoAtParts() {
        // May 10 at 10:59 PM – May 11 at 12 PM
        EventDate date = FacebookParserUtils.parseEventDate(DATE_STRING_WITH_TWO_AT);
        LocalDateTime expectedBegin = LocalDateTime
                .of(2019, 5, 10, 22, 59, 0)
                .with(ChronoField.YEAR, LocalDate.now().get(ChronoField.YEAR));
        LocalDateTime expectedEnd = LocalDateTime
                .of(2019, 5, 11, 12, 00, 0)
                .with(ChronoField.YEAR, LocalDate.now().get(ChronoField.YEAR));
        assertEquals(expectedBegin, date.getBeginDate());
        assertEquals(expectedEnd, date.getEndDate());
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

    @Test
    public void testTimeDTMFormatter() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        LocalTime expected = LocalTime.of(22, 59);

        String time = "10:59 PM";
        LocalTime parsed = LocalTime
                .parse(time, formatter);
        assertEquals(expected, parsed);
    }

    @Test
    public void testTimeWoutMinsDTMFormatter() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[h[:m] a]");
        LocalTime expected = LocalTime.of(20, 0);

        String time = "8 PM";
        LocalTime parsed = LocalTime
                .parse(time, formatter);
        assertEquals(expected, parsed);
    }
}