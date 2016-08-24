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
import metafire.stageready.dialogs.menu.sub.LyricsSettingsDialog;
import metafire.stageready.dialogs.menu.sub.SaveLyricsDialog;
import metafire.stageready.dialogs.menu.sub.edit.SearchAndReplaceLyricsDialog;
import metafire.stageready.dialogs.menu.sub.edit.TransposeChordsDialog;

/**
 * Created by Jessica on 7/7/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class LyricsMenuDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = 7618281020539489469L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setItems(R.array.lyrics_menu_options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        DialogFragment saveLyricsDialog = new SaveLyricsDialog();
                        saveLyricsDialog.show(getFragmentManager(), "SaveLyricsDialog");
                        break;
                    case 1:
                        DialogFragment transposeChordsDialog = new TransposeChordsDialog();
                        transposeChordsDialog.show(getFragmentManager(), "TransposeChordsDialog");
                        break;
                    case 2:
                        DialogFragment searchAndReplaceLyricsDialog = new SearchAndReplaceLyricsDialog();
                        searchAndReplaceLyricsDialog.show(getFragmentManager(), "SearchAndReplaceLyricsDialog");
                        break;
                    case 3:
                        DialogFragment lyricsSettingsDialog = new LyricsSettingsDialog();
                        lyricsSettingsDialog.show(getFragmentManager(), "LyricsSettingsDialog");
                        break;
                }
            }
        });
        final AlertDialog lyricsMenuDialog = builder.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width * 0.85);
        lyricsMenuDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        lyricsMenuDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        return lyricsMenuDialog;
    }
}