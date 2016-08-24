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
import metafire.stageready.dialogs.menu.sub.delete_or_clear.DeleteEventDialog;
import metafire.stageready.dialogs.menu.sub.edit.EditEventDialog;
import metafire.stageready.dialogs.menu.sub.notes.EventNotesDialog;

/**
 * Created by Jessica on 7/3/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class EventMenuDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = 3371746327086284221L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class to construct dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setItems(R.array.event_options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        DialogFragment editEventDialog = new EditEventDialog();
                        editEventDialog.show(getFragmentManager(), "EditEventDialog");
                        break;
                    case 1:
                        DialogFragment eventNotesDialog = new EventNotesDialog();
                        eventNotesDialog.show(getFragmentManager(), "EventNotesDialog");
                        break;
                    case 2:
                        DialogFragment deleteEventDialog = new DeleteEventDialog();
                        deleteEventDialog.show(getFragmentManager(), "DeleteEventDialog");
                        break;
                }
            }
        });

        final AlertDialog eventDialog = builder.show();

        eventDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        eventDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        return eventDialog;
    }
}