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

import java.io.Serializable;
import java.util.ArrayList;

import metafire.stageready.R;
import metafire.stageready.activities.SongDetailsActivity;
import metafire.stageready.adapters.TracksAdapter;
import metafire.stageready.model.SetManager;
import metafire.stageready.model.Song;
import metafire.stageready.model.Track;

/**
 * Created by Jessica on 6/9/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class TracksFragment extends ListFragment implements Serializable {

    private static final long serialVersionUID = -1763842888193560094L;
    private ArrayList<String> titles;
    private transient TracksAdapter arrayAdapter;

    /**
     * Creates a new TracksFragment.
     */

    public TracksFragment() {
        titles = new ArrayList<>();
    }

    /**
     * Called when the fragment's view is first created.
     * @param inflater the layout inflater used to inflate the view
     * @param container the view group used to inflate the view
     * @param savedInstanceState the saved instance state to load the view of the fragment
     * @return the view of the fragment
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tracks, container, false);
        arrayAdapter = new TracksAdapter(getActivity(), titles);
        Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
        SongDetailsActivity songDetailsActivity = (SongDetailsActivity) getActivity();
        if (songDetailsActivity.getMediaPlayer() == null){
            currentSong.setCurrentTrackPosition(100);
        }



        for (Track track : currentSong.getTracks()){
            String title = track.getName();
            if (!titles.contains(title)) {
                titles.add(title);
            }
        }

        setListAdapter(arrayAdapter);
        currentSong.setTracksFragment(this);

        Button importTrackButton = (Button) rootView.findViewById(R.id.importTrackButton);

        if (songDetailsActivity.getMediaPlayer() != null || songDetailsActivity.getMediaRecorder() != null){
            importTrackButton.setClickable(false);
            importTrackButton.setEnabled(false);
        }

        Button controllerButton = (Button) rootView.findViewById(R.id.controllerButton);

        Button stopTrackButton = (Button) rootView.findViewById(R.id.stopTrackButton);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthButton = (int) Math.floor(width*0.333);
        importTrackButton.setWidth(widthButton);
        importTrackButton.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        controllerButton.setWidth(widthButton);
        controllerButton.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        stopTrackButton.setWidth(widthButton);
        stopTrackButton.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.MULTIPLY);

        if (songDetailsActivity.getMediaPlayer() == null){
            controllerButton.setClickable(false);
            controllerButton.setEnabled(false);
            stopTrackButton.setClickable(false);
            stopTrackButton.setEnabled(false);
        }
        return rootView;
    }

    /**
     * Returns the array adapter.
     * @return the array adapter
     */

    public ArrayAdapter getArrayAdapter(){
        return arrayAdapter;
    }

    /**
     * Returns the titles.
     * @return an ArrayList of the titles.
     */

    public ArrayList<String> getTitles(){
        return titles;
    }
}