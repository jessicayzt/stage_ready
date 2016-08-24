package metafire.stageready.dialogs.menu.sub.edit;

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

/**
 * Created by Jessica on 6/23/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class EditTrackDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = 595525692451095624L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.edit_track_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        builder.setTitle("Edit Track");

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // overwritten below
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditTrackDialog.this.getDialog().cancel();
                    }
                });

        final AlertDialog addBreakDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        Button positiveButton = addBreakDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = addBreakDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);
        addBreakDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        addBreakDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        EditText trackName = (EditText) view.findViewById(R.id.trackNameEditText);

        Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
        String oldTitle = currentSong.getTracks().get(currentSong.getCurrentTrackPosition()).getName();

        trackName.setText(oldTitle);

        addBreakDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean closeDialog = false;

                EditText trackName = (EditText) view.findViewById(R.id.trackNameEditText);

                final int defaultColor = trackName.getCurrentHintTextColor();
                trackName.setHintTextColor(defaultColor);

                String trackNameString = trackName.getText().toString().trim();

                int redColor = ContextCompat.getColor(getActivity(), R.color.colorRed);

                if (trackNameString.length() == 0){
                    trackName.setHintTextColor(redColor);
                    Toast toast = Toast.makeText(getActivity(), "Insert track name.", Toast.LENGTH_SHORT);
                    toast.show();
                }

                else {
                    Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
                    String oldTitle = currentSong.getTracks().get(currentSong.getCurrentTrackPosition()).getName();
                    currentSong.getTracks().get(currentSong.getCurrentTrackPosition()).setName(trackNameString);

                    currentSong.getTracksFragment().getTitles().remove(oldTitle);

                    if (currentSong.getTracksFragment().getTitles().size() < currentSong.getCurrentTrackPosition()){
                        currentSong.getTracksFragment().getTitles().add(trackNameString);
                    }
                    else {
                        currentSong.getTracksFragment().getTitles().add(currentSong.getCurrentTrackPosition(), trackNameString);
                    }
                    currentSong.setCurrentTrackPosition(100);
                    currentSong.getTracksFragment().getArrayAdapter().notifyDataSetChanged();
                    closeDialog = true;
                }

                if(closeDialog) {
                    addBreakDialog.dismiss();
                }
            }
        });
        return addBreakDialog;
    }
}