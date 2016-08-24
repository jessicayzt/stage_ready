package metafire.stageready.dialogs.menu.sub;

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
import android.widget.CheckBox;

import java.io.Serializable;

import metafire.stageready.R;
import metafire.stageready.model.Schedule;

/**
 * Created by Jessica on 7/4/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class CalendarSettingsDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = -4795510076488048833L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.calendar_settings_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        builder.setTitle("Calendar Settings");

        final CheckBox useThreeLetterAbbrevCheckBox = (CheckBox) view.findViewById(R.id.useThreeLetterAbbrevCheckBox);
        final CheckBox showMondayAsFirstDayCheckBox = (CheckBox) view.findViewById(R.id.showMondayAsFirstDayCheckBox);

        useThreeLetterAbbrevCheckBox.setChecked(Schedule.getInstance().getUseThreeLetterAbbreviation());
        showMondayAsFirstDayCheckBox.setChecked(Schedule.getInstance().getShowMondayAsFirstDay());

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                boolean useThreeLetterAbbrev = useThreeLetterAbbrevCheckBox.isChecked();
                boolean showMondayAsFirstDay = showMondayAsFirstDayCheckBox.isChecked();

                Schedule.getInstance().setUseThreeLetterAbbreviation(useThreeLetterAbbrev);
                Schedule.getInstance().getCompactCalendarView().setUseThreeLetterAbbreviation(useThreeLetterAbbrev);

                Schedule.getInstance().setShowMondayAsFirstDay(showMondayAsFirstDay);
                Schedule.getInstance().getCompactCalendarView().setShouldShowMondayAsFirstDay(showMondayAsFirstDay);

            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CalendarSettingsDialog.this.getDialog().cancel();
                    }
                });

        final AlertDialog calendarSettingsDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        final Button positiveButton = calendarSettingsDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = calendarSettingsDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);
        calendarSettingsDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        calendarSettingsDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        return calendarSettingsDialog;
    }
}