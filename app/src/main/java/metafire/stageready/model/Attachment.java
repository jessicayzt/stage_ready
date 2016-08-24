package metafire.stageready.model;

import java.io.Serializable;

/**
 * Created by Jessica on 6/26/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class Attachment implements Serializable {

    private static final long serialVersionUID = -5158691886635347666L;
    private String uri;
    private String notes;

    /**
     * Creates a new attachment with given Uri and an empty string for the notes.
     * @param uri the Uri associated with the attachment
     */

    public Attachment(String uri) {
        this.uri = uri;
        notes = "";
    }

    /**
     * Returns the attachment's notes.
     * @return the attachment's notes
     */

    public String getNotes() {
        return notes;
    }

    /**
     * Sets the notes to the given string.
     * @param notes the notes to set
     */

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Returns the Uri associated with the attachment.
     * @return the Uri
     */

    public String getUri() {
        return uri;
    }
}