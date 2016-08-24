package metafire.stageready.model;

import java.io.Serializable;

/**
 * Created by Jessica on 6/22/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

/**
 * Represents a track, imported or recorded.
 */

public class Track implements Serializable {

    private static final long serialVersionUID = -8241428817150616867L;
    private String uri;
    private String name;
    private String notes;

    /**
     * Constructs a new Track with the given Uri and name. Sets the notes to an empty string.
     * @param uri the Uri of the audio file of the track
     * @param name the name of the track
     */

    public Track(String uri, String name) {
        this.uri = uri;
        this.name = name;
        notes = "";
    }

    /**
     * Gets the track's Uri.
     * @return the track's Uri
     */

    public String getUri() {
        return uri;
    }

    /**
     * Gets the track's name.
     * @return the track's name
     */

    public String getName() {
        return name;
    }

    /**
     * Sets the track's name.
     * @param name the track's name
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the track's notes.
     * @return the track's notes
     */

    public String getNotes() {
        return notes;
    }

    /**
     * Sets the track's notes.
     * @param notes the notes to set
     */

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
