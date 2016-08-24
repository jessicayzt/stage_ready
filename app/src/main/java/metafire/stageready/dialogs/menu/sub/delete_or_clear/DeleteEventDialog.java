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
import metafire.stageready.model.Schedule;

/**
 * Created by Jessica on 7/3/2016.
 */

public class DeleteEventDialog extends DialogFragment implements Serializable {
    private static final long serialVersionUID = 46617490753734585L;

    /**
     * @author  Jessica Yang <jessicayzt@hotmail.com>
     * @version 1.0
     * @since   1.0
     */

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Delete event?");

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Schedule.getInstance().getEvents().remove(Schedule.getInstance().getCurrentEvent());
                Schedule.getInstance().getCompactCalendarView().removeEvent(Schedule.getInstance().getCurrentEvent());
                dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DeleteEventDialog.this.getDialog().cancel();
            }
        });

        final AlertDialog deleteEventDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        Button positiveButton = deleteEventDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = deleteEventDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);
        deleteEventDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width * 0.85);

        deleteEventDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        return deleteEventDialog;
    }
}