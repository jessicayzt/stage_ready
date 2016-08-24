package metafire.stageready.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import java.io.Serializable;

import metafire.stageready.R;
import metafire.stageready.activities.SongDetailsActivity;

/**
 * Created by Jessica on 6/9/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class RecordTrackFragment extends Fragment implements Serializable {

    private static final long serialVersionUID = -407130417142712106L;

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

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_record_track, container, false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthButton = (int) Math.floor(width*0.499);

        Button stopRecordingButton = (Button) rootView.findViewById(R.id.stopRecordingButton);
        Button recordButton = (Button) rootView.findViewById(R.id.recordButton);

        stopRecordingButton.setWidth(widthButton);
        stopRecordingButton.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        recordButton.setWidth(widthButton);
        recordButton.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.MULTIPLY);

        SongDetailsActivity songDetailsActivity = (SongDetailsActivity) getActivity();

        if (songDetailsActivity.getMediaRecorder() != null) {
            stopRecordingButton.setClickable(true);
            stopRecordingButton.setEnabled(true);
            recordButton.setText(getActivity().getResources().getString(R.string.recording));
            recordButton.setClickable(false);
            recordButton.setEnabled(false);

            Chronometer timer = (Chronometer) rootView.findViewById(R.id.timer);
            timer.setBase(((SongDetailsActivity) getActivity()).getLastChronometerBase());
            timer.start();
        }

        else{
            recordButton.setText(getActivity().getResources().getString(R.string.record));
            recordButton.setClickable(true);
            recordButton.setEnabled(true);
            stopRecordingButton.setClickable(false);
            stopRecordingButton.setEnabled(false);
        }
        return rootView;


    }
}