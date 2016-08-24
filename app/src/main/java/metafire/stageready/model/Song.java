package metafire.stageready.model;

import java.io.Serializable;
import java.util.ArrayList;

import metafire.stageready.fragments.TracksFragment;

/**
 * Created by Jessica on 5/24/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

/**
 * Represents a song in a set.
 */

public class Song extends Slot implements Serializable {

    private static final long serialVersionUID = -7951699016941987224L;
    private String name;
    private String lyrics;
    private int bpm;
    private String key;
    private ArrayList<Attachment> attachments;
    private ArrayList<Track> tracks;
    private transient int currentAttachmentPosition;
    private TracksFragment tracksFragment;
    private transient int currentTrackPosition;
    private String notes;

    /**
     * Constructs a new Song with the given name, minutes, and seconds. Sets lyrics and notes to
     * the empty string, and the BPM to 0 as a way to identify that it has not been initiated.
     * @param name the name of the song
     * @param minutes the number of minutes
     * @param seconds the number of seconds
     */

    public Song(String name, int minutes, int seconds) {
        super(minutes, seconds);
        this.name = name;
        lyrics = "";
        bpm = 0;
        key = "";
        attachments = new ArrayList<>();
        tracks = new ArrayList<>();
        // due to the nature of this field and how ints are
        // instantiated as 0, setting currentTrackPosition to
        // 100 will ensure the highlighting displays correctly on the UI
        currentTrackPosition = 100;
        notes = "";
    }

    /**
     * Gets the name of the song.
     * @return the name of the song
     */

    public String getName() {
        return name;
    }

    /**
     * Sets the name of the song.
     * @param name the name of the song
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the lyrics.
     * @return the lyrics
     */

    public String getLyrics() {
        return lyrics;
    }

    /**
     * Sets the lyrics.
     * @param lyrics the lyrics to set
     */

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    /**
     * Gets the title of the song to use in the set fragment's ListView.
     * @return the title
     */

    public String getTitle() {
        String minutesString = String.valueOf(getMinutes());
        String secondsString = String.valueOf(getSeconds());

        if (minutesString.length() == 1) {
            minutesString = '0' + minutesString;
        }

        if (getSeconds() < 10) {
            secondsString = '0' + secondsString;
        }

        return minutesString + ':' + secondsString + " - " + getName();
    }

    /**
     * Gets the attachments associated with the song.
     * @return a list of attachments
     */

    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }

    /**
     * Gets the tracks associated with the song.
     * @return a list of tracks
     */

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    /**
     * Sets the attachments associated with the song.
     * @param attachments the attachments to set
     */

    public void setAttachments(ArrayList<Attachment> attachments) {
        this.attachments = attachments;
    }

    /**
     * Sets the tracks associated with the song.
     * @param tracks the tracks to set
     */

    public void setTracks(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }

    /**
     * Gets the song's BPM.
     * @return the song's BPM
     */

    public int getBpm() {
        return bpm;
    }

    /**
     * Sets the song's BPM.
     * @param bpm the song's BPM
     */

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    /**
     * Gets the key of the song.
     * @return the key of the song
     */

    public String getKey() {
        return key;
    }

    /**
     * Sets the key of the song.
     * @param key the key to set
     */

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Returns the current attachment position (the attachment currently being viewed or modified).
     * @return the position of the current attachment
     */

    public int getCurrentAttachmentPosition() {
        return currentAttachmentPosition;
    }

    /**
     * Sets the current attachment position.
     * @param currentAttachmentPosition the position to set
     */

    public void setCurrentAttachmentPosition(int currentAttachmentPosition) {
        this.currentAttachmentPosition = currentAttachmentPosition;
    }

    /**
     * Sets the TracksFragment associated with the song.
     * @param tracksFragment the TracksFragment to set
     */

    public void setTracksFragment(TracksFragment tracksFragment) {
        this.tracksFragment = tracksFragment;
    }

    /**
     * Gets the TracksFragment associated with the song.
     * @return the TracksFragment
     */

    public TracksFragment getTracksFragment(){
        return tracksFragment;
    }

    /**
     * Gets the current track position (the track currently being played or modified).
     * @return the current position
     */

    public int getCurrentTrackPosition() {
        return currentTrackPosition;
    }

    /**
     * Sets the current tracks position.
     * @param currentTrackPosition the position to set
     */

    public void setCurrentTrackPosition(int currentTrackPosition) {
        this.currentTrackPosition = currentTrackPosition;
    }

    /**
     * Gets the song's notes.
     * @return the song's notes
     */

    public String getNotes() {
        return notes;
    }

    /**
     * Sets the song's notes.
     * @param notes the song's notes
     */

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
