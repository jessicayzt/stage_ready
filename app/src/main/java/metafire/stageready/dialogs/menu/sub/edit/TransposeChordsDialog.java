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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import metafire.stageready.R;
import metafire.stageready.data_structs.CircularArrayList;

/**
 * Created by Jessica on 7/7/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class TransposeChordsDialog extends DialogFragment implements Serializable{

    private static final long serialVersionUID = 1214758858740417031L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.transpose_chords_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        builder.setTitle("Transpose Chords");

        final Spinner transposeChordsSpinner = (Spinner) view.findViewById(R.id.transposeChordsSpinner);

        ArrayAdapter halfStepsAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.half_steps_change, android.R.layout.simple_spinner_dropdown_item);
        transposeChordsSpinner.setAdapter(halfStepsAdapter);

        transposeChordsSpinner.setSelection(10);

        final Switch accidentalsSwitch = (Switch) view.findViewById(R.id.accidentalsSwitch);

        builder.setPositiveButton("Transpose", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditText lyricsEditText = (EditText) getActivity().findViewById(R.id.lyricsEditText);
                String lyricsString = lyricsEditText.getText().toString().trim();
                String halfSteps = (String) transposeChordsSpinner.getSelectedItem();
                String newLyrics = transposeChords(lyricsString, Integer.valueOf(halfSteps.replace("+", "")), accidentalsSwitch.isChecked());
                lyricsEditText.setText(newLyrics);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TransposeChordsDialog.this.getDialog().cancel();
                    }
                });

        final AlertDialog transposeChordsDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        final Button positiveButton = transposeChordsDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = transposeChordsDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);
        transposeChordsDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        transposeChordsDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        return transposeChordsDialog;
    }

    /**
     * Transposes chords based on given lyrics (only chords, which are denoted in square brackets
     * will be candidates for transposition), the number of half steps up or down, and whether
     * accidentals are denoted as sharps or flats.
     * @param lyrics the lyrics with the chords with which to transpose
     * @param halfSteps the number of half steps up or down (negative for down, positive for up)
     * @param accidentalsAsFlats true if accidentals are denoted as flats, false if sharps
     * @return a string with the chords in the given lyrics transposed
     */

    private String transposeChords(String lyrics, int halfSteps, boolean accidentalsAsFlats){

        if (accidentalsAsFlats){
            return lyrics
                     // match accidentals first
                    .replace("[A#", transposeChordFlats(2, halfSteps))
                    .replace("[C#", transposeChordFlats(5, halfSteps))
                    .replace("[D#", transposeChordFlats(7, halfSteps))
                    .replace("[F#", transposeChordFlats(10, halfSteps))
                    .replace("[G#", transposeChordFlats(0, halfSteps))
                    .replace("[Ab", transposeChordFlats(0, halfSteps))
                    .replace("[A", transposeChordFlats(1, halfSteps))
                    .replace("[Bb", transposeChordFlats(2, halfSteps))
                    .replace("[B", transposeChordFlats(3, halfSteps))
                    .replace("[C", transposeChordFlats(4, halfSteps))
                    .replace("[Db", transposeChordFlats(5, halfSteps))
                    .replace("[D", transposeChordFlats(6, halfSteps))
                    .replace("[Eb", transposeChordFlats(7, halfSteps))
                    .replace("[E", transposeChordFlats(8, halfSteps))
                    .replace("[F", transposeChordFlats(9, halfSteps))
                    .replace("[Gb", transposeChordFlats(10, halfSteps))
                    .replace("[G", transposeChordFlats(11, halfSteps))
                    .replace("[@$#", "[");
        }

        else {
            return lyrics
                    .replace("[Ab", transposeChordSharps(11, halfSteps))
                    .replace("[Bb", transposeChordSharps(1, halfSteps))
                    .replace("[Db", transposeChordSharps(4, halfSteps))
                    .replace("[Eb", transposeChordSharps(6, halfSteps))
                    .replace("[Gb", transposeChordSharps(9, halfSteps))
                    .replace("[A#", transposeChordSharps(1, halfSteps))
                    .replace("[A", transposeChordSharps(0, halfSteps))
                    .replace("[B", transposeChordSharps(2, halfSteps))
                    .replace("[C#", transposeChordSharps(4, halfSteps))
                    .replace("[C", transposeChordSharps(3, halfSteps))
                    .replace("[D#", transposeChordSharps(6, halfSteps))
                    .replace("[D", transposeChordSharps(5, halfSteps))
                    .replace("[E", transposeChordSharps(7, halfSteps))
                    .replace("[F#", transposeChordSharps(9, halfSteps))
                    .replace("[F", transposeChordSharps(8, halfSteps))
                    .replace("[G#", transposeChordSharps(11, halfSteps))
                    .replace("[G", transposeChordSharps(10, halfSteps))
                    .replace("[@$#", "[");
        }
    }

    /**
     * Transposes a single chord with the original index and number of half steps up or down with
     * the accidental denoted as a sharp.
     * @param originalChord the original index of the chord to transpose
     * @param halfSteps the number of half steps up or down (negative for down, positive for up)
     */

    private String transposeChordSharps(int originalChord, int halfSteps){
        String transposedChord;
        ArrayList<String> halfStepsArray = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.half_steps_sharps)));

        CircularArrayList<String> halfStepsCircularArray = new CircularArrayList<>();
        for (String halfStep : halfStepsArray) {
            halfStepsCircularArray.add(halfStep);
        }

        if (halfSteps < 0){
            int halfStepsDown = Math.abs(halfSteps);
            transposedChord = halfStepsCircularArray.get(originalChord - halfStepsDown);
        }

        else{
            transposedChord = halfStepsCircularArray.get(originalChord + halfSteps);
        }

        return "[@$#" + transposedChord;
    }

    /**
     * Transposes a single chord with the original index and number of half steps up or down with
     * any accidental denoted as a flat.
     * @param originalChord the original index of the chord to transpose
     * @param halfSteps the number of half steps up or down (negative for down, positive for up)
     */

    private String transposeChordFlats(int originalChord, int halfSteps){
        String transposedChord;
        ArrayList<String> halfStepsArray = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.half_steps_flats)));

        CircularArrayList<String> halfStepsCircularArray = new CircularArrayList<>();
        for (String halfStep : halfStepsArray) {
            halfStepsCircularArray.add(halfStep);
        }

        if (halfSteps < 0){
            int halfStepsDown = Math.abs(halfSteps);
            transposedChord = halfStepsCircularArray.get(originalChord - halfStepsDown);
        }

        else{
            transposedChord = halfStepsCircularArray.get(originalChord + halfSteps);
        }

        return "[@$#" + transposedChord;
    }
}
