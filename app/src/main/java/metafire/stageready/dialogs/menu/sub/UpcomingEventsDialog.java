package metafire.stageready.dialogs.menu.sub;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;

import java.io.Serializable;

import metafire.stageready.R;
import metafire.stageready.adapters.UpcomingEventsAdapter;
import metafire.stageready.model.Schedule;


/**
 * Created by Jessica on 7/4/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class UpcomingEventsDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = -5902121794090162396L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);

        builder.setTitle("Upcoming Events");

        if (Schedule.getInstance().getUpcomingEvents().size() == 0) {
            builder.setMessage("No upcoming events.");

            AlertDialog upcomingEventsDialog = builder.show();

            upcomingEventsDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int widthDialog = (int) Math.floor(width * 0.85);

            upcomingEventsDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

            return upcomingEventsDialog;
        }
        else {
            LayoutInflater layoutInflater = getActivity().getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.upcoming_events_dialog, null);
            GridView upcomingEventsGridView = (GridView) view.findViewById(R.id.upcomingEventsGridView);
            UpcomingEventsAdapter upcomingEventsAdapter = new UpcomingEventsAdapter(getActivity());

            upcomingEventsGridView.setAdapter(upcomingEventsAdapter);
            builder.setView(view);

            AlertDialog upcomingEventsDialog = builder.show();

            upcomingEventsDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int widthDialog = (int) Math.floor(width * 0.85);

            upcomingEventsDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

            return upcomingEventsDialog;
        }
    }

    /**
     * Called when the dialog is dismissed. Redraws the calendar.
     * @param dialog the dialog interface
     */

    @Override
    public void onDismiss(DialogInterface dialog) {
        Schedule.getInstance().getCompactCalendarView().invalidate();
        super.onDismiss(dialog);
    }
}
