package metafire.stageready.libs.compact_calendar_view;

import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * @author  SundeepK @ <https://github.com/SundeepK/CompactCalendarView> modified by Jessica Yang
 * <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class Event implements Serializable, Comparable<Event> {

    private static final long serialVersionUID = -6824392074813625784L;
    private int color;
    private long timeInMillis;
    private Object data;

    public Event(int color, long timeInMillis, Object data) {
        this.color = color;
        this.timeInMillis = timeInMillis;
        this.data = data;
    }

    public int getColor() {
        return color;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    @Nullable
    public Object getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (color != event.color) return false;
        if (timeInMillis != event.timeInMillis) return false;
        return !(data != null ? !data.equals(event.data) : event.data != null);
    }

    @Override
    public int hashCode() {
        int result = color;
        result = 31 * result + (int) (timeInMillis ^ (timeInMillis >>> 32));
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EventData{" +
                "color=" + color +
                ", timeInMillis=" + timeInMillis +
                ", data=" + data +
                '}';
    }

    @Override
    public int compareTo(Event event) {
        return Long.compare(this.getTimeInMillis(), event.getTimeInMillis());
    }
}