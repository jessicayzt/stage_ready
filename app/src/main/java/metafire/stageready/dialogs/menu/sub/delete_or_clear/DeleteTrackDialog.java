package metafire.stageready.dialogs.menu.sub.delete_or_clear;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Button;

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

public class DeleteTrackDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = 7248475682213249739L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
        String trackName = currentSong.getTracks().get(currentSong.getCurrentTrackPosition()).getName();
        builder.setMessage("Delete " + trackName + '?');

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
                int currentTrackPosition = currentSong.getCurrentTrackPosition();
                currentSong.getTracks().remove(currentTrackPosition);
                currentSong.getTracksFragment().getTitles().remove(currentTrackPosition);
                currentSong.setCurrentTrackPosition(100);
                currentSong.getTracksFragment().getArrayAdapter().notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DeleteTrackDialog.this.getDialog().cancel();
            }
        });

        final AlertDialog deleteTrackDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        Button positiveButton = deleteTrackDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = deleteTrackDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);
        deleteTrackDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        deleteTrackDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        return deleteTrackDialog;
    }
}