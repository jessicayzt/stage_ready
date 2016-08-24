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
import metafire.stageready.dialogs.menu.sub.delete_or_clear.DeleteBreakDialog;
import metafire.stageready.dialogs.menu.sub.edit.EditBreakDialog;
import metafire.stageready.dialogs.menu.sub.notes.BreakNotesDialog;
import metafire.stageready.model.Break;
import metafire.stageready.model.SetManager;

/**
 * Created by Jessica on 6/4/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class BreakMenuDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = 9142410704883646458L;
    private boolean stayHighlight;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        stayHighlight = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Break currentBreak = (Break) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
        String breakMinutes = String.valueOf(currentBreak.getMinutes());
        String breakSeconds = String.valueOf(currentBreak.getSeconds());

        if (breakSeconds.length() == 1) {
            breakSeconds = '0' + breakSeconds;
        }

        if (breakMinutes.length() == 1) {
            breakMinutes = '0' + breakMinutes;
        }

        builder.setTitle("Break - " + breakMinutes + ':' + breakSeconds);
        builder.setItems(R.array.break_options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        DialogFragment editBreakDialog = new EditBreakDialog();
                        editBreakDialog.show(getFragmentManager(), "EditBreakDialog");
                        stayHighlight = true;
                        break;
                    case 1:
                        DialogFragment breakNotesDialog = new BreakNotesDialog();
                        breakNotesDialog.show(getFragmentManager(), "BreakNotesDialog");
                        stayHighlight = true;
                        break;
                    case 2:
                        DialogFragment deleteBreakDialog = new DeleteBreakDialog();
                        deleteBreakDialog.show(getFragmentManager(), "DeleteBreakDialog");
                        stayHighlight = true;
                        break;
                }
            }
        });

        final AlertDialog breakWindowDialog = builder.show();

        breakWindowDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        breakWindowDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        return breakWindowDialog;
    }

    /**
     * Called when the dialog is dismissed. Removes the highlighting on the selected break if it
     * should be removed.
     * @param dialog the dialog interface
     */

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (!stayHighlight) {
            SetManager.getInstance().getCurrentSet().getSetFragment().getArrayAdapter().notifyDataSetChanged();
        }
        super.onDismiss(dialog);
    }
}