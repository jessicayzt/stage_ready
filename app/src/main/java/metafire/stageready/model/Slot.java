package metafire.stageready.model;

import java.io.Serializable;

/**
 * Created by Jessica on 6/1/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

/**
 * Represents a slot in a set.
 */

public class Slot implements Serializable {

    private static final long serialVersionUID = -7410365269196827116L;
    private int minutes;
    private int seconds;

    /**
     * Constructs a new Slot.
     * @param minutes the minutes
     * @param seconds the seconds
     */

    public Slot(int minutes, int seconds){
        this.minutes = minutes;
        this.seconds = seconds;
    }

    /**
     * Gets a slot's minutes.
     * @return the number of minutes.
     */

    public int getMinutes() {
        return minutes;
    }

    /**
     * Sets a slot's minutes.
     * @param minutes the number of minutes.
     */

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    /**
     * Gets a slot's seconds
     * @return the number of seconds.
     */

    public int getSeconds() {
        return seconds;
    }

    /**
     * Sets a slot's seconds
     * @param seconds the number of minutes.
     */

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

}
