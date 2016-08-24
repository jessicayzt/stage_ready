package metafire.stageready.model;

import java.io.Serializable;

/**
 * Created by Jessica on 6/28/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class EventData implements Serializable {

    private static final long serialVersionUID = 5335087359727482267L;
    private String name;
    private String type;
    private String hour;
    private String mins;
    private String amOrPm;
    private int year;
    private int month;
    private int day;
    private String time;
    private String location;
    private String notes;

    /**
     * Creates a new EventData with given parameters.
     * @param name the name of the event
     * @param type the type of event
     * @param year the year
     * @param month the month
     * @param day the mday
     * @param hour the hour in the form of a string
     * @param mins the minutes in the form of a string
     * @param amOrPm whether the event time is "AM" or "PM", represented as a string
     * @param time the time of the event represented as a string
     * @param location the location
     */

    public EventData(String name, String type, int year, int month, int day, String hour, String mins, String amOrPm, String time, String location) {
        this.name = name;
        this.hour = hour;
        this.mins = mins;
        this.amOrPm = amOrPm;
        this.year = year;
        this.month = month;
        this.day = day;
        this.time = time;
        this.location =location;
        this.type = type;
        notes = "";
    }

    /**
     * Returns the type.
     * @return the type
     */

    public String getType() {
        return type;
    }

    /**
     * Returns the location.
     * @return the location
     */

    public String getLocation() {
        return location;
    }

    /**
     * Returns the time.
     * @return the time
     */

    public String getTime() {
        return time;
    }

    /**
     * Returns the name.
     * @return the name
     */

    public String getName() {
        return name;
    }

    /**
     * Returns the notes.
     * @return the notes
     */

    public String getNotes() {
        return notes;
    }

    /**
     * Sets the notes associated with the event.
     * @param notes the notes to set
     */

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Returns the hour.
     * @return the hour
     */

    public String getHour() {
        return hour;
    }

    /**
     * Returns the minutes.
     * @return the minutes.
     */

    public String getMins() {
        return mins;
    }

    /**
     * Returns the year
     * @return the year
     */

    public int getYear() {
        return year;
    }

    /**
     * Returns the day.
     * @return the day
     */

    public int getDay() {
        return day;
    }

    /**
     * Returns the month.
     * @return the month
     */

    public int getMonth() {
        return month;
    }

    /**
     * Returns AM or PM.
     * @return returns "AM" or "PM" depending on whether the time of the event is in AM or PM
     */

    public String getAmOrPm() {
        return amOrPm;
    }
}