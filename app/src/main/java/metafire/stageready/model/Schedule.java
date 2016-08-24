package metafire.stageready.model;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import metafire.stageready.libs.compact_calendar_view.CompactCalendarView;
import metafire.stageready.libs.compact_calendar_view.Event;

/**
 * Created by Jessica on 6/29/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

// Singleton class

public class Schedule implements Serializable {

    private static final long serialVersionUID = 5042402698529015409L;
    private static String fileName = "Schedule.ser";
    private ArrayList<Event> events;
    private static Schedule instance;
    private transient Date selectedDate;
    private transient CompactCalendarView compactCalendarView;
    private transient boolean isFirstLaunch;
    private Event currentEvent;
    private boolean useThreeLetterAbbreviation;
    private boolean showMondayAsFirstDay;

    /**
     * Constructs a Schedule with useThreeLetterAbbreviation and showMondayAsFirstDay as false.
     */

    private Schedule(){
        events = new ArrayList<>();
        useThreeLetterAbbreviation = false;
        showMondayAsFirstDay = false;
    }

    /**
     * Returns the Schedule instance, or creates it if it does not exist.
     * @return the Schedule instance
     */

    public static Schedule getInstance() {
        if (instance == null) {
            instance = new Schedule();
        }
        return instance;
    }

    /**
     * Returns the events.
     * @return the events on the schedule
     */

    public ArrayList<Event> getEvents() {
        return events;
    }

    /**
     * Returns the first 5 upcoming events.
     * @return the first 5 upcoming events
     */

    public ArrayList<Event> getUpcomingEvents(){
        ArrayList<Event> upcomingEvents = new ArrayList<>();
        Date currentDate = new Date();

        for (Event event : events){
            if (event.getTimeInMillis() > currentDate.getTime()){
                upcomingEvents.add(event);
            }
        }

        Collections.sort(upcomingEvents);
        ArrayList<Event> upcomingEventsShort = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            if (positionExists(upcomingEvents, i)) {
                upcomingEventsShort.add(upcomingEvents.get(i));
            }
        }
        return upcomingEventsShort;
    }

    /**
     * Returns the selected date.
     * @return the selected date
     */

    public Date getSelectedDate() {
        return selectedDate;
    }

    /**
     * Sets the selected date.
     * @param selectedDate the date to select
     */

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    /**
     * Returns the CompactCalendarView
     * @return the CompactCalendarView associated with the schedule
     */

    public CompactCalendarView getCompactCalendarView() {
        return compactCalendarView;
    }

    /**
     * Sets the schedule's CompactCalendarView
     * @param compactCalendarView the CompactCalendarView to set
     */

    public void setCompactCalendarView(CompactCalendarView compactCalendarView) {
        this.compactCalendarView = compactCalendarView;
    }

    /**
     * Serializes the Schedule instance to private app data.
     * @param context the context
     */

    public static void saveToFile(Context context){
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(instance);
            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch (IOException e) {
            // empty catch block
        }
    }

    /**
     * Loads a schedule to the current instance.
     * @param schedule the schedule to load as the instance
     */

    public void loadSchedule(Schedule schedule){
        events = schedule.getEvents();
        useThreeLetterAbbreviation = schedule.getUseThreeLetterAbbreviation();
        showMondayAsFirstDay = schedule.getShowMondayAsFirstDay();
        setIsFirstLaunch(true);
    }

    /**
     * Checks if it is the first time launching and loading the application.
     * @return true if it is the first launch, false otherwise
     */

    public boolean isFirstLaunch() {
        return isFirstLaunch;
    }

    /**
     * Sets if it is the first time launching and loading the application.
     * @param isFirstLaunch whether a full load is required
     */

    public void setIsFirstLaunch(boolean isFirstLaunch) {
        this.isFirstLaunch = isFirstLaunch;
    }

    /**
     * Sets the current event.
     * @param currentEvent the current event to set
     */

    public void setCurrentEvent(Event currentEvent){
        this.currentEvent = currentEvent;
    }
    /**
     * Gets the current event.
     * @return uri the Uri associated with the attachment
     */

    public Event getCurrentEvent(){
        return currentEvent;
    }

    /**
     * If Monday instead of Sunday should be shown as the first day on the calendar.
     * @return true if Monday should be shown as first day, false otherwise
     */

    public boolean getShowMondayAsFirstDay() {
        return showMondayAsFirstDay;
    }

    /**
     * Sets if Monday should be shown as the first day on the calendar.
     * @param showMondayAsFirstDay true if the Monday should be shown as the first day, false
     * otherwise
     */

    public void setShowMondayAsFirstDay(boolean showMondayAsFirstDay) {
        this.showMondayAsFirstDay = showMondayAsFirstDay;
    }

    /**
     * If the three letter abbreviation should be used to display the week names on the calendar.
     * @return true if the three letter abbreviation should be used, false otherwise
     */

    public boolean getUseThreeLetterAbbreviation() {
        return useThreeLetterAbbreviation;
    }

    /**
     * Sets if the three letter abbreviation should be used to display the week names on the
     * calendar.
     * @param useThreeLetterAbbreviation true if the three letter abbreviation should be used,
     * false otherwise
     */

    public void setUseThreeLetterAbbreviation(boolean useThreeLetterAbbreviation) {
        this.useThreeLetterAbbreviation = useThreeLetterAbbreviation;
    }

    /**
     * Checks if the given position exists.
     * @param position the position to check
     * @return true if the position exists, false otherwise
     */

    private static boolean positionExists(ArrayList<Event> upcomingEvents, int position) {
        try {
            upcomingEvents.get(position);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
}