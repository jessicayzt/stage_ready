package metafire.stageready.activities;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import metafire.stageready.R;
import metafire.stageready.adapters.MainMenuAdapter;
import metafire.stageready.adapters.ViewPagerAdapter;
import metafire.stageready.dialogs.menu.sub.AboutDialog;
import metafire.stageready.dialogs.menu.sub.ContactDeveloperDialog;
import metafire.stageready.dialogs.add.AddBreakDialog;
import metafire.stageready.dialogs.add.AddEventDatePickerDialog;
import metafire.stageready.dialogs.add.AddEventDialog;
import metafire.stageready.dialogs.add.AddSongDialog;
import metafire.stageready.dialogs.menu.BreakMenuDialog;
import metafire.stageready.dialogs.menu.EventsDialog;
import metafire.stageready.dialogs.menu.SchedMenuDialog;
import metafire.stageready.dialogs.menu.SetMenuDialog;
import metafire.stageready.dialogs.menu.SongMenuDialog;
import metafire.stageready.dialogs.menu.sub.BackupDialog;
import metafire.stageready.dialogs.menu.sub.RestoreBackupDialog;
import metafire.stageready.dialogs.menu.sub.delete_or_clear.ClearAppDataDialog;
import metafire.stageready.dialogs.menu.sub.edit.EditEventDatePickerDialog;
import metafire.stageready.fragments.ScheduleFragment;
import metafire.stageready.fragments.SetFragment;
import metafire.stageready.libs.compact_calendar_view.CompactCalendarView;
import metafire.stageready.model.Schedule;
import metafire.stageready.model.SetManager;

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class MainActivity extends AppCompatActivity implements Serializable, AdapterView.OnItemSelectedListener {

    private static final long serialVersionUID = 7937366308199225917L;
    private SetFragment setFragment;


    /**
     * Called when the activity is first created. Reads serialized instances of SetManager and
     * Schedule and loads them, if they exist. If not, it is the user's first launch, and a toast
     * message welcoming them is shown.
     * @param  savedInstanceState the saved instance state used to load the activity
     */

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar mainActivityToolbar = (Toolbar) findViewById(R.id.mainActivityToolbar);
        setSupportActionBar(mainActivityToolbar);
        TabLayout mainActivityTabLayout = (TabLayout) findViewById(R.id.mainActivityTabLayout);
        ViewPager mainActivityViewPager = (ViewPager) findViewById(R.id.mainActivityViewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        try {
            FileInputStream fileInputStreamSetManager = openFileInput("SetManager.ser");
            ObjectInputStream objectInputStreamSetManager = new ObjectInputStream(fileInputStreamSetManager);
            SetManager setManager = (SetManager) objectInputStreamSetManager.readObject();
            objectInputStreamSetManager.close();
            fileInputStreamSetManager.close();
            if (setManager != null) {
                SetManager.getInstance().loadSetManager(setManager);
            }

            FileInputStream fileInputStreamSchedule = openFileInput("Schedule.ser");
            ObjectInputStream objectInputStreamSchedule = new ObjectInputStream(fileInputStreamSchedule);
            Schedule schedule = (Schedule) objectInputStreamSchedule.readObject();
            objectInputStreamSchedule.close();
            fileInputStreamSchedule.close();
            if (schedule != null) {
                Schedule.getInstance().loadSchedule(schedule);
            }

        }

        catch (FileNotFoundException e) {
            // user's first time opening app
            Toast toast = Toast.makeText(getApplicationContext(), "Welcome to Stage Ready!", Toast.LENGTH_SHORT);
            toast.show();
        }
        catch (IOException | ClassNotFoundException e) {
            // empty catch block
        }

        setFragment = new SetFragment();
        ScheduleFragment scheduleFragment = new ScheduleFragment();

        viewPagerAdapter.addFragment(setFragment, "Set");
        viewPagerAdapter.addFragment(scheduleFragment, "Schedule");

        mainActivityViewPager.setAdapter(viewPagerAdapter);
        mainActivityTabLayout.setupWithViewPager(mainActivityViewPager);

        int tabAccentColor = ContextCompat.getColor(this, R.color.colorBackground);
        mainActivityTabLayout.setSelectedTabIndicatorColor(tabAccentColor);
        mainActivityTabLayout.setTabTextColors(tabAccentColor, tabAccentColor);

        TextView loadingTextView = (TextView) findViewById(R.id.loadingTextView);
        if (loadingTextView != null) {
            loadingTextView.setVisibility(View.GONE);
        }

        Spinner mainMenuSpinner = (Spinner) findViewById(R.id.mainMenuSpinner);
        MainMenuAdapter mainMenuAdapter = new MainMenuAdapter(this, getResources().getStringArray(R.array.main_menu_options));
        mainMenuAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        mainMenuSpinner.setAdapter(mainMenuAdapter);
        mainMenuSpinner.setOnItemSelectedListener(this);
    }

    /**
     * Called when the activity starts, implementation sets the visibility of the loading text view to GONE.
     */

    @Override
    protected void onStart(){
        super.onStart();
        TextView loadingTextView = (TextView) findViewById(R.id.loadingTextView);
        if (loadingTextView != null) {
            loadingTextView.setVisibility(View.GONE);
        }
    }

    /**
     * Called when the ADD SONG button is clicked. Launches add song dialog if the current set has
     * under or equal to 20 slots, otherwise, displays a toast message. Reloads application if the
     * set fragment's array adapter is null.
     * @param  view the view
     */

    public void onClickAddSong(View view) {
        if (SetManager.getInstance().getCurrentSet().getSlots().size() < 20) { // if current set has under 20 slots
            if (!(setFragment.getArrayAdapter() == null)) {
                DialogFragment addSongDialog = new AddSongDialog();
                addSongDialog.show(getFragmentManager(), "AddSongDialog");
            }
            else { // reload application as the set fragment's array adapter is null (only happens during hard device testing)
                Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                DialogFragment addSongDialog = new AddSongDialog();
                addSongDialog.show(getFragmentManager(), "AddSongDialog");
            }
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "Maximum set length reached.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Called when the ADD BREAK button is clicked. Launches add break dialog if the current set has
     * under or equal to 20 slots, otherwise, displays a toast message. Reloads application if the
     * set fragment's array adapter is null.
     * @param  view the view
     */

    public void onClickAddBreak(View view) {
        if (SetManager.getInstance().getCurrentSet().getSlots().size() < 20) { // if current set has under 20 slots
            if (!(setFragment.getArrayAdapter() == null)) {
                DialogFragment addBreakDialog = new AddBreakDialog();
                addBreakDialog.show(getFragmentManager(), "AddBreakDialog");
            } else { // reload application as the set fragment's array adapter is null (only happens during hard device testing)
                Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                DialogFragment addBreakDialog = new AddBreakDialog();
                addBreakDialog.show(getFragmentManager(), "AddBreakDialog");
            }
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "Maximum set length reached.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Called when the MENU button is clicked on set fragment. Launches set menu dialog.
     * @param  view the view
     */

    public void onClickSetMenu(View view){
        DialogFragment setMenuDialog = new SetMenuDialog();
        setMenuDialog.show(getFragmentManager(), "SetMenuDialog");
    }

    /**
     * Called when a song is clicked in the set fragment's ListView. Launches the corresponding
     * song's menu.
     */

    public void onClickSong() {
        DialogFragment songWindowDialog = new SongMenuDialog();
        songWindowDialog.show(getFragmentManager(), "SongMenuDialog");
    }

    /**
     * Called when a break is clicked in the set fragment's ListView. Launches the corresponding
     * break's menu.
     */

    public void onClickBreak() {
        DialogFragment breakWindowDialog = new BreakMenuDialog();
        breakWindowDialog.show(getFragmentManager(), "BreakMenuDialog");
    }

    /**
     * Called when the activity pauses, calls the methods necessary for serialization of SetManager
     * and Schedule.
     */

    @Override
    public void onPause(){
        super.onPause();
        SetManager.saveToFile(this);
        Schedule.getInstance().setIsFirstLaunch(true);
        Schedule.saveToFile(this);
    }

    /**
     * Called when the activity resumes, sets the visibility of the loading text view to GONE.
     */

    @Override
    public void onResume() {
        super.onResume();
        TextView loadingTextView = (TextView) findViewById(R.id.loadingTextView);
        if (loadingTextView != null) {
            loadingTextView.setVisibility(View.GONE);
        }
    }

    /**
     * Called when the ADD EVENT button on the schedule fragment is clicked. Launches the add event
     * dialog.
     * @param view the view
     */

    public void onClickAddEvent(View view) {
        DialogFragment addPracticeDialog = new AddEventDialog();
        addPracticeDialog.show(getFragmentManager(), "AddEventDialog");
    }

    /**
     * Called when the MENU button on the schedule fragment is clicked. Launches the schedule menu.
     * @param view the view
     */

    public void onClickSchedMenu(View view) {
        DialogFragment schedMenuDialog = new SchedMenuDialog();
        schedMenuDialog.show(getFragmentManager(), "SchedMenuDialog");
    }

    /**
     * Called when the left arrow on the calendar in the schedule fragment is clicked. Guards
     * to make sure user stays within a year of current time frame (a year into the past) by
     * preventing future navigation at the endpoint. Updates the month TextView and deselects the
     * current date from the calendar.
     *
     * @param view the view
     */

    public void onClickLeftArrow(View view) {
        CompactCalendarView compactCalendarView = (CompactCalendarView) findViewById(R.id.compactCalendarView);

        double yearBackwardInMillis = Calendar.getInstance().getTimeInMillis()-3.154e+10;

        if (Schedule.getInstance().getCompactCalendarView().getFirstDayOfCurrentMonth().getTime() > yearBackwardInMillis) {

            TextView monthTextView = (TextView) findViewById(R.id.monthTextView);

            if (compactCalendarView != null && monthTextView != null) {
                compactCalendarView.showPreviousMonth();

                Date firstDayOfCurrentMonth = compactCalendarView.getFirstDayOfCurrentMonth();

                String monthName = (String) android.text.format.DateFormat.format("MMMM", firstDayOfCurrentMonth);
                String year = (String) android.text.format.DateFormat.format("yyyy", firstDayOfCurrentMonth);

                monthTextView.setText(String.format(getResources().getString(R.string.space), monthName, year));

                int colorBackground = ContextCompat.getColor(this, R.color.colorTransparent);
                compactCalendarView.setCurrentSelectedDayBackgroundColor(colorBackground);
            }
        }

        else{
            Toast toast = Toast.makeText(getApplicationContext(), "No previous month to show.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Called when the right arrow on the calendar in the schedule fragment is clicked. Guards
     * to make sure user stays within a year of current time frame (a year into the future) by
     * preventing future navigation at the endpoint. Updates the month TextView and deselects the
     * current date from the calendar.
     *
     * @param view the view
     */

    public void onClickRightArrow(View view) {
        double yearForwardInMillis = Calendar.getInstance().getTimeInMillis()+3.154e+10;
        if (Schedule.getInstance().getCompactCalendarView().getFirstDayOfCurrentMonth().getTime() < yearForwardInMillis) {

            CompactCalendarView compactCalendarView = (CompactCalendarView) findViewById(R.id.compactCalendarView);
            TextView monthTextView = (TextView) findViewById(R.id.monthTextView);

            if (compactCalendarView != null && monthTextView != null) {
                compactCalendarView.showNextMonth();

                Date firstDayOfCurrentMonth = compactCalendarView.getFirstDayOfCurrentMonth();

                String monthName = (String) android.text.format.DateFormat.format("MMMM", firstDayOfCurrentMonth);
                String year = (String) android.text.format.DateFormat.format("yyyy", firstDayOfCurrentMonth);

                monthTextView.setText(String.format(getResources().getString(R.string.space), monthName, year));
                int colorBackground = ContextCompat.getColor(this, R.color.colorTransparent);
                compactCalendarView.setCurrentSelectedDayBackgroundColor(colorBackground);
            }
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), "No next month to show.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Called when the a day on the calendar is clicked. Launches the events dialog for that day.
     */

    public void onClickDay(){
        DialogFragment eventsDialog = new EventsDialog();
        eventsDialog.show(getFragmentManager(), "EventsDialog");
    }

    /**
     * Called when an item is selected on the main menu spinner.
     * @param adapterView the adapter view
     * @param view the view
     * @param i the position in the spinner's array
     * @param l param not used
     */

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner mainMenuSpinner = (Spinner) findViewById(R.id.mainMenuSpinner);
        switch(i){
            case 0: // empty default case
                break;
            case 1:
                DialogFragment backupDialog = new BackupDialog();
                backupDialog.show(getFragmentManager(), "BackupDialog");
                break;
            case 2:
                DialogFragment restoreBackupDialog = new RestoreBackupDialog();
                restoreBackupDialog.show(getFragmentManager(), "RestoreBackupDialog");
                break;
            case 3:
                DialogFragment aboutDialog = new AboutDialog();
                aboutDialog.show(getFragmentManager(), "AboutDialog");
                break;
            case 4:
                DialogFragment contactDeveloperDialog = new ContactDeveloperDialog();
                contactDeveloperDialog.show(getFragmentManager(), "ContactDeveloperDialog");
                break;
            case 5:
                String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                }
                catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                break;
            case 6:
                DialogFragment clearAppDataDialog = new ClearAppDataDialog();
                clearAppDataDialog.show(getFragmentManager(), "ClearAppDataDialog");
                break;
        }
        mainMenuSpinner.setSelection(0); // keeps the spinner's default case selected
    }

    /**
     * Called when nothing is selected on the spinner. No implementation.
     * @param adapterView the adapter view
     */

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /**
     * Called when the SET DATE button is clicked on the add event dialog. Launches the add event
     * date picker dialog.
     * @param view the view
     */

    public void onClickAddEventSetDate(View view) {
        DialogFragment datePickerDialog =  new AddEventDatePickerDialog();
        datePickerDialog.show(getFragmentManager(), "AddEventDatePickerDialog");
    }

    /**
     * Called when the SET DATE button is clicked on the edit event dialog. Launches the edit event
     * date picker dialog.
     * @param view the view
     */

    public void onClickEditEventSetDate(View view) {
        DialogFragment datePickerDialog =  new EditEventDatePickerDialog();
        datePickerDialog.show(getFragmentManager(), "EditEventDatePickerDialog");
    }
}