package metafire.stageready.dialogs.menu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import metafire.stageready.R;
import metafire.stageready.adapters.EventsAdapter;
import metafire.stageready.model.Schedule;


/**
 * Created by Jessica on 6/30/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class EventsDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = 3948764979441629692L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);

        Date date = Schedule.getInstance().getSelectedDate();

        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd", Locale.ENGLISH);

        builder.setTitle(dateFormat.format(date));

        if (Schedule.getInstance().getCompactCalendarView().getEvents(Schedule.getInstance().getSelectedDate()).size() == 0) {
            builder.setMessage("No events.");

            AlertDialog eventsDialog = builder.show();

            eventsDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int widthDialog = (int) Math.floor(width * 0.85);

            eventsDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

            return eventsDialog;

        }
        else {
            LayoutInflater layoutInflater = getActivity().getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.events_dialog, null);
            GridView eventsGridView = (GridView) view.findViewById(R.id.eventsGridView);
            EventsAdapter eventsAdapter = new EventsAdapter(getActivity());

            eventsGridView.setAdapter(eventsAdapter);
            builder.setView(view);

            AlertDialog eventDialog = builder.show();

            eventDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int widthDialog = (int) Math.floor(width * 0.85);

            eventDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

            return eventDialog;
        }
    }

    /**
     * Called when the dialog is dismissed. Removes the highlighting on the selected day.
     * @param dialog the dialog interface
     */

    @Override
    public void onDismiss(DialogInterface dialog) {
        int colorBackground = ContextCompat.getColor(getActivity(), R.color.colorTransparent);
        Schedule.getInstance().getCompactCalendarView().setCurrentSelectedDayBackgroundColor(colorBackground);
        super.onDismiss(dialog);
    }
}
