package metafire.stageready.dialogs.menu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.Serializable;

import metafire.stageready.R;
import metafire.stageready.dialogs.menu.sub.delete_or_clear.DeleteTrackDialog;
import metafire.stageready.dialogs.menu.sub.edit.EditTrackDialog;
import metafire.stageready.dialogs.menu.sub.notes.TrackNotesDialog;
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

public class TrackMenuDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = 6907915587969531426L;
    private boolean stayHighlight;

    /**
     * Called when the activity is first created.
     * @param  savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        stayHighlight = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setItems(R.array.track_options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        DialogFragment editTrackDialog = new EditTrackDialog();
                        editTrackDialog.show(getFragmentManager(), "EditTrackDialog");
                        stayHighlight = true;
                        break;
                    case 1:
                        DialogFragment trackNotesDialog = new TrackNotesDialog();
                        trackNotesDialog.show(getFragmentManager(), "TrackNotesDialog");
                        stayHighlight = true;
                        break;
                    case 2:
                        DialogFragment deleteTrackDialog = new DeleteTrackDialog();
                        deleteTrackDialog.show(getFragmentManager(), "DeleteTrackDialog");
                        stayHighlight = true;
                        break;
                }
            }
        });

        final AlertDialog trackDialog = builder.show();

        trackDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        trackDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        return trackDialog;
    }

    /**
     * Called when the dialog is dismissed. Removes the highlighting on the selected track.
     * @param dialog the dialog interface
     */

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (!stayHighlight) {
            Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
            currentSong.setCurrentTrackPosition(100);
            currentSong.getTracksFragment().getArrayAdapter().notifyDataSetChanged();
        }
        dismiss();
    }
}