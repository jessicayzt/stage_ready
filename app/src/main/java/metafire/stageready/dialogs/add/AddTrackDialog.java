package metafire.stageready.dialogs.add;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;

import metafire.stageready.R;
import metafire.stageready.model.SetManager;
import metafire.stageready.model.Song;
import metafire.stageready.model.Track;

/**
 * Created by Jessica on 6/22/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class AddTrackDialog extends DialogFragment implements Serializable{

    private static final long serialVersionUID = 6322271450311086976L;
    private String trackUri;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class to construct dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.add_track_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        builder.setTitle("Add Track");

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // overwritten below
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddTrackDialog.this.getDialog().cancel();
                    }
                });

        final AlertDialog addTrackDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        final Button positiveButton = addTrackDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = addTrackDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);
        addTrackDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        addTrackDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        addTrackDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean closeDialog = false;

                EditText trackName = (EditText) view.findViewById(R.id.trackNameEditText);

                final int defaultColor = trackName.getCurrentHintTextColor();
                trackName.setHintTextColor(defaultColor);

                String trackNameString = trackName.getText().toString().trim();

                int redColor = ContextCompat.getColor(getActivity(), R.color.colorRed);

                if (trackNameString.length() == 0) {
                    trackName.setHintTextColor(redColor);
                    Toast toast = Toast.makeText(getActivity(), "Insert track name.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (duplicateTrackName(trackNameString)) {
                    Toast toast = Toast.makeText(getActivity(), "Track name in use.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
                    Track newImportedTrack = new Track(trackUri, trackNameString); // title from edit text after checks have been bypassed
                    currentSong.getTracks().add(newImportedTrack);
                    currentSong.setCurrentTrackPosition(100);
                    currentSong.getTracksFragment().getTitles().add(newImportedTrack.getName());
                    currentSong.getTracksFragment().getArrayAdapter().notifyDataSetChanged();
                    closeDialog = true;
                }

                if(closeDialog) {
                    addTrackDialog.dismiss();
                }
            }
        });

        return addTrackDialog;
    }

    /**
     * Checks if the track name given already exists in the list of tracks associated with the
     * current song.
     * @param trackName the track name to check
     * @return true if the track name already exists, false otherwise
     */

    private static boolean duplicateTrackName(String trackName) {
        boolean duplicate = false;
        Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
        for (Track track : currentSong.getTracks()) {
            String name = track.getName();
            if (name.equals(trackName)) {
                duplicate = true;
            }
        }
        return duplicate;
    }

    /**
     * Sets the track Uri.
     * @param trackUri the track Uri
     */

    public void setTrackUri(String trackUri){
        this.trackUri = trackUri;
    }
}
