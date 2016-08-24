package metafire.stageready.model;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import java.io.Serializable;

import metafire.stageready.R;

/**
 * Created by Jessica on 6/30/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public enum SchedEvents implements Serializable{

    PRACTICE, GIG, INTERVIEW, PODCAST, SESSION, PHOTOSHOOT;

    /**
     * Gets the name for the given SchedEvents enum
     * @param schedEvent the SchedEvents enum with which to retrieve the corresponding string name
     */

    public static String getName(SchedEvents schedEvent){
        switch (schedEvent) {
            case PRACTICE:
                return "Practice";
            case GIG:
                return "Gig";
            case INTERVIEW:
                return "Interview";
            case PODCAST:
                return "Podcast";
            case SESSION:
                return "Session";
            default:
                return "Photoshoot";
        }
    }

    /**
     * Gets the color for the given SchedEvents string name.
     * @param context the context
     * @param schedEventName the SchedEvents string name with which to retrieve the corresponding color
     */

    public static int getColor(Context context, String schedEventName){
        switch (schedEventName) {
            case "Practice":
                return ContextCompat.getColor(context, R.color.colorPractice);
            case "Gig":
                return ContextCompat.getColor(context, R.color.colorGig);
            case "Interview":
                return ContextCompat.getColor(context, R.color.colorInterview);
            case "Podcast":
                return ContextCompat.getColor(context, R.color.colorPodcast);
            case "Session":
                return ContextCompat.getColor(context, R.color.colorSession);
            default:
                return ContextCompat.getColor(context, R.color.colorPhotoshoot);
        }
    }
}