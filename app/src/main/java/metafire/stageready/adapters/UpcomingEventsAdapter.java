package metafire.stageready.adapters;

import android.app.DialogFragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import metafire.stageready.R;
import metafire.stageready.activities.MainActivity;
import metafire.stageready.dialogs.menu.EventMenuDialog;
import metafire.stageready.dialogs.menu.sub.UpcomingEventsDialog;
import metafire.stageready.libs.compact_calendar_view.Event;
import metafire.stageready.model.EventData;
import metafire.stageready.model.Schedule;

/**
 * Created by Jessica on 7/4/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class UpcomingEventsAdapter extends BaseAdapter implements Serializable {

    private static final long serialVersionUID = 8964226666403835960L;
    private Context context;

    /**
     * Constructs an UpcomingEventsAdapter with the upcoming events associated with the schedule
     * and the given application context.
     * @param applicationContext the application context
     */

    public UpcomingEventsAdapter(Context applicationContext){
        context = applicationContext;
    }

    /**
     * Returns the size of the list of upcoming events
     * @return the size of the upcoming events list
     */

    @Override
    public int getCount() {
        return Schedule.getInstance().getUpcomingEvents().size();
    }

    /**
     * Returns the object (an event) at the given position.
     * @param position the position to retrieve the event
     * @return the object (an event) at the given position
     */

    @Override
    public Object getItem(int position) {
        return Schedule.getInstance().getUpcomingEvents().get(position);
    }

    /**
     * Returns the item Id (always 0, as item Id is not used).
     * @param position the position to retrieve the item Id
     * @return always 0
     */

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Returns the view with the given position, view, and ViewGroup.
     * @param position the position to retrieve the view
     * @param convertView the convertView
     * @param parent the parent ViewGroup
     * @return the view given the position, view, and ViewGroup given
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (!positionExists(position)) {
            return null;
        }

        else {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.upcoming_events_dialog_content, null);

            TextView dateTextView = (TextView) view.findViewById(R.id.dateTextView);
            TextView typeAndNameTextView = (TextView) view.findViewById(R.id.typeAndNameTextView);
            TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);
            TextView locationTextView = (TextView) view.findViewById(R.id.locationTextView);

            final Event currentEvent = Schedule.getInstance().getUpcomingEvents().get(position);

            EventData eventData = (EventData) currentEvent.getData();

            DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd", Locale.ENGLISH);

            Date date = new Date(currentEvent.getTimeInMillis());

            dateTextView.setText(String.format(context.getResources().getString(R.string.comma_space), dateFormat.format(date), eventData.getYear())); // eventData cannot be null, will always be an EventData object
            typeAndNameTextView.setText(String.format(context.getResources().getString(R.string.colon_space), eventData.getType(), eventData.getName()));
            timeTextView.setText(eventData.getTime());
            locationTextView.setText(eventData.getLocation());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Schedule.getInstance().setCurrentEvent(currentEvent);
                    DialogFragment eventDialog = new EventMenuDialog();
                    MainActivity mainActivity = (MainActivity) context;
                    UpcomingEventsDialog upcomingEventsDialog = (UpcomingEventsDialog) mainActivity.getFragmentManager().findFragmentByTag("UpcomingEventsDialog");
                    upcomingEventsDialog.dismiss();
                    eventDialog.show(mainActivity.getFragmentManager(), "EventMenuDialog");
                }
            });

            return view;
        }
    }

    /**
     * Checks if the given position exists.
     * @param position the position to check
     * @return true if the position exists, false otherwise
     */

    private static boolean positionExists(int position) {
        try {
            Schedule.getInstance().getUpcomingEvents().get(position);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
}
