package metafire.stageready.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import metafire.stageready.R;
import metafire.stageready.adapters.SetAdapter;
import metafire.stageready.model.SetManager;

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class SetFragment extends ListFragment implements Serializable {

    private static final long serialVersionUID = -2909963247564171954L;
    private ArrayList<String> titles;
    private transient SetAdapter arrayAdapter;

    /**
     * Constructs a new SetFragment.
     */

    public SetFragment() {
        titles = new ArrayList<>();
    }

    /**
     * Called when the fragment's view is first created.
     * @param inflater           the layout inflater used to inflate the view
     * @param container          the view group used to inflate the view
     * @param savedInstanceState the saved instance state to load the view of the fragment
     * @return the view of the fragment
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_set, container, false);
        arrayAdapter = new SetAdapter(getActivity(), titles);
        for (String title : SetManager.getInstance().getCurrentSet().getTitles()) {
            arrayAdapter.add(title);
        }
        setListAdapter(arrayAdapter);
        setRetainInstance(true);

        SetManager.getInstance().getCurrentSet().setSetFragment(this);

        Button addSongButton = (Button) rootView.findViewById(R.id.addSongButton);
        Button addBreakButton = (Button) rootView.findViewById(R.id.addBreakButton);
        Button clearSetButton = (Button) rootView.findViewById(R.id.setMenuButton);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthButton = (int) Math.floor(width * 0.333);

        addSongButton.setWidth(widthButton);
        addSongButton.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        addBreakButton.setWidth(widthButton);
        addBreakButton.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        clearSetButton.setWidth(widthButton);
        clearSetButton.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.MULTIPLY);

        TextView statsTextView = (TextView) rootView.findViewById(R.id.statsTextView);

        SetManager.getInstance().getCurrentSet().setStatsTextView(statsTextView);
        SetManager.getInstance().getCurrentSet().updateStatsTextView();

        return rootView;
    }

    /**
     * Returns the set fragment's array adapter.
     * @return the array adapter
     */

    public ArrayAdapter getArrayAdapter() {
        return arrayAdapter;
    }

    /**
     * Returns the set fragment's titles.
     * @return the set fragment's titles.
     */

    public ArrayList<String> getTitles() {
        return titles;
    }
}