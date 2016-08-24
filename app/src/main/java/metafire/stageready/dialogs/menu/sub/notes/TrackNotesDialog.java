package metafire.stageready.dialogs.menu.sub.notes;

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

import java.io.Serializable;

import metafire.stageready.R;
import metafire.stageready.model.SetManager;
import metafire.stageready.model.Song;

/**
 * Created by Jessica on 7/2/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class TrackNotesDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = -8100708388050330198L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.notes_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        builder.setTitle("Notes");

        final EditText notesEditText = (EditText) view.findViewById(R.id.notesEditText);

        final Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();

        notesEditText.setText(currentSong.getTracks().get(currentSong.getCurrentTrackPosition()).getNotes());

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                currentSong.getTracks().get(currentSong.getCurrentTrackPosition()).setNotes(notesEditText.getText().toString().trim());
                currentSong.setCurrentTrackPosition(100);
                currentSong.getTracksFragment().getArrayAdapter().notifyDataSetChanged();

            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TrackNotesDialog.this.getDialog().cancel();
                        Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
                        currentSong.setCurrentTrackPosition(100);
                        currentSong.getTracksFragment().getArrayAdapter().notifyDataSetChanged();
                    }
                });

        final AlertDialog trackNotesDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        final Button positiveButton = trackNotesDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = trackNotesDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);
        trackNotesDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        trackNotesDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        return trackNotesDialog;
    }

    /**
     * Called when the dialog is paused. Clears the highlighting on the selected track.
     */

    @Override
    public void onPause(){
        super.onPause();
        Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
        currentSong.setCurrentTrackPosition(100);
        currentSong.getTracksFragment().getArrayAdapter().notifyDataSetChanged();
    }
}