package metafire.stageready.dialogs.add;

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
import metafire.stageready.model.SetManager;
import metafire.stageready.model.Song;

/**
 * Created by Jessica on 5/24/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class AddSongDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = -8223675686379826477L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class to construct dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.add_song_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        builder.setTitle("Add Song");

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // overwritten below
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddSongDialog.this.getDialog().cancel();
                    }
                });

        final AlertDialog addSongDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        final Button positiveButton = addSongDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = addSongDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);
        addSongDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        addSongDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        addSongDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean closeDialog = false;

                EditText songName = (EditText) view.findViewById(R.id.songNameEditText);
                EditText mins = (EditText) view.findViewById(R.id.minsEditText);
                EditText secs = (EditText) view.findViewById(R.id.secsEditText);

                final int defaultColor = songName.getCurrentHintTextColor();
                songName.setHintTextColor(defaultColor);
                mins.setHintTextColor(defaultColor);
                secs.setHintTextColor(defaultColor);

                String songNameString = songName.getText().toString().trim();
                String minsString = mins.getText().toString().trim();
                String secsString = secs.getText().toString().trim();

                ArrayList<EditText> fields = new ArrayList<>();

                fields.add(songName);
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
                        || Integer.valueOf(secsString) > 59 || Integer.valueOf(secsString) < 0 || Integer.valueOf(minsString) < 0) {

                    Toast toast = Toast.makeText(getActivity(), "Invalid song length.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (duplicateSongName(songNameString)) {
                    Toast toast = Toast.makeText(getActivity(), "Song already in set.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    int minsInt = Integer.parseInt(minsString);
                    int secsInt = Integer.parseInt(secsString);
                    Song newSong = new Song(songName.getText().toString().trim(), minsInt, secsInt);
                    SetManager.getInstance().getCurrentSet().addSong(newSong);
                    String songTitle = newSong.getTitle();
                    SetManager.getInstance().getCurrentSet().getTitles().add(songTitle);
                    SetManager.getInstance().getCurrentSet().getSetFragment().getTitles().add(songTitle);
                    SetManager.getInstance().getCurrentSet().getSetFragment().getArrayAdapter().notifyDataSetChanged();
                    SetManager.getInstance().getCurrentSet().updateStatsTextView();
                    closeDialog = true;
                }

                if(closeDialog) {
                    addSongDialog.dismiss();
                }
            }
        });

        return addSongDialog;
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
     * Checks if the song name given already exists in the set.
     * @param songName the song name to check
     * @return true if the song name already exists, false otherwise
     */

    private static boolean duplicateSongName(String songName) {
        boolean duplicate = false;
        for (Song song : SetManager.getInstance().getCurrentSet().getSongs()) {
            if (song.getName().equals(songName)) {
                duplicate = true;
            }
        }
        return duplicate;
    }
}
