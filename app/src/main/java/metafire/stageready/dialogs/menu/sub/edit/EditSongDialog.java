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
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import metafire.stageready.R;
import metafire.stageready.fragments.TracksFragment;
import metafire.stageready.model.Attachment;
import metafire.stageready.model.SetManager;
import metafire.stageready.model.Song;
import metafire.stageready.model.Track;

/**
 * Created by Jessica on 6/4/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class EditSongDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = 8447026429185715487L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.edit_song_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
        builder.setTitle("Edit: " + currentSong.getName());

        EditText songName = (EditText) view.findViewById(R.id.songNameEditText);
        EditText pos = (EditText) view.findViewById(R.id.positionEditText);
        EditText mins = (EditText) view.findViewById(R.id.minsEditText);
        EditText secs = (EditText) view.findViewById(R.id.secsEditText);

        String songSeconds = String.valueOf(currentSong.getSeconds());
        if (songSeconds.length() == 1){
            songSeconds = '0' + songSeconds;
        }

        songName.setText(currentSong.getName());
        pos.setText(String.valueOf(SetManager.getInstance().getCurrentSet().getSlots().indexOf(currentSong) + 1));
        mins.setText(String.valueOf(currentSong.getMinutes()));
        secs.setText(songSeconds);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // overwritten below
            }

        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditSongDialog.this.getDialog().cancel();
                    }
                });

        final AlertDialog editSongDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        Button positiveButton = editSongDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = editSongDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);
        editSongDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        editSongDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        editSongDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean closeDialog = false;

                EditText songName = (EditText) view.findViewById(R.id.songNameEditText);
                EditText pos = (EditText) view.findViewById(R.id.positionEditText);
                EditText mins = (EditText) view.findViewById(R.id.minsEditText);
                EditText secs = (EditText) view.findViewById(R.id.secsEditText);

                final int defaultColor = songName.getCurrentHintTextColor();
                songName.setHintTextColor(defaultColor);
                pos.setHintTextColor(defaultColor);
                mins.setHintTextColor(defaultColor);
                secs.setHintTextColor(defaultColor);

                String songNameString = songName.getText().toString().trim();
                String posString = pos.getText().toString().trim();
                String minsString = mins.getText().toString().trim();
                String secsString = secs.getText().toString().trim();

                ArrayList<EditText> fields = new ArrayList<>();

                fields.add(songName);
                fields.add(pos);
                fields.add(mins);
                fields.add(secs);

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
                else if (!(secsString.length() == 2) || !isNumeric(minsString) || !isNumeric(secsString)
                        || Integer.valueOf(secsString) > 60 || Integer.valueOf(secsString) < 0 || Integer.valueOf(minsString) < 0) {

                    Toast toast = Toast.makeText(getActivity(), "Invalid song length.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (!isNumeric(posString) || Integer.valueOf(posString) < 1 || Integer.valueOf(posString) > SetManager.getInstance().getCurrentSet().getSlots().size()){
                    Toast toast = Toast.makeText(getActivity(), "Invalid position in set.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (duplicateSongName(songNameString)){
                    Toast toast = Toast.makeText(getActivity(), "Song already in set.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    int posInt = (Integer.parseInt(posString)) - 1;
                    int minsInt = Integer.parseInt(minsString);
                    int secsInt = Integer.parseInt(secsString);

                    Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();

                    int oldPosition = SetManager.getInstance().getCurrentSet().getSlots().indexOf(currentSong);

                    String oldTitle = currentSong.getTitle();

                    if (posInt == oldPosition){

                        currentSong.setName(songNameString);
                        currentSong.setMinutes(minsInt);
                        currentSong.setSeconds(secsInt);

                        String newTitle = currentSong.getTitle();

                        SetManager.getInstance().getCurrentSet().getTitles().add(posInt, newTitle);
                        SetManager.getInstance().getCurrentSet().getSetFragment().getTitles().add(posInt, newTitle);
                        SetManager.getInstance().getCurrentSet().getTitles().remove(oldTitle);
                        SetManager.getInstance().getCurrentSet().getSetFragment().getTitles().remove(oldTitle);
                    }
                    else {
                        String copyLyrics = currentSong.getLyrics();
                        int copyBpm = currentSong.getBpm();
                        String copyKey = currentSong.getKey();
                        ArrayList<Attachment> copyAttachments = currentSong.getAttachments();
                        ArrayList<Track> copyTracks = currentSong.getTracks();
                        TracksFragment copyTracksFragment = currentSong.getTracksFragment();


                        Song editedSong = new Song(songNameString, minsInt, secsInt);

                        editedSong.setLyrics(copyLyrics);
                        editedSong.setBpm(copyBpm);
                        editedSong.setKey(copyKey);
                        editedSong.setAttachments(copyAttachments);
                        editedSong.setTracks(copyTracks);
                        editedSong.setTracksFragment(copyTracksFragment);

                        String newTitle = editedSong.getTitle();

                        if (posInt == (SetManager.getInstance().getCurrentSet().getSlots().size()-1)) {
                            SetManager.getInstance().getCurrentSet().getTitles().remove(oldPosition);
                            SetManager.getInstance().getCurrentSet().getSetFragment().getTitles().remove(oldPosition);
                            SetManager.getInstance().getCurrentSet().getSlots().remove(oldPosition);

                            SetManager.getInstance().getCurrentSet().getTitles().add(newTitle);
                            SetManager.getInstance().getCurrentSet().getSetFragment().getTitles().add(newTitle);
                            SetManager.getInstance().getCurrentSet().getSlots().add(editedSong);
                        }
                        else if(posInt > oldPosition){

                            SetManager.getInstance().getCurrentSet().getTitles().add(posInt + 1, newTitle);
                            SetManager.getInstance().getCurrentSet().getSetFragment().getTitles().add(posInt + 1, newTitle);

                            SetManager.getInstance().getCurrentSet().getSlots().add(posInt + 1, editedSong);

                            SetManager.getInstance().getCurrentSet().getTitles().remove(oldPosition);
                            SetManager.getInstance().getCurrentSet().getSetFragment().getTitles().remove(oldPosition);
                            SetManager.getInstance().getCurrentSet().getSlots().remove(oldPosition);
                        }
                        else {
                            SetManager.getInstance().getCurrentSet().getTitles().add(posInt, newTitle);
                            SetManager.getInstance().getCurrentSet().getSetFragment().getTitles().add(posInt, newTitle);
                            SetManager.getInstance().getCurrentSet().getSlots().add(posInt, editedSong);

                            SetManager.getInstance().getCurrentSet().getTitles().remove(oldPosition+1);
                            SetManager.getInstance().getCurrentSet().getSetFragment().getTitles().remove(oldPosition+1);
                            SetManager.getInstance().getCurrentSet().getSlots().remove(oldPosition+1);
                        }
                    }
                    SetManager.getInstance().getCurrentSet().getSetFragment().getArrayAdapter().notifyDataSetChanged();
                    SetManager.getInstance().getCurrentSet().updateStatsTextView();
                    closeDialog = true;
                }
                if(closeDialog) {
                    editSongDialog.dismiss();
                }
            }
        });

        return editSongDialog;
    }

    /**
     * Checks if a string can be parsed into an integer (if it is numeric).
     * @param str the string to check
     * @return true if the string is numeric, false otherwise
     */

    private static boolean isNumeric(String str){
        try
        {
            Integer.parseInt(str);
        }
        catch(NumberFormatException e)
        {
            return false;
        }
        return true;
    }

    /**
     * Checks if the song name given already exists in the set.
     * @param songName the song name to check
     * @return true if the song name already exists, false otherwise
     */

    private static boolean duplicateSongName(String songName) {
        boolean duplicate = false;
        Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
        if (songName.equals(currentSong.getName())) {
            return false;
        } else {
            for (Song song : SetManager.getInstance().getCurrentSet().getSongs()) {
                if (song.getName().equals(songName)) {
                    duplicate = true;
                }
            }
        }
        return duplicate;
    }

    /**
     * Called when the dialog is stopped. Updates the set fragment's ListView (removes highlighting,
     * updates the edited song).
     */

    @Override
    public void onStop(){
        super.onStop();
        SetManager.getInstance().getCurrentSet().getSetFragment().getArrayAdapter().notifyDataSetChanged();
    }
}