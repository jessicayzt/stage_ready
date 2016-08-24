package metafire.stageready.libs.compact_calendar_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.OverScroller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import metafire.stageready.model.Schedule;

/**
 * @author  SundeepK @ <https://github.com/SundeepK/CompactCalendarView> modified by Jessica Yang
 * <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class CompactCalendarView extends View implements Serializable {

    private static final long serialVersionUID = -1190892233021436699L;
    private CompactCalendarController compactCalendarController;
    private GestureDetectorCompat gestureDetector;

    public interface CompactCalendarViewListener {
        void onDayClick(Date dateClicked);
        void onMonthScroll(Date firstDayOfNewMonth);
    }

    private final GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            compactCalendarController.onSingleTapConfirmed(e);
            invalidate();
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            double yearBackwardInMillis = Calendar.getInstance().getTimeInMillis()-3.154e+10;
            double yearForwardInMillis = Calendar.getInstance().getTimeInMillis()+3.154e+10;
            if((Schedule.getInstance().getCompactCalendarView().getFirstDayOfCurrentMonth().getTime() > yearBackwardInMillis)
                    && (Schedule.getInstance().getCompactCalendarView().getFirstDayOfCurrentMonth().getTime() < yearForwardInMillis)) {
                compactCalendarController.onScroll(e1, e2, distanceX, distanceY);
                invalidate();
            }
            return true;
        }
    };

    public CompactCalendarView(Context context) {
        this(context, null);
    }

    public CompactCalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompactCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        compactCalendarController = new CompactCalendarController(new Paint(), new OverScroller(getContext()),
                new Rect(), attrs, getContext(),  Color.argb(255, 233, 84, 81),
                Color.argb(255, 64, 64, 64), Color.argb(255, 219, 219, 219), VelocityTracker.obtain(),
                Color.argb(255, 100, 68, 65));
        gestureDetector = new GestureDetectorCompat(getContext(), gestureListener);
    }

    /*
    Use a custom locale for compact calendar.
     */
    public void setLocale(){
        compactCalendarController.setLocale(Locale.ENGLISH);
        invalidate();
    }

    /*
    Compact calendar will use the locale to determine the abbreviation to use as the day column names.
    The default is to use the default locale and to abbreviate the day names to one character.
    Setting this to true will displace the short weekday string provided by java.
     */
    public void setUseThreeLetterAbbreviation(boolean useThreeLetterAbbreviation){
        compactCalendarController.setUseWeekDayAbbreviation(useThreeLetterAbbreviation);
        invalidate();
    }

    public void setShouldShowMondayAsFirstDay(boolean shouldShowMondayAsFirstDay) {
        compactCalendarController.setShouldShowMondayAsFirstDay(shouldShowMondayAsFirstDay);
        invalidate();
    }

    public void setCurrentSelectedDayBackgroundColor(int currentSelectedDayBackgroundColor) {
        compactCalendarController.setCurrentSelectedDayBackgroundColor(currentSelectedDayBackgroundColor);
        invalidate();
    }

    public void setListener(CompactCalendarViewListener listener){
        compactCalendarController.setListener(listener);
    }

    public Date getFirstDayOfCurrentMonth(){
        return compactCalendarController.getFirstDayOfCurrentMonth();
    }

    /**
     *  Adds an event to be drawn as an indicator in the calendar.
     *  If adding multiple events see {@link #addEvents(List)}} method.
     * @param event to be added to the calendar
     *
     */
    public void addEvent(Event event){
        compactCalendarController.addEvent(event);
        invalidate();
    }

    /**
     * Adds multiple events to the calendar and invalidates the view once all events are added.
     */
    public void addEvents(List<Event> events){
        compactCalendarController.addEvents(events);
        invalidate();
    }

    /**
     * Fetches the events for the date passed in
     * @param date
     * @return
     */

    public List<Event> getEvents(Date date){
        return compactCalendarController.getCalendarEventsFor(date);
    }

    /**
     * Fetches the events for the epochMillis passed in
     * @param epochMillis
     * @return
     */
    public List<Event> getEvents(long epochMillis){
        return compactCalendarController.getCalendarEventsFor(epochMillis);
    }

    /**
     * Removes an event from the calendar.
     *  @param event event to remove from the calendar
     *
     */
    public void removeEvent(Event event){
        compactCalendarController.removeEvent(event);
        invalidate();
    }

    /**
     * Removes all events that are more than 3 months old. **ADDED METHOD** insert edit note
     */

    public void removeEventsPastThreeMonths(){
        for(Event event : Schedule.getInstance().getEvents()){
            if (event.getTimeInMillis() < Calendar.getInstance().getTimeInMillis() - 7.884e+9){
                Schedule.getInstance().getCompactCalendarView().removeEvent(event);
            }
        }
    }

    /**
     * Clears all SchedEvents from the calendar.
     */
    public void removeAllEvents() {
        compactCalendarController.removeAllEvents();
    }

    public void showNextMonth(){
        compactCalendarController.showNextMonth();
        invalidate();
    }

    public void showPreviousMonth(){
        compactCalendarController.showPreviousMonth();
        invalidate();
    }

    @Override
    protected void onMeasure(int parentWidth, int parentHeight) {
        super.onMeasure(parentWidth, parentHeight);
        int width = MeasureSpec.getSize(parentWidth);
        int height = MeasureSpec.getSize(parentHeight);
        if(width > 0 && height > 0) {
            compactCalendarController.onMeasure(width, height, getPaddingRight(), getPaddingLeft());
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        compactCalendarController.onDraw(canvas);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(compactCalendarController.computeScroll()){
            invalidate();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        compactCalendarController.onTouch(event);
        invalidate();

        // prevent parent container from processing ACTION_MOVE events (scroll inside ViewPager issue #82)
        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            getParent().requestDisallowInterceptTouchEvent(true);
        } else if(event.getAction() == MotionEvent.ACTION_CANCEL) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        // always allow gestureDetector to detect onSingleTap and scroll events
        return gestureDetector.onTouchEvent(event);
    }
}