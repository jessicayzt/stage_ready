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
import metafire.stageready.model.Set;
import metafire.stageready.model.SetManager;

/**
 * Created by Jessica on 7/13/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class DeleteSavedSetDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = -4306103592398571835L;
    private int position;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Set setToRemove = SetManager.getInstance().getSets().get(position);

        builder.setMessage("Delete " + setToRemove.getName() + '?');

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SetManager.getInstance().getSets().remove(position);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DeleteSavedSetDialog.this.getDialog().cancel();
            }
        });

        final AlertDialog deleteSetDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        Button positiveButton = deleteSetDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = deleteSetDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);
        deleteSetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        deleteSetDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        return deleteSetDialog;
    }

    /**
     * Sets the position.
     * @param position the position to set
     */

    public void setPosition(int position){
        this.position = position;
    }
}