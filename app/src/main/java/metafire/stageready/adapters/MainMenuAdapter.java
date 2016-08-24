package metafire.stageready.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by Jessica on 7/6/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class MainMenuAdapter extends ArrayAdapter<String> implements Serializable {

    private static final long serialVersionUID = -8590796101719191153L;

    /**
     * Constructs a MainMenuAdapter with the given context, resource, and objects
     * @param context the context with which to construct this object
     * @param objects an array of strings with which to construct the adapter
     */
    public MainMenuAdapter(Context context, String[] objects) {
        super(context, metafire.stageready.R.layout.support_simple_spinner_dropdown_item, objects);
    }

    /**
     * Returns the view with the given position, view, and ViewGroup.
     * @param position the position to retrieve the view
     * @param convertView the convertView
     * @param parent the parent ViewGroup
     * @return the view given the position, view, and ViewGroup given
     */

    @Override
    public View getDropDownView (int position, View convertView, ViewGroup parent){
        View view;
        if (position == 0) { // removes the index 0 from the UI to disable the default setting of the spinner (so it can be used as a menu)
            TextView textView = new TextView(getContext());
            textView.setHeight(0);
            textView.setVisibility(View.GONE);
            view = textView;
        } else {
            view = super.getDropDownView(position, null, parent); // every other position's view is created normally
        }
        return view;
    }
}
