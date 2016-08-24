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
import metafire.stageready.dialogs.menu.sub.CalendarSettingsDialog;
import metafire.stageready.dialogs.menu.sub.UpcomingEventsDialog;
import metafire.stageready.dialogs.menu.sub.delete_or_clear.ClearEventsDialog;

/**
 * Created by Jessica on 7/4/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class SchedMenuDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = 1313826700621707114L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setItems(R.array.sched_menu_options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        DialogFragment upcomingEventsDialog = new UpcomingEventsDialog();
                        upcomingEventsDialog.show(getFragmentManager(), "UpcomingEventsDialog");
                        break;
                    case 1:
                        DialogFragment clearEventsDialog = new ClearEventsDialog();
                        clearEventsDialog.show(getFragmentManager(), "ClearEventsDialog");
                        break;
                    case 2:
                        DialogFragment schedSettingsDialog = new CalendarSettingsDialog();
                        schedSettingsDialog.show(getFragmentManager(), "CalendarSettingsDialog");
                        break;
                }
            }
        });
        final AlertDialog schedMenuDialog =  builder.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);
        schedMenuDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        schedMenuDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        return schedMenuDialog;
    }
}