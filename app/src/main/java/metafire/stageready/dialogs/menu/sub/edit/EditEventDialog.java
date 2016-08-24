package metafire.stageready.dialogs.menu.sub.edit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import metafire.stageready.R;
import metafire.stageready.libs.compact_calendar_view.CompactCalendarView;
import metafire.stageready.libs.compact_calendar_view.Event;
import metafire.stageready.model.EventData;
import metafire.stageready.model.SchedEvents;
import metafire.stageready.model.Schedule;

/**
 * Created by Jessica on 7/3/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class EditEventDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = 1798936536846866828L;
    private int day;
    private int month;
    private int year;
    private TextView dateTextView;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final EventData currentEventData = (EventData) Schedule.getInstance().getCurrentEvent().getData();

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View rootView = layoutInflater.inflate(R.layout.edit_event_dialog, null);
        builder.setView(rootView);
        builder.setCancelable(true);
        builder.setTitle("Edit Event");

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditEventDialog.this.getDialog().cancel();
                    }
                });

        final AlertDialog editEventDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        final Button positiveButton = editEventDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = editEventDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);
        editEventDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width * 0.85);

        editEventDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        final Spinner eventSpinner = (Spinner) rootView.findViewById(R.id.eventSpinner);

        ArrayList<SchedEvents> schedEventsList = new ArrayList<>(Arrays.asList(SchedEvents.values()));

        ArrayList<String> schedEventsStringList = new ArrayList<>();

        for (SchedEvents schedEvent : schedEventsList) {
            schedEventsStringList.add(SchedEvents.getName(schedEvent));
        }

        ArrayAdapter<String> eventAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, schedEventsStringList);

        eventSpinner.setAdapter(eventAdapter);

        eventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String eventType = (String) eventSpinner.getSelectedItem();
                Button eventColorIndicator = (Button) rootView.findViewById(R.id.eventColorIndicator);
                eventColorIndicator.setBackgroundColor(SchedEvents.getColor(getActivity(), eventType));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        eventSpinner.setSelection(schedEventsStringList.indexOf(currentEventData.getType())); // currentEventData is never null, always an EventData object

        Button editEventSetDateButton = (Button) rootView.findViewById(R.id.editEventSetDateButton);

        editEventSetDateButton.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.MULTIPLY);

        EditText name = (EditText) rootView.findViewById(R.id.eventNameEditText);
        EditText hour = (EditText) rootView.findViewById(R.id.eventHourEditText);
        EditText mins = (EditText) rootView.findViewById(R.id.eventMinsEditText);
        EditText location = (EditText) rootView.findViewById(R.id.locationEditText);

        name.setText(currentEventData.getName());
        hour.setText(currentEventData.getHour());
        mins.setText(currentEventData.getMins());
        location.setText(currentEventData.getLocation());


        final Spinner amPmSpinner = (Spinner) rootView.findViewById(R.id.amPmSpinner);
        ArrayAdapter amPmAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.am_pm, android.R.layout.simple_spinner_dropdown_item);
        amPmSpinner.setAdapter(amPmAdapter);

        ArrayList<String> amPmArrayList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.am_pm)));

        amPmSpinner.setSelection(amPmArrayList.indexOf(currentEventData.getAmOrPm()));

        dateTextView = (TextView) rootView.findViewById(R.id.dateTextView);

        setDay(currentEventData.getDay());
        setMonth(currentEventData.getMonth());
        setYear(currentEventData.getYear());

        updateDateTextView();

        editEventDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean closeDialog = false;

                Spinner eventSpinnerInner = (Spinner) rootView.findViewById(R.id.eventSpinner);

                String eventType = (String) eventSpinnerInner.getSelectedItem();

                EditText name = (EditText) rootView.findViewById(R.id.eventNameEditText);
                EditText hour = (EditText) rootView.findViewById(R.id.eventHourEditText);
                EditText mins = (EditText) rootView.findViewById(R.id.eventMinsEditText);
                EditText location = (EditText) rootView.findViewById(R.id.locationEditText);

                final int defaultColor = name.getCurrentHintTextColor();
                name.setHintTextColor(defaultColor);
                hour.setHintTextColor(defaultColor);
                mins.setHintTextColor(defaultColor);
                location.setHintTextColor(defaultColor);

                String nameString = name.getText().toString().trim();
                String hourString = hour.getText().toString().trim();
                String minsString = mins.getText().toString().trim();

                String locationString = location.getText().toString().trim();

                ArrayList<EditText> fields = new ArrayList<>();

                fields.add(name);
                fields.add(hour);
                fields.add(mins);
                fields.add(location);

                ArrayList<EditText> missingFields = new ArrayList<>();

                int redColor = ContextCompat.getColor(getActivity(), R.color.colorRed);

                for (EditText field : fields) {
                    if (field.getText().toString().trim().length() == 0) {
                        missingFields.add(field);
                    }
                }

                if (!(missingFields.size() == 0)) {
                    for (EditText missingField : missingFields) {
                        missingField.setHintTextColor(redColor);
                    }
                    Toast toast = Toast.makeText(getActivity(), "Insert missing fields.", Toast.LENGTH_SHORT);
                    toast.show();
                }

                else if (year == 0){
                    Toast toast = Toast.makeText(getActivity(), "Set a date.", Toast.LENGTH_SHORT);
                    toast.show();
                }

                else if (!(minsString.length() == 2) || !isNumeric(hourString) || !isNumeric(minsString)
                        || Integer.valueOf(minsString) > 59 || Integer.valueOf(minsString) < 0 || Integer.valueOf(hourString) < 1 || Integer.valueOf(hourString) > 12) {

                    Toast toast = Toast.makeText(getActivity(), "Invalid time.", Toast.LENGTH_SHORT);
                    toast.show();
                }

                else {
                    String copyNotes = currentEventData.getNotes();

                    Schedule.getInstance().getEvents().remove(Schedule.getInstance().getCurrentEvent());
                    Schedule.getInstance().getCompactCalendarView().removeEvent(Schedule.getInstance().getCurrentEvent());
                    int eventColor = SchedEvents.getColor(getActivity(), eventType);

                    int hourInt = Integer.parseInt(hourString);
                    int minsInt = Integer.parseInt(minsString);
                    int hourIntConverted;

                    String amOrPm = (String) amPmSpinner.getSelectedItem();

                    if (amOrPm.equals("AM") && hourInt == 12) {
                        hourIntConverted = 0;
                    } else if (amOrPm.equals("AM")) {
                        hourIntConverted = hourInt;
                    } else if (amOrPm.equals("PM") && hourInt == 12) {
                        hourIntConverted = hourInt;
                    } else {
                        hourIntConverted = hourInt + 12;
                    }

                    Calendar calendar = Calendar.getInstance();

                    calendar.set(year, month, day, hourIntConverted, minsInt);

                    String time = hourString + ':' + minsString + amOrPm.toLowerCase();

                    Event event = new Event(eventColor, calendar.getTimeInMillis(), new EventData(nameString, eventType, year, month, day, hourString, minsString, amOrPm, time, locationString));

                    EventData newEventData = (EventData) event.getData();
                    newEventData.setNotes(copyNotes); // newEventData is never null as it is always an EventData object
                    Schedule.getInstance().getEvents().add(event);

                    CompactCalendarView compactCalendarView = (CompactCalendarView) getActivity().findViewById(R.id.compactCalendarView);

                    compactCalendarView.addEvent(event);

                    closeDialog = true;
                }

                if (closeDialog) {
                    editEventDialog.dismiss();
                }

            }
        });

        return editEventDialog;
    }

    /**
     * Checks if a string can be parsed into an integer (if it is numeric).
     * @param str the string to check
     * @return true if the string is numeric, false otherwise
     */

    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Sets the day.
     * @param day the day to set
     */

    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Sets the month.
     * @param month the month to set
     */

    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Sets the year.
     * @param year the year to set
     */

    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Gets the month.
     * @return the month
     */

    public int getMonth() {
        return month;
    }

    /**
     * Gets the year.
     * @return the year
     */

    public int getYear() {
        return year;
    }

    /**
     * Gets the day.
     * @return the day
     */

    public int getDay() {
        return day;
    }

    /**
     * Updates the date text view on the dialog (to the right of the SET DATE button).
     */

    public void updateDateTextView() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy", Locale.ENGLISH);
        if (dateTextView != null){
            dateTextView.setText(dateFormat.format(calendar.getTimeInMillis()));
        }
    }
}