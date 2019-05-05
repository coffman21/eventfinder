package com.banditos.server.model.pojo;

import java.time.LocalDateTime;
import java.util.Objects;

public class EventDate {

    private LocalDateTime beginDate;

    private LocalDateTime endDate;

    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    public EventDate setBeginDate(LocalDateTime beginDate) {
        this.beginDate = beginDate;
        return this;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public EventDate setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventDate eventDate = (EventDate) o;
        return Objects.equals(beginDate, eventDate.beginDate) &&
                Objects.equals(endDate, eventDate.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beginDate, endDate);
    }

    @Override
    public String toString() {
        return "EventDate{" +
                "beginDate=" + beginDate +
                ", endDate=" + endDate +
                '}';
    }
}
