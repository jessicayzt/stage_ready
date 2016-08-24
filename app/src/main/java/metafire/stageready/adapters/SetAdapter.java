package metafire.stageready.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.util.List;

import metafire.stageready.activities.MainActivity;
import metafire.stageready.model.Break;
import metafire.stageready.model.SetManager;
import metafire.stageready.model.Slot;

/**
 * Created by Jessica on 5/26/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class SetAdapter extends ArrayAdapter<String> implements Serializable {

    private static final long serialVersionUID = -6218385646198045018L;
    private MainActivity mainActivity;

    /**
     * Creates a SetAdapter with given context, resource, text view resource Id, and list of strings
     * @param context the context to construct the object
     * @param objects the list of strings with which to construct the adapter
     */

    public SetAdapter(Context context, List<String> objects) {
        super(context, metafire.stageready.R.layout.list_title, metafire.stageready.R.id.title, objects);
        this.mainActivity = (MainActivity) context;
    }

    /**
     * Returns the view with the given position, view, and ViewGroup.
     * @param position the position to retrieve the view
     * @param convertView the convertView
     * @param parent the parent ViewGroup
     * @return the view given the position, view, and ViewGroup given
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = super.getView(position, convertView, parent);
        if (positionExists(position)) {
            final Slot slot = SetManager.getInstance().getCurrentSet().getSlots().get(position);

            int breakColor = Color.rgb(235, 235, 235);

            int songColor = Color.rgb(245, 245, 245);

            final int selectedColor = Color.argb(115, 178, 235, 242);

            if (slot.getClass() == Break.class) {
                view.setBackgroundColor(breakColor);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setBackgroundColor(selectedColor);
                        SetManager.getInstance().getCurrentSet().setCurrentlyModifying(slot);
                        mainActivity.onClickBreak();
                    }
                });

            } else {
                view.setBackgroundColor(songColor);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setBackgroundColor(selectedColor);
                        SetManager.getInstance().getCurrentSet().setCurrentlyModifying(slot);
                        mainActivity.onClickSong();
                    }
                });
            }
        }

        return view;
    }

    /**
     * Checks if the given position exists.
     * @param position the position to check
     * @return true if the position exists, false otherwise
     */

    private static boolean positionExists(int position) {
        try {
            SetManager.getInstance().getCurrentSet().getSlots().get(position);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
}
