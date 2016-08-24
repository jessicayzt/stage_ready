package metafire.stageready.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Date;

import metafire.stageready.R;
import metafire.stageready.activities.MainActivity;
import metafire.stageready.libs.compact_calendar_view.CompactCalendarView;
import metafire.stageready.model.Schedule;

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class ScheduleFragment extends Fragment implements Serializable {

    private static final long serialVersionUID = 3879537044623180334L;

    /**
     * Called when the fragment's view is first created.
     * @param inflater the layout inflater used to inflate the view
     * @param container the view group used to inflate the view
     * @param savedInstanceState the saved instance state to load the view of the fragment
     * @return the view of the fragment
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_schedule, container, false);

        final CompactCalendarView compactCalendarView = (CompactCalendarView) rootView.findViewById(R.id.compactCalendarView);

        int colorTransparent = ContextCompat.getColor(getActivity(), R.color.colorTransparent);
        compactCalendarView.setCurrentSelectedDayBackgroundColor(colorTransparent);

        Button addEventButton = (Button) rootView.findViewById(R.id.addEventButton);
        Button schedMenuButton = (Button) rootView.findViewById(R.id.schedMenuButton);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthButton = (int) Math.floor(width*0.499);

        addEventButton.setWidth(widthButton);
        addEventButton.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        schedMenuButton.setWidth(widthButton);
        schedMenuButton.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        compactCalendarView.setLocale();

        final TextView monthTextView = (TextView) rootView.findViewById(R.id.monthTextView);

        Date firstDayOfCurrentMonth = compactCalendarView.getFirstDayOfCurrentMonth();

        final String monthName = (String) android.text.format.DateFormat.format("MMMM", firstDayOfCurrentMonth);
        String year = (String) android.text.format.DateFormat.format("yyyy", firstDayOfCurrentMonth);

        monthTextView.setText(String.format(getActivity().getResources().getString(R.string.space), monthName, year));

        Schedule.getInstance().setCompactCalendarView(compactCalendarView);

        if (Schedule.getInstance().isFirstLaunch()){
            compactCalendarView.addEvents(Schedule.getInstance().getEvents());
            compactCalendarView.setUseThreeLetterAbbreviation(Schedule.getInstance().getUseThreeLetterAbbreviation());
            compactCalendarView.setShouldShowMondayAsFirstDay(Schedule.getInstance().getShowMondayAsFirstDay());
            Schedule.getInstance().setIsFirstLaunch(false);
        }

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Schedule.getInstance().setSelectedDate(dateClicked);

                int colorBackground = ContextCompat.getColor(getActivity(), R.color.colorSelected);
                compactCalendarView.setCurrentSelectedDayBackgroundColor(colorBackground);

                MainActivity mainActivity = (MainActivity) getActivity();

                mainActivity.onClickDay();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                CompactCalendarView compactCalendarView = (CompactCalendarView) rootView.findViewById(R.id.compactCalendarView);
                TextView monthTextView = (TextView) rootView.findViewById(R.id.monthTextView);

                String monthName = (String) android.text.format.DateFormat.format("MMMM", firstDayOfNewMonth);
                String year = (String) android.text.format.DateFormat.format("yyyy", firstDayOfNewMonth);

                monthTextView.setText(String.format(getActivity().getResources().getString(R.string.space), monthName, year));
                int colorTransparent = ContextCompat.getColor(getActivity(), R.color.colorTransparent);
                compactCalendarView.setCurrentSelectedDayBackgroundColor(colorTransparent);
            }
        });

        return rootView;
    }

    /**
     * Called when the fragment is resumed. Calls the method to remove the events older than three
     * months.
     */

    @Override
    public void onResume(){
        super.onResume();
        Schedule.getInstance().getCompactCalendarView().removeEventsPastThreeMonths();
    }
}