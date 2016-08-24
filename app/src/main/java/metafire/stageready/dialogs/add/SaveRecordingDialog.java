package metafire.stageready.dialogs.add;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;

import metafire.stageready.R;
import metafire.stageready.activities.SongDetailsActivity;
import metafire.stageready.model.SetManager;
import metafire.stageready.model.Song;
import metafire.stageready.model.Track;

/**
 * Created by Jessica on 6/24/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class SaveRecordingDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = 165862134393227564L;
    private boolean keepFile;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        keepFile = false;
        // Use the Builder class to construct dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.add_track_dialog, null);
        builder.setView(view);
        builder.setTitle("Save recording to tracks?");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SaveRecordingDialog.this.getDialog().cancel();
                // file deletion handled in onDismiss overwrite below
            }
        });

        final AlertDialog saveRecordingDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        Button positiveButton = saveRecordingDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = saveRecordingDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);

        saveRecordingDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        saveRecordingDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        saveRecordingDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
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
                    SongDetailsActivity songDetailsActivity = (SongDetailsActivity) getActivity();
                    Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
                    Track newImportedTrack = new Track(Uri.fromFile(new File(songDetailsActivity.getLastFilePath())).toString(), trackNameString); // title from edit text after checks have been bypassed
                    currentSong.getTracks().add(newImportedTrack);
                    currentSong.getTracksFragment().getTitles().add(newImportedTrack.getName());
                    currentSong.getTracksFragment().getArrayAdapter().notifyDataSetChanged();
                    closeDialog = true;
                }

                if (closeDialog) {
                    keepFile = true;
                    saveRecordingDialog.dismiss();
                }
            }
        });

        return saveRecordingDialog;
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
     * Called when the dialog is dismissed. Does not save track.
     * @param dialog the dialog interface
     */

    @Override
    public void onDismiss(DialogInterface dialog){
        if (!keepFile) {
            SongDetailsActivity songDetailsActivity = (SongDetailsActivity) getActivity();
            File file = new File(songDetailsActivity.getLastFilePath());
            if (!file.delete()) {
                Toast toast = Toast.makeText(getActivity(), "Recording kept in external storage.", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        Chronometer timer = (Chronometer) getActivity().findViewById(R.id.timer);
        if (timer != null){
            timer.setText(getActivity().getResources().getString(R.string.chronometer_reset));
        }

        Button stopRecordingButton = (Button) getActivity().findViewById(R.id.stopRecordingButton);
        Button recordButton = (Button) getActivity().findViewById(R.id.recordButton);

        if (stopRecordingButton != null && recordButton != null) {
            stopRecordingButton.setClickable(false);
            stopRecordingButton.setEnabled(false);
            recordButton.setClickable(true);
            recordButton.setEnabled(true);
        }
        dismiss();
    }
}