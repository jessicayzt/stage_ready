package metafire.stageready.activities;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.MediaController;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import metafire.stageready.R;
import metafire.stageready.adapters.AttachmentsAdapter;
import metafire.stageready.adapters.ViewPagerAdapter;
import metafire.stageready.dialogs.add.AddTrackDialog;
import metafire.stageready.dialogs.add.SaveRecordingDialog;
import metafire.stageready.dialogs.menu.LyricsMenuDialog;
import metafire.stageready.dialogs.menu.sub.delete_or_clear.DeleteAttachmentDialog;
import metafire.stageready.dialogs.menu.sub.edit.UndoLyricsDialog;
import metafire.stageready.fragments.AttachmentsFragment;
import metafire.stageready.fragments.LyricsFragment;
import metafire.stageready.fragments.RecordTrackFragment;
import metafire.stageready.fragments.StatsFragment;
import metafire.stageready.fragments.TracksFragment;
import metafire.stageready.libs.touch_image_view.TouchImageView;
import metafire.stageready.model.Attachment;
import metafire.stageready.model.SetManager;
import metafire.stageready.model.Song;

/**
 * Created by Jessica on 6/4/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class SongDetailsActivity extends AppCompatActivity implements Serializable, MediaController.MediaPlayerControl, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private static final long serialVersionUID = -8410895586509372864L;
    private ViewPager songDetailsViewPager;
    private MediaPlayer mediaPlayer;
    private MediaController mediaController;
    private Handler handler = new Handler();
    private MediaRecorder mediaRecorder;
    private String lastFilePath;
    private long lastChronometerBase;

    /**
     * Called when the activity is first created.
     * @param  savedInstanceState the saved instance state
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_details);
        Toolbar songDetailsActivityToolbar = (Toolbar) findViewById(R.id.songDetailsActivityToolbar);
        setSupportActionBar(songDetailsActivityToolbar);
        TabLayout songDetailsActivityTabLayout = (TabLayout) findViewById(R.id.songDetailsActivityTabLayout);
        songDetailsViewPager = (ViewPager) findViewById(R.id.songDetailsViewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        StatsFragment statsFragment = new StatsFragment();
        LyricsFragment lyricsFragment = new LyricsFragment();
        AttachmentsFragment attachmentsFragment = new AttachmentsFragment();
        TracksFragment tracksFragment = new TracksFragment();
        RecordTrackFragment recordTrackFragment = new RecordTrackFragment();

        viewPagerAdapter.addFragment(statsFragment, "STATS");
        viewPagerAdapter.addFragment(lyricsFragment, "LYRICS");
        viewPagerAdapter.addFragment(attachmentsFragment, "SHEET MUSIC");
        viewPagerAdapter.addFragment(tracksFragment, "TRACKS");
        viewPagerAdapter.addFragment(recordTrackFragment, "RECORD TRACK");

        songDetailsViewPager.setAdapter(viewPagerAdapter);
        songDetailsActivityTabLayout.setupWithViewPager(songDetailsViewPager);
        int songDetailsTabColor = ContextCompat.getColor(this, R.color.colorSongDetails);
        songDetailsActivityTabLayout.setBackgroundColor(songDetailsTabColor);
        int tabAccentColor = ContextCompat.getColor(this, R.color.colorBackground);
        songDetailsActivityTabLayout.setSelectedTabIndicatorColor(tabAccentColor);
        songDetailsActivityTabLayout.setTabTextColors(tabAccentColor, tabAccentColor);

        songDetailsViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                final InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(songDetailsViewPager.getWindowToken(), 0);

                if (mediaController != null) {
                    mediaController.hide();
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
        currentSong.setCurrentTrackPosition(100); // removes the highlight
    }

    /**
     * Called when the activity is paused. Saves changes to lyrics, clears any flags to keep the
     * screen on, stops media player and media recorder if either or both are playing, resets state
     * of the activity's UI buttons and sets up the main activity's UI. Calls serialization
     * method of SetManager.
     */

    @Override
    protected void onPause() {
        super.onPause();
        EditText lyricsEditText = (EditText) findViewById(R.id.lyricsEditText);
        Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
        if (lyricsEditText != null) {
            currentSong.setLyrics(lyricsEditText.getText().toString().trim());
        }

        Button importSheetMusicButton = (Button) findViewById(R.id.importSheetMusicButton);
        if (importSheetMusicButton != null){
            importSheetMusicButton.setClickable(true);
            importSheetMusicButton.setEnabled(true);
        }

        TouchImageView attachmentImageView = (TouchImageView) findViewById(R.id.attachmentImageView);
        Button closeViewButton = (Button) findViewById(R.id.closeViewButton);
        if (attachmentImageView != null && attachmentImageView.getVisibility() == View.VISIBLE) {
            if (closeViewButton != null) {
                closeViewButton.performClick();
                closeViewButton.setClickable(false);
                closeViewButton.setEnabled(false);
            }
        }

        if (closeViewButton != null){
            closeViewButton.setClickable(false);
            closeViewButton.setEnabled(false);
        }

        Button importTrackButton = (Button) findViewById(R.id.importTrackButton);
        if (importTrackButton != null){
            importTrackButton.setClickable(true);
            importTrackButton.setEnabled(true);
        }

        Button controllerButton = (Button) findViewById(R.id.controllerButton);
        if (controllerButton != null) {
            controllerButton.setClickable(false);
            controllerButton.setEnabled(false);
        }

        Button stopButton = (Button) findViewById(R.id.stopTrackButton);
        if (stopButton != null){
            stopButton.setClickable(false);
            stopButton.setEnabled(false);
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            if (currentSong.getTracksFragment().getArrayAdapter() != null) {
                currentSong.setCurrentTrackPosition(100);
                currentSong.getTracksFragment().getArrayAdapter().notifyDataSetChanged();
            }
            if (mediaController != null) {
                mediaController.hide();
                mediaController.setEnabled(false);
                mediaController = null;
            }
            mediaPlayer = null;
        }

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        if (mediaRecorder != null){
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            Chronometer timer = (Chronometer) findViewById(R.id.timer);
            if (timer != null){
                timer.stop();
                timer.setText(getResources().getString(R.string.chronometer_reset));
            }

            Button recordButton = (Button) findViewById(R.id.recordButton);
            if (recordButton != null) {
                recordButton.setText(getResources().getString(R.string.record));
            }
            File file = new File(getLastFilePath());
            file.delete(); // file is deleted from testing
        }

        Button stopRecordingButton = (Button) findViewById(R.id.stopRecordingButton);
        Button recordButton = (Button) findViewById(R.id.recordButton);

        if (stopRecordingButton != null && recordButton != null) {
            stopRecordingButton.setClickable(false);
            stopRecordingButton.setEnabled(false);
            recordButton.setClickable(true);
            recordButton.setEnabled(true);
        }

        SetManager.saveToFile(this);
        SetManager.getInstance().getCurrentSet().getSetFragment().getArrayAdapter().notifyDataSetChanged();
    }

    /**
     * Called when the IMPORT button on the attachments fragment is clicked. Launches import intent
     * for images if there are less than 4 attachments associated with the current song. Otherwise,
     * displays a toast message to the user.
     * @param view the view
     */

    public void onClickImportSheetMusic(View view) {
        Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
        if (currentSong.getAttachments().size() < 4){
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        }

        else {
            Toast toast = Toast.makeText(getApplicationContext(), "Maximum number of imports reached.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Called on activity result. Deals with imports of attachments (sheet music) and tracks.
     * @param reqCode the request code, 1 for attachments and 2 for tracks
     * @param resCode the result code
     * @param data the data associated with the result
     */

    public void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == RESULT_OK && reqCode == 1) {
            Uri imageUri = data.getData();
            Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
            currentSong.getAttachments().add(new Attachment(imageUri.toString()));
            GridView attachmentsGridView = (GridView) findViewById(R.id.attachmentsGridView);

            if (attachmentsGridView != null) {
                AttachmentsAdapter attachmentsAdapter = (AttachmentsAdapter) attachmentsGridView.getAdapter();
                attachmentsAdapter.notifyDataSetChanged();
            }
        }

        if (resCode == RESULT_OK && reqCode == 2) {
            Uri audioUri = data.getData();
            AddTrackDialog addTrackDialog = new AddTrackDialog();
            addTrackDialog.setTrackUri(audioUri.toString());
            addTrackDialog.show(getFragmentManager(), "AddTrackDialog");
        }
    }

    /**
     * Called when the CLOSE VIEW button on the attachments fragment is clicked. Resets the zoom of
     * the ImageView and closes it, then sets the CLOSE VIEW button to not clickable and not
     * enabled.
     * @param view the view
     */

    public void onClickCloseView(View view) {
        TouchImageView attachmentImageView = (TouchImageView) findViewById(R.id.attachmentImageView);

        if (attachmentImageView != null) {
            attachmentImageView.setZoom(1);
            attachmentImageView.setVisibility(View.GONE);
        }

        Button closeViewButton = (Button) findViewById(R.id.closeViewButton);
        if (closeViewButton != null){
            closeViewButton.setClickable(false);
            closeViewButton.setEnabled(false);
        }
    }

    /**
     * Called when the delete attachment dialog is to be launched from the attachment menu.
     */

    public void launchDeleteAttachmentDialog() {
        DialogFragment deleteAttachmentDialog = new DeleteAttachmentDialog();
        deleteAttachmentDialog.show(getFragmentManager(), "DeleteAttachmentDialog");
    }

    /**
     * Called when the IMPORT button on the tracks fragment is clicked. Launches import intent for
     * audio if there are less than 10 tracks associated with the current song. Otherwise, displays
     * a toast message to the user.
     * @param view the view
     */

    public void onClickImportTrack(View view) {
        Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();

        if (currentSong.getTracks().size() < 6) {
            Intent intent = new Intent();
            intent.setType("audio/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select audio source."), 2);
        }

        else {
            Toast toast = Toast.makeText(getApplicationContext(), "Maximum number of imports reached.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Called when a track is clicked in the track fragment's ListView. Plays the audio from the
     * associated file.
     * @param position the position of the track in the ListView and in the song's list of tracks
     */

    public void onClickTrack(int position) {

        if (mediaPlayer != null) { // if another track is already playing, stop it and release the media player
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
        currentSong.setCurrentTrackPosition(position);
        currentSong.getTracksFragment().getArrayAdapter().notifyDataSetChanged();
        playAudio(position);
    }

    /**
     * Called when the CONTROLLER button on the tracks fragment is clicked. Shows the controller if
     * it is applicable (controller is not shown and the media player is in use).
     * @param view the view
     */

    public void onClickController(View view) {
        if (mediaController != null && mediaPlayer != null) {
            if (!mediaController.isShowing() || !mediaController.isEnabled()) {
                mediaController.setAnchorView(findViewById(R.id.tracksMediaController));
                mediaController.setEnabled(true);
                mediaController.show();
            }
        }
    }

    /**
     * Called when the activity resumes. Removes highlight on any track in the track fragment. Sets
     * up UI's buttons' states for the activity.
     */

    @Override
    public void onResume() {
        super.onResume();
        Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
        Button importSheetMusicButton = (Button) findViewById(R.id.importSheetMusicButton);
        if (importSheetMusicButton != null){
            importSheetMusicButton.setClickable(true);
            importSheetMusicButton.setEnabled(true);
        }

        TouchImageView attachmentImageView = (TouchImageView) findViewById(R.id.attachmentImageView);
        Button closeViewButton = (Button) findViewById(R.id.closeViewButton);
        if (attachmentImageView != null && attachmentImageView.getVisibility() == View.VISIBLE) {
            if (closeViewButton != null) {
                closeViewButton.performClick();
                closeViewButton.setClickable(false);
                closeViewButton.setEnabled(false);
            }
        }

        if (closeViewButton != null){
            closeViewButton.setClickable(false);
            closeViewButton.setEnabled(false);
        }

        Button importTrackButton = (Button) findViewById(R.id.importTrackButton);
        if (importTrackButton != null){
            importTrackButton.setClickable(true);
            importTrackButton.setEnabled(true);
        }

        Button controllerButton = (Button) findViewById(R.id.controllerButton);
        if (controllerButton != null) {
            controllerButton.setClickable(false);
            controllerButton.setEnabled(false);
        }

        Button stopButton = (Button) findViewById(R.id.stopTrackButton);
        if (stopButton != null){
            stopButton.setClickable(false);
            stopButton.setEnabled(false);
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            if (currentSong.getTracksFragment().getArrayAdapter() != null) {
                currentSong.setCurrentTrackPosition(100);
                currentSong.getTracksFragment().getArrayAdapter().notifyDataSetChanged();
            }
            if (mediaController != null) {
                mediaController.hide();
                mediaController.setEnabled(false);
                mediaController = null;
            }
            mediaPlayer = null;
        }

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (mediaRecorder != null){
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            Chronometer timer = (Chronometer) findViewById(R.id.timer);
            if (timer != null){
                timer.stop();
                timer.setText(getResources().getString(R.string.chronometer_reset));
            }

            Button recordButton = (Button) findViewById(R.id.recordButton);
            if (recordButton != null) {
                recordButton.setText(getResources().getString(R.string.record));
            }
            File file = new File(getLastFilePath());
            file.delete(); // file is deleted from testing
        }

        Button stopRecordingButton = (Button) findViewById(R.id.stopRecordingButton);
        Button recordButton = (Button) findViewById(R.id.recordButton);

        if (stopRecordingButton != null && recordButton != null) {
            stopRecordingButton.setClickable(false);
            stopRecordingButton.setEnabled(false);
            recordButton.setClickable(true);
            recordButton.setEnabled(true);
        }
    }

    /**
     * Starts playing the media player if it is not null.
     */

    @Override
    public void start() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    /**
     * Called when the media player is paused.
     */

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    /**
     * Gets the duration of the media player's audio, or returns 0 if the media player is null.
     * @return the duration of the file, 0 if the media player is null
     */

    @Override
    public int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        else{
            return 0;
        }
    }

    /**
     * Gets the current position of the media player's state if it is not null, otherwise, returns
     * 0.
     * @return the current position, or 0 if the media player is null
     */

    @Override
    public int getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    /**
     * Called when the user seeks with the media player's seek bar.
     * @param pos the position to seek to
     */

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    /**
     * Checks if the media player is playing.
     * @return true if the media player is playing, false otherwise
     */

    @Override
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    /**
     * Always returns 0, as buffer percentage is not shown in the Ui due to small file sizes.
     * @return always 0
     */

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    /**
     * Returns true so the media player can pause.
     * @return true to enable pausing.
     */

    @Override
    public boolean canPause() {
        return true;
    }

    /**
     * Returns true so the media player can seek backwards.
     * @return true to enable seeking backwards
     */

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    /**
     * Returns true so the media player can seek forwards.
     * @return true to enable seeking forwards
     */

    @Override
    public boolean canSeekForward() {
        return true;
    }

    /**
     * Returns 0, as audio session Id is not used.
     * @return always 0
     */

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    /**
     * Plays the audio of the file associated with the given position.
     * @param position the position of the file to play
     */

    private void playAudio(int position) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (mediaController != null) {
            mediaController.hide();
        }

        mediaController = new MediaController(this);
        Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
        mediaController.setMediaPlayer(this);

        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(currentSong.getTracks().get(position).getUri()));
            mediaPlayer.prepare();

        } catch (IOException e) {
            Toast toast = Toast.makeText(getApplicationContext(), "File not found.", Toast.LENGTH_SHORT);
            toast.show();
            currentSong.setCurrentTrackPosition(100);
            currentSong.getTracksFragment().getArrayAdapter().notifyDataSetChanged();
        }
    }

    /**
     * Method not used as buffering is not shown in the UI due to small file sizes.
     * @param mediaPlayer the media player
     * @param percent the percent currently buffered
     */

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
    }

    /**
     * Called upon the media player reaching the end of a file.
     * @param mediaPlayer the media player
     */

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
        currentSong.setCurrentTrackPosition(100); // removes highlighting
        currentSong.getTracksFragment().getArrayAdapter().notifyDataSetChanged();
        if (mediaController != null) {
            mediaController.hide();
            mediaController.setEnabled(false);
            mediaController = null;
        }

        this.mediaPlayer = null;

        if (mediaRecorder == null) {

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            Button importSheetMusicButton = (Button) findViewById(R.id.importSheetMusicButton);

            if (importSheetMusicButton != null) {
                importSheetMusicButton.setClickable(true);
                importSheetMusicButton.setEnabled(true);
            }

            Button importTrackButton = (Button) findViewById(R.id.importTrackButton);

            if (importTrackButton != null) {
                importTrackButton.setClickable(true);
                importTrackButton.setEnabled(true);
            }

            Button controllerButton = (Button) findViewById(R.id.controllerButton);
            if (controllerButton != null) {
                controllerButton.setClickable(false);
                controllerButton.setEnabled(false);
            }
            Button stopButton = (Button) findViewById(R.id.stopTrackButton);
            if (stopButton != null) {
                stopButton.setClickable(false);
                stopButton.setEnabled(false);
            }
        }
    }

    /**
     * Called when the media player is prepared.
     * @param mediaPlayer the media player
     */

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaController.setAnchorView(findViewById(R.id.tracksMediaController));
        mediaController.setEnabled(true);
        mediaController.show();
        mediaPlayer.start();
        handler.post(new Runnable() {
            @Override
            public void run() {
                mediaController.setEnabled(true);
                mediaController.show();
                Button controllerButton = (Button) findViewById(R.id.controllerButton);
                if (controllerButton != null) {
                    controllerButton.setClickable(true);
                    controllerButton.setEnabled(true);
                }
                Button stopButton = (Button) findViewById(R.id.stopTrackButton);
                if (stopButton != null) {
                    stopButton.setClickable(true);
                    stopButton.setEnabled(true);
                }
                Button importSheetMusicButton = (Button) findViewById(R.id.importSheetMusicButton);

                if (importSheetMusicButton != null) {
                    importSheetMusicButton.setClickable(false);
                    importSheetMusicButton.setEnabled(false);
                }

                Button importTrackButton = (Button) findViewById(R.id.importTrackButton);

                if (importTrackButton != null) {
                    importTrackButton.setClickable(false);
                    importTrackButton.setEnabled(false);
                }
            }
        });
    }

    /**
     * Returns the media player.
     * @return the media player
     */

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /**
     * Called when the back button is pressed. Disables the back button and closes the ImageView on
     * the attachments fragment instead if it is visible.
     */

    @Override
    public void onBackPressed() {
        TouchImageView attachmentImageView = (TouchImageView) findViewById(R.id.attachmentImageView);
        Button closeViewButton = (Button) findViewById(R.id.closeViewButton);
        if (attachmentImageView != null && attachmentImageView.getVisibility() == View.VISIBLE) {
            if (closeViewButton != null) {
                closeViewButton.performClick();
                closeViewButton.setClickable(false);
                closeViewButton.setEnabled(false);
            }
        }
        else {
            super.onBackPressed();
        }
    }

    /**
     * Called when the STOP button is pressed on the tracks fragment. Stops the media player,
     * removes highlighting on the track that was playing, hides the media controller, removes flag
     * to keep the screen on if the media recorder is not in use, and configures UI buttons.
     * @param view the view
     */

    public void onClickStopTrack(View view) {
        if (mediaPlayer != null){
            mediaPlayer.stop();
            Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
            currentSong.setCurrentTrackPosition(100);
            currentSong.getTracksFragment().getArrayAdapter().notifyDataSetChanged();
            if (mediaController != null) {
                mediaController.hide();
                mediaController.setEnabled(false);
                mediaController = null;
            }

            mediaPlayer = null;
            Button controllerButton = (Button) findViewById(R.id.controllerButton);
            if (controllerButton != null) {
                controllerButton.setClickable(false);
                controllerButton.setEnabled(false);
            }

            Button stopButton = (Button) findViewById(R.id.stopTrackButton);
            if (stopButton != null){
                stopButton.setClickable(false);
                stopButton.setEnabled(false);
            }
        }

        if (mediaRecorder == null) {

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            Button importSheetMusicButton = (Button) findViewById(R.id.importSheetMusicButton);

            if (importSheetMusicButton != null) {
                importSheetMusicButton.setClickable(true);
                importSheetMusicButton.setEnabled(true);
            }

            Button importTrackButton = (Button) findViewById(R.id.importTrackButton);

            if (importTrackButton != null) {
                importTrackButton.setClickable(true);
                importTrackButton.setEnabled(true);
            }
        }
    }

    /**
     * Called when the STOP button on the record track fragment is clicked. Stops recording.
     * @param view the view
     */

    public void onClickStopRecording(View view) {
        stopRecording();
    }

    /**
     * Called when the RECORD button on the record track fragment is clicked. Begins recording if
     * possible (can write to external storage and less than 6 tracks associated with the current
     * song), otherwise, displays a toast message.
     * @param view the view
     */

    public void onClickRecord(View view) {
        if (canWriteExternalStorage()) {
            Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
            if (currentSong.getTracks().size() < 6) {
                Button recordButton = (Button) findViewById(R.id.recordButton);
                if (recordButton != null) {
                    recordButton.setText(getResources().getString(R.string.recording));
                    recordButton.setClickable(false);
                    recordButton.setEnabled(false);
                }

                Button stopRecordingButton = (Button) findViewById(R.id.stopRecordingButton);

                if (stopRecordingButton != null) {
                    stopRecordingButton.setClickable(true);
                    stopRecordingButton.setEnabled(true);
                }

                startRecording();
            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(), "Maximum number of imports reached.", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        else{
            Toast toast = Toast.makeText(getApplicationContext(), "Cannot write to external storage.", Toast.LENGTH_SHORT);
            toast.show();
        }

        Button importSheetMusicButton = (Button) findViewById(R.id.importSheetMusicButton);

        if (importSheetMusicButton != null) {
            importSheetMusicButton.setClickable(false);
            importSheetMusicButton.setEnabled(false);
        }

        Button importTrackButton = (Button) findViewById(R.id.importTrackButton);

        if (importTrackButton != null) {
            importTrackButton.setClickable(false);
            importTrackButton.setEnabled(false);
        }
    }

    /**
     * Starts recording by setting the mic as the audio source, 44100 as the sampling rate, 96000 as
     * encoding bit rate, MPEG_4 as the output format, AAC as the audio encoder, and creates the
     * directory for all external files of the app if it has not been created. If that is
     * successful, a file name based on the current time is generated and used for the recording.
     * Otherwise, a toast message is displayed. The chronometer is also reset and flag is added to
     * keep the screen on.
     */

    private void startRecording(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setAudioEncodingBitRate(96000);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        String filePath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filePath, "Stage Ready");
        Long currentTime = System.currentTimeMillis();

        if (!file.exists()){
            if (!file.mkdirs()){
                Toast toast = Toast.makeText(getApplicationContext(), "Cannot create directory in external storage.", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        }
        String outputFilePath = file.getAbsolutePath() + File.separator + currentTime.toString() + "_rec.m4a";
        mediaRecorder.setOutputFile(outputFilePath);
        lastFilePath = outputFilePath;

        try {
            mediaRecorder.prepare();
            Chronometer timer = (Chronometer) findViewById(R.id.timer);

            if (timer != null){
                timer.setBase(SystemClock.elapsedRealtime());
                lastChronometerBase = SystemClock.elapsedRealtime();
                timer.start();
            }

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            mediaRecorder.start();

        }
        catch (IOException e) {
            Toast toast = Toast.makeText(getApplicationContext(), "Error occurred with recording.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Stops recording by resetting, releasing, and then setting the media recorder to null. Stops
     * the chronometer, configures UI, removes the flag to keep the screen on if media player is
     * not in use, and launches the save recording dialog.
     */

    private void stopRecording(){
        if (mediaRecorder != null){
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;

            Chronometer timer = (Chronometer) findViewById(R.id.timer);
            if (timer != null){
                timer.stop();
            }

            Button recordButton = (Button) findViewById(R.id.recordButton);
            if (recordButton != null) {
                recordButton.setText(getResources().getString(R.string.record));
            }

            SaveRecordingDialog saveRecordingDialog = new SaveRecordingDialog();
            saveRecordingDialog.show(getFragmentManager(), "SaveRecordingDialog");

            if (mediaPlayer == null) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                Button importSheetMusicButton = (Button) findViewById(R.id.importSheetMusicButton);

                if (importSheetMusicButton != null) {
                    importSheetMusicButton.setClickable(true);
                    importSheetMusicButton.setEnabled(true);
                }
                Button importTrackButton = (Button) findViewById(R.id.importTrackButton);

                if (importTrackButton != null) {
                    importTrackButton.setClickable(true);
                    importTrackButton.setEnabled(true);
                }
            }
        }
    }

    /**
     * Gets the file path for the last recorded track.
     * @return the last file path
     */

    public String getLastFilePath(){
        return lastFilePath;
    }

    /**
     * Returns the media recorder.
     * @return the media recorder
     */

    public MediaRecorder getMediaRecorder(){
        return mediaRecorder;
    }

    /**
     * Called when the UNDO button in the lyrics fragment is clicked. Launches the undo lyrics
     * dialog.
     * @param view the view
     */

    public void onClickUndo(View view) {
        DialogFragment undoLyricsDialog = new UndoLyricsDialog();
        undoLyricsDialog.show(getFragmentManager(), "UndoLyricsDialog");
    }

    /**
     * Called when the MENU button in the lyrics fragment is clicked. Launches the lyrics menu
     * dialog.
     * @param view the view
     */

    public void onClickLyricsMenu(View view) {
        DialogFragment lyricsMenuDialog =  new LyricsMenuDialog();
        lyricsMenuDialog.show(getFragmentManager(), "LyricsMenuDialog");
    }

    /**
     * Checks if the external storage is present with read and write permissions.
     * @return true if can write to external storage, false otherwise
     */

    private static boolean canWriteExternalStorage() {
        return (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()));
    }

    /**
     * Returns the last chronometer base for resuming the RecordTrack fragment.
     * @return the last chronometer base
     */

    public long getLastChronometerBase() {
        return lastChronometerBase;
    }
}