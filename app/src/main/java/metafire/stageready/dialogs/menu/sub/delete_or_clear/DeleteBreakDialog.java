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
import metafire.stageready.model.Break;
import metafire.stageready.model.SetManager;

/**
 * Created by Jessica on 6/9/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class DeleteBreakDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = -1523384595625575706L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final Break currentBreak = (Break) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();

        builder.setMessage("Delete break?");

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int index = SetManager.getInstance().getCurrentSet().getSlots().indexOf(currentBreak);
                SetManager.getInstance().getCurrentSet().getTitles().remove(index);
                SetManager.getInstance().getCurrentSet().getSetFragment().getTitles().remove(index);
                SetManager.getInstance().getCurrentSet().getSlots().remove(currentBreak);
                SetManager.getInstance().getCurrentSet().getSetFragment().getArrayAdapter().notifyDataSetChanged();
                SetManager.getInstance().getCurrentSet().updateStatsTextView();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DeleteBreakDialog.this.getDialog().cancel();
            }
        });

        final AlertDialog deleteBreakDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        Button positiveButton = deleteBreakDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = deleteBreakDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);
        deleteBreakDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        deleteBreakDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        return deleteBreakDialog;
    }

    @Override
    public void onStop(){
        super.onStop();
        SetManager.getInstance().getCurrentSet().getSetFragment().getArrayAdapter().notifyDataSetChanged();
    }
}