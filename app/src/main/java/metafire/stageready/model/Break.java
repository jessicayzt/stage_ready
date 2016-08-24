package metafire.stageready.model;

import java.io.Serializable;

/**
 * Created by Jessica on 5/25/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class Break extends Slot implements Serializable {

    private static final long serialVersionUID = -8415402867154019003L;
    private String name;
    private String notes;

    /**
     * Creates a new attachment with given name, minutes, seconds, and with the empty string for
     * the notes.
     * @param minutes the minutes
     * @param seconds the seconds
     */

    public Break(int minutes, int seconds) {
        super(minutes, seconds);
        this.name = "B R E A K";
        notes = "";
    }

    /**
     * Gets the title of the break to be used in its dialog.
     * @return the title
     */

    public String getTitle() {
        String minutesString = String.valueOf(getMinutes());
        String secondsString = String.valueOf(getSeconds());

        if (minutesString.length() == 1){
            minutesString = '0' + minutesString;
        }

        if (getSeconds() < 10) {
            secondsString = '0' + secondsString;
        }

        return minutesString + ':' + secondsString + " - " + name;
    }

    /**
     * Gets the notes associated with the break.
     * @return the notes
     */

    public String getNotes() {
        return notes;
    }

    /**
     * Sets the notes associated with the break.
     * @param notes the notes to set
     */

    public void setNotes(String notes) {
        this.notes = notes;
    }
}