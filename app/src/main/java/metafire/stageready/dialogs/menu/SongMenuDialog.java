package metafire.stageready.dialogs.menu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.Serializable;

import metafire.stageready.R;
import metafire.stageready.activities.SongDetailsActivity;
import metafire.stageready.dialogs.menu.sub.delete_or_clear.DeleteSongDialog;
import metafire.stageready.dialogs.menu.sub.edit.EditSongDialog;
import metafire.stageready.dialogs.menu.sub.notes.SongNotesDialog;
import metafire.stageready.model.SetManager;
import metafire.stageready.model.Song;

/**
 * Created by Jessica on 6/4/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class SongMenuDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = 1128589228880138756L;
    private boolean stayHighlight;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        stayHighlight = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
        builder.setTitle(currentSong.getName());
        builder.setItems(R.array.song_options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        TextView loadingTextView = (TextView) getActivity().findViewById(R.id.loadingTextView);
                        loadingTextView.setText(getActivity().getResources().getString(R.string.loading));
                        loadingTextView.setVisibility(View.VISIBLE);
                        Intent i = new Intent(getActivity(), SongDetailsActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        break;

                    case 1:
                        DialogFragment editSongDialog = new EditSongDialog();
                        editSongDialog.show(getFragmentManager(), "EditSongDialog");
                        stayHighlight = true;
                        break;

                    case 2:
                        DialogFragment songNotesDialog = new SongNotesDialog();
                        songNotesDialog.show(getFragmentManager(), "SongNotesDialog");
                        stayHighlight = true;
                        break;
                    case 3:
                        DialogFragment deleteSongDialog = new DeleteSongDialog();
                        deleteSongDialog.show(getFragmentManager(), "DeleteSongDialog");
                        stayHighlight = true;
                        break;

                }
            }
        });
        final AlertDialog songWindowDialog =  builder.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);
        songWindowDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        songWindowDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        return songWindowDialog;
    }

    /**
     * Called when the dialog is dismissed. Updates the ListView in the set fragment.
     * @param dialog the dialog interface
     */

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (!stayHighlight) {
            SetManager.getInstance().getCurrentSet().getSetFragment().getArrayAdapter().notifyDataSetChanged();
        }
        dismiss();
    }
}