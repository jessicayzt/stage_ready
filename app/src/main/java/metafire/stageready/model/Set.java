package metafire.stageready.model;

import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import metafire.stageready.fragments.SetFragment;

/**
 * Created by Jessica on 5/24/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

/**
 * Represents a set in the SetManager.
 */

public class Set implements Serializable {

    private ArrayList<Slot> slots;
    private ArrayList<String> titles;
    private transient Slot currentlyModifying;
    private transient SetFragment setFragment;
    private transient TextView statsTextView;
    private String name;
    private static final long serialVersionUID = 4542294376324399803L;

    /**
     * Constructs a new Set.
     */

    public Set() {
        slots = new ArrayList<>();
        titles = new ArrayList<>();
    }

    /**
     * Adds a song to the set.
     * @param song the song to add
     */

    public void addSong(Song song) {
        slots.add(song);
    }

    /**
     * Adds a break to the set.
     * @param breakToAdd the break to add
     */

    public void addBreak(Break breakToAdd) {
        slots.add(breakToAdd);
    }

    /**
     * Returns the number of hours in the set.
     * @return the number of hours in the set
     */

    private int getSetHours() {
        double numberOfHoursUnrounded = getSetMinutesToHours() / 60;

        Double numberOfHours = Math.floor(numberOfHoursUnrounded);

        return numberOfHours.intValue();
    }

    /**
     * Returns the number of minutes in the set.
     * @return the number of minutes in the set
     */

    private int getSetMinutes() {
        return getSetMinutesToHours() % 60;
    }

    /**
     * Returns the number of total minutes in the set.
     * @return the number of total minutes in the set
     */


    private int getSetMinutesToHours() {
        int minutes = 0;

        for (Slot slot : slots) {
            minutes += slot.getMinutes();
        }
        double numberOfMinutesUnrounded = getSetSecondsToMinutes() / 60;

        Double numberOfMinutes = Math.floor(numberOfMinutesUnrounded);

        int numberOfMinutesInt = numberOfMinutes.intValue();

        return minutes + numberOfMinutesInt;
    }

    /**
     * Returns the number of total seconds in the set.
     * @return the number of total seconds in the set
     */

    private int getSetSecondsToMinutes() {
        int seconds = 0;

        for (Slot slot : slots) {
            seconds += slot.getSeconds();
        }
        return seconds;
    }

    /**
     * Returns the number of seconds in the set.
     * @return the number of seconds in the set
     */

    private int getSetSeconds() {
        return getSetSecondsToMinutes() % 60;
    }

    /**
     * Returns the slots in the set.
     * @return the slots in the set
     */

    public ArrayList<Slot> getSlots() {
        return slots;
    }

    /**
     * Clears all slots and titles associated with the set.
     */

    public void clearAll() {
        slots.clear();
        titles.clear();
    }

    /**
     * Returns the total time in the set.
     * @return the total length of the set as a string
     */

    public String getTotalTime() {
        String setHours = String.valueOf(getSetHours());
        String setMins = String.valueOf(getSetMinutes());
        String setSecs = String.valueOf(getSetSeconds());

        if (getSetHours() == 0 && getSetSeconds() == 0 && getSetMinutes() == 0) {
            return "00:00";
        }

        if (setSecs.length() == 1) {
            setSecs = '0' + setSecs;
        }

        if (getSetHours() == 0) {
            if (setMins.length() == 1){
                return '0' + setMins + ':' + setSecs;
            }
            else {
                return setMins + ':' + setSecs;
            }
        } else if (setHours.length() == 1 && setMins.length() == 1) {
            return '0' + setHours + ":0" + setMins + ':' + setSecs;
        } else if (setHours.length() == 1 && setMins.length() == 2) {
            return '0' + setHours + ':' + setMins + ':' + setSecs;
        } else if (setHours.length() == 2 && setMins.length() == 1) {
            return setHours + ":0" + setMins + ':' + setSecs;
        } else {
            return setHours + ':' + setMins + ':' + setSecs;
        }
    }

    /**
     * Returns the list of titles associated with the set.
     * @return the list of titles
     */

    public ArrayList<String> getTitles() {
        return titles;
    }

    /**
     * Returns a list of songs in the set.
     * @return the list of songs in the set
     */

    public ArrayList<Song> getSongs() {
        ArrayList<Song> songs = new ArrayList<>();
        for (Slot slot : slots) {
            if (slot.getClass() == Song.class) {
                songs.add((Song) slot);
            }
        }
        return songs;
    }

    /**
     * Returns a list of breaks in the set.
     * @return the number of total minutes in the set
     */

    private ArrayList<Break> getBreaks() {
        ArrayList<Break> breaks = new ArrayList<>();
        for (Slot slot : slots) {
            if (slot.getClass() == Break.class) {
                breaks.add((Break) slot);
            }
        }
        return breaks;
    }

    /**
     * Returns the slot that is currently being modified.
     * @return the slot that is currently being modified
     */

    public Slot getCurrentlyModifying() {
        return currentlyModifying;
    }

    /**
     * Sets the slot that is currently being modified.
     * @param slot the slot to set as the slot that is being currently modified
     */

    public void setCurrentlyModifying(Slot slot) {
        currentlyModifying = slot;
    }

    /**
     * Associates a set fragment with the set.
     * @param setFragment the set fragment to associate with the set
     */

    public void setSetFragment(SetFragment setFragment) {
        this.setFragment = setFragment;
    }

    /**
     * Gets the set fragment associated with the set.
     * @return the set fragment associated with the set
     */

    public SetFragment getSetFragment() {
        return setFragment;
    }

    /**
     * Associates a TextView to display the stats to the set.
     * @param statsTextView the TextView to associate with the set.
     */

    public void setStatsTextView(TextView statsTextView){
        this.statsTextView = statsTextView;
    }

    /**
     * Updates the stats TextView based on the current number of songs and breaks and the total set
     * time.
     */

    public void updateStatsTextView() {
        // Hardcoded strings as resources cannot be used due to a lack of a context. Passing around
        // context to numerous set objects can cause memory leak
        if (getSongs().size() == 1 && getBreaks().size() == 1) {
            statsTextView.setText("1 Song, 1 Break, Total Time: " + String.valueOf(getTotalTime()));
        } else if (getSongs().size() == 1) {
            statsTextView.setText("1 Song, " +
                    String.valueOf(getBreaks().size()) + " Breaks, Total Time: " + String.valueOf(getTotalTime()));
        } else if (getBreaks().size() == 1) {
            statsTextView.setText(String.valueOf(getSongs().size()) + " Songs, 1 Break, Total Time: " + String.valueOf(getTotalTime()));
        } else {
            statsTextView.setText(String.valueOf(getSongs().size()) + " Songs, " +
                    String.valueOf(getBreaks().size()) + " Breaks, Total Time: " + String.valueOf(getTotalTime()));
        }
    }

    /**
     * Sets the name of the set.
     * @param name the name of the set to give the set
     */

    public void setName(String name){
        this.name = name;
    }

    /**
     * Returns the name of the set.
     * @return the name of the set
     */

    public String getName(){
        return name;
    }
}
