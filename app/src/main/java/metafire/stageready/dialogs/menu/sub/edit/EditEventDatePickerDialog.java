package metafire.stageready.dialogs.menu.sub.edit;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import metafire.stageready.R;
import metafire.stageready.model.Schedule;

/**
 * Created by Jessica on 7/13/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class EditEventDatePickerDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = 204914213274172134L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View rootView = layoutInflater.inflate(R.layout.date_picker_dialog, null);
        builder.setView(rootView);
        builder.setCancelable(true);
        builder.setTitle("Choose Date");

        final DatePicker eventDatePicker = (DatePicker) rootView.findViewById(R.id.eventDatePicker);

        Calendar currentCalendar = Calendar.getInstance();
        Long yearInMillis = Long.parseLong(String.format(Locale.ENGLISH, "%.0f", 3.154e+10));
        eventDatePicker.setMinDate(currentCalendar.getTimeInMillis());
        eventDatePicker.setMaxDate(currentCalendar.getTimeInMillis() + yearInMillis);

        final EditEventDialog editEventDialog = (EditEventDialog) getFragmentManager().findFragmentByTag("EditEventDialog");
        if (editEventDialog != null) {

            eventDatePicker.init(editEventDialog.getYear(), editEventDialog.getMonth(), editEventDialog.getDay(), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {

                }
            });
        }

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditEventDatePickerDialog.this.getDialog().cancel();
                    }
                });

        final AlertDialog editEventDatePickerDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        final Button positiveButton = editEventDatePickerDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = editEventDatePickerDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);
        editEventDatePickerDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width * 0.85);

        editEventDatePickerDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);
        editEventDatePickerDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int day = eventDatePicker.getDayOfMonth();
                int month = eventDatePicker.getMonth();
                int year = eventDatePicker.getYear();

                Calendar testCalendar = Calendar.getInstance();
                testCalendar.set(year, month, day);

                if (Schedule.getInstance().getCompactCalendarView().getEvents(testCalendar.getTime()).size() == 6) {
                    DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd", Locale.ENGLISH);
                    Toast toast = Toast.makeText(getActivity(), "Maximum number of events reached on " + dateFormat.format(testCalendar.getTimeInMillis()), Toast.LENGTH_SHORT);
                    toast.show();

                }
                else {

                    editEventDialog.setDay(day); // editEventDialog cannot be null as it is the only dialog that can launch this dialog
                    editEventDialog.setYear(year);
                    editEventDialog.setMonth(month);
                    editEventDialog.updateDateTextView();

                    editEventDatePickerDialog.dismiss();
                }
            }
        });

        return editEventDatePickerDialog;
    }
}