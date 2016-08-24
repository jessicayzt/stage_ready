package metafire.stageready.adapters;

import android.app.DialogFragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.Serializable;

import metafire.stageready.R;
import metafire.stageready.activities.MainActivity;
import metafire.stageready.dialogs.menu.EventMenuDialog;
import metafire.stageready.dialogs.menu.EventsDialog;
import metafire.stageready.libs.compact_calendar_view.Event;
import metafire.stageready.model.EventData;
import metafire.stageready.model.Schedule;

/**
 * Created by Jessica on 6/30/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class EventsAdapter extends BaseAdapter implements Serializable {

    private static final long serialVersionUID = 1537808472154985159L;
    private Context context;

    /**
     * Constructs an EventsAdapter with the given application context
     * @param applicationContext the application context
     */

    public EventsAdapter(Context applicationContext){
        context = applicationContext;
    }

    /**
     * Returns the number of events for the selected date
     * @return the number of events for the selected date
     */

    @Override
    public int getCount() {
        return Schedule.getInstance().getCompactCalendarView().getEvents(Schedule.getInstance().getSelectedDate()).size();
    }

    /**
     * Returns the object (an event) with the given position
     * @param position the position of the event to return
     * @return the object (an event) in the given position
     */

    @Override
    public Object getItem(int position) {
        return Schedule.getInstance().getCompactCalendarView().getEvents(Schedule.getInstance().getSelectedDate()).get(position);
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

        else{
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.events_dialog_content, null);
            TextView typeAndNameTextView = (TextView) view.findViewById(R.id.typeAndNameTextView);
            TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);
            TextView locationTextView = (TextView) view.findViewById(R.id.locationTextView);

            final Event currentEvent = Schedule.getInstance().getCompactCalendarView().getEvents(Schedule.getInstance().getSelectedDate()).get(position);

            EventData eventData = (EventData) currentEvent.getData();

            typeAndNameTextView.setText(String.format(context.getResources().getString(R.string.colon_space), eventData.getType(), eventData.getName())); // eventData will never be null, always an EventData object
            timeTextView.setText(eventData.getTime());
            locationTextView.setText(eventData.getLocation());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Schedule.getInstance().setCurrentEvent(currentEvent);
                    DialogFragment eventDialog = new EventMenuDialog();
                    MainActivity mainActivity = (MainActivity) context;
                    EventsDialog eventsDialog = (EventsDialog) mainActivity.getFragmentManager().findFragmentByTag("EventsDialog");
                    eventsDialog.dismiss();
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
            Schedule.getInstance().getCompactCalendarView().getEvents(Schedule.getInstance().getSelectedDate()).get(position);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
}
