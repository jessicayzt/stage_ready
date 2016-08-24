package metafire.stageready.libs.compact_calendar_view;

import java.io.Serializable;
import java.util.List;

/**
 * @author  SundeepK @ <https://github.com/SundeepK/CompactCalendarView> modified by Jessica Yang
 * <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

class Events implements Serializable {

    private static final long serialVersionUID = -6455498171160896404L;
    private final List<Event> events;
    private final long timeInMillis;

    Events(long timeInMillis, List<Event> events) {
        this.timeInMillis = timeInMillis;
        this.events = events;
    }

    long getTimeInMillis() {
        return timeInMillis;
    }

    List<Event> getEvents() {
        return events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Events event = (Events) o;

        if (timeInMillis != event.timeInMillis) return false;
        return events != null ? events.equals(event.events) : event.events == null;

    }

    @Override
    public int hashCode() {
        int result = events != null ? events.hashCode() : 0;
        result = 31 * result + (int) (timeInMillis ^ (timeInMillis >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "SchedEvents{" +
                "events=" + events +
                ", timeInMillis=" + timeInMillis +
                '}';
    }
}