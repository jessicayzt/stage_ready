package metafire.stageready.model;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jessica on 6/26/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

// Singleton class

public class SetManager implements Serializable {

    private static String fileName = "SetManager.ser";
    private Set currentSet;
    private ArrayList<Set> sets;
    private static SetManager instance;
    private boolean useLargeTextSize;
    private boolean alignLyricsToCentre;

    /**
     * Constructs a new SetManager, with large text size and align lyrics to centre disabled.
     */

    private SetManager(){
        sets = new ArrayList<>();
        currentSet = new Set();
        useLargeTextSize = false;
        alignLyricsToCentre = false;
    }

    /**
     * Gets the only instance, or creates it if it does not exist.
     * @return the only instance of SetManager
     */

    public static SetManager getInstance() {
        if (instance == null) {
            instance = new SetManager();
        }
        return instance;
    }

    /**
     * Gets the current set.
     * @return the current set.
     */

    public Set getCurrentSet() {
        return currentSet;
    }

    /**
     * Returns all the sets in the manager.
     * @return a list of all the sets
     */

    public ArrayList<Set> getSets(){
        return sets;
    }

    /**
     * Serializes the only instance of SetManager to private app data.
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
     * Loads a set from the given saved set.
     * @param savedSet the saved set to load
     */

    public void loadSetFromSavedSet(Set savedSet){
        currentSet.clearAll();
        for (Slot slot : savedSet.getSlots()){
            currentSet.getSlots().add(slot);
        }
        for (String title : savedSet.getTitles()){
            currentSet.getTitles().add(title);
        }
        currentSet.setName(savedSet.getName());
    }

    /**
     * Loads a SetManager to the only instance of SetManager.
     * @param setManager the SetManager to load
     */

    public void loadSetManager(SetManager setManager){
        this.currentSet = setManager.getCurrentSet();
        this.sets = setManager.getSets();
        this.useLargeTextSize = setManager.getUseLargeTextSize();
        this.alignLyricsToCentre = setManager.getAlignLyricsToCentre();
    }

    /**
     * Checks whether large text size should be used to display lyrics.
     * @return true if large text size for lyrics should be used, false otherwise
     */

    public boolean getUseLargeTextSize() {
        return useLargeTextSize;
    }

    /**
     * Sets whether large text size should be used to display lyrics.
     * @param useLargeTextSize true if large text size for lyrics should be used, false otherwise
     */

    public void setUseLargeTextSize(boolean useLargeTextSize) {
        this.useLargeTextSize = useLargeTextSize;
    }

    /**
     * Checks whether lyrics should be aligned to centre.
     * @return true if lyrics should aligned to centre, false otherwise
     */

    public boolean getAlignLyricsToCentre() {
        return alignLyricsToCentre;
    }

    /**
     * Sets whether lyrics should be aligned to centre.
     * @param alignLyricsToCentre true if lyrics should aligned to centre, false otherwise
     */

    public void setAlignLyricsToCentre(boolean alignLyricsToCentre) {
        this.alignLyricsToCentre = alignLyricsToCentre;
    }
}