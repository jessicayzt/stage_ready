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
import metafire.stageready.model.Break;
import metafire.stageready.model.SetManager;

/**
 * Created by Jessica on 6/4/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class EditBreakDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = 4321980200257588740L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.edit_break_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);

        builder.setTitle("Edit Break");

        Break currentBreak = (Break) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
        EditText pos = (EditText) view.findViewById(R.id.positionEditText);
        EditText mins = (EditText) view.findViewById(R.id.minsEditText);
        EditText secs = (EditText) view.findViewById(R.id.secsEditText);

        pos.setText(String.valueOf(SetManager.getInstance().getCurrentSet().getSlots().indexOf(currentBreak) + 1));

        String breakSeconds = String.valueOf(currentBreak.getSeconds());
        if (breakSeconds.length() == 1){
            breakSeconds = '0' + breakSeconds;
        }
        mins.setText(String.valueOf(currentBreak.getMinutes()));
        secs.setText(breakSeconds);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditBreakDialog.this.getDialog().cancel();
                    }
                });

        final AlertDialog editBreakDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        Button positiveButton = editBreakDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = editBreakDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);

        editBreakDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        editBreakDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        editBreakDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                boolean closeDialog = false;

                EditText pos = (EditText) view.findViewById(R.id.positionEditText);
                EditText mins = (EditText) view.findViewById(R.id.minsEditText);
                EditText secs = (EditText) view.findViewById(R.id.secsEditText);

                final int defaultColor = pos.getCurrentHintTextColor();
                pos.setHintTextColor(defaultColor);
                mins.setHintTextColor(defaultColor);
                secs.setHintTextColor(defaultColor);

                String posString = pos.getText().toString().trim();
                String minsString = mins.getText().toString().trim();
                String secsString = secs.getText().toString().trim();

                ArrayList<EditText> fields = new ArrayList<>();

                fields.add(pos);
                fields.add(mins);
                fields.add(secs);

                ArrayList<EditText> missingFields = new ArrayList<>();

                int redColor = ContextCompat.getColor(getActivity(), R.color.colorRed);

                for(EditText field : fields){
                    if (field.getText().toString().trim().length() == 0){
                        missingFields.add(field);
                    }
                }

                if (!(missingFields.size() == 0)){
                    for(EditText missingField : missingFields){
                        missingField.setHintTextColor(redColor);
                    }
                    Toast toast = Toast.makeText(getActivity(), "Insert missing fields.", Toast.LENGTH_SHORT);
                    toast.show();
                }

                else if (!isNumeric(posString) || Integer.valueOf(posString) < 1 || Integer.valueOf(posString) > SetManager.getInstance().getCurrentSet().getSlots().size()){
                    Toast toast = Toast.makeText(getActivity(), "Invalid position in set.", Toast.LENGTH_SHORT);
                    toast.show();
                }

                else if (minsString.length()== 0 || secsString.length() == 0 ||
                        !(secsString.length() == 2) || !isNumeric(minsString) || !isNumeric(secsString)
                        || Integer.valueOf(secsString) > 60 || Integer.valueOf(secsString) < 0 || Integer.valueOf(minsString) < 0){

                    Toast toast = Toast.makeText(getActivity(), "Invalid break length.", Toast.LENGTH_SHORT);
                    toast.show();
                }

                else {
                    int posInt = (Integer.parseInt(posString)) - 1;
                    int minsInt = Integer.parseInt(minsString);
                    int secsInt = Integer.parseInt(secsString);

                    Break currentBreak = (Break) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();

                    int oldPosition = SetManager.getInstance().getCurrentSet().getSlots().indexOf(currentBreak);

                    String oldTitle = currentBreak.getTitle();

                    if (posInt == oldPosition){

                        currentBreak.setMinutes(minsInt);
                        currentBreak.setSeconds(secsInt);

                        String newTitle = currentBreak.getTitle();

                        SetManager.getInstance().getCurrentSet().getTitles().add(posInt, newTitle);
                        SetManager.getInstance().getCurrentSet().getSetFragment().getTitles().add(posInt, newTitle);
                        SetManager.getInstance().getCurrentSet().getTitles().remove(oldTitle);
                        SetManager.getInstance().getCurrentSet().getSetFragment().getTitles().remove(oldTitle);
                    }

                    else{
                        Break editedBreak = new Break(minsInt, secsInt);

                        String newTitle = editedBreak.getTitle();

                        if (posInt == (SetManager.getInstance().getCurrentSet().getSlots().size()-1)) {
                            SetManager.getInstance().getCurrentSet().getTitles().remove(oldPosition);
                            SetManager.getInstance().getCurrentSet().getSetFragment().getTitles().remove(oldPosition);
                            SetManager.getInstance().getCurrentSet().getSlots().remove(oldPosition);

                            SetManager.getInstance().getCurrentSet().getTitles().add(newTitle);
                            SetManager.getInstance().getCurrentSet().getSetFragment().getTitles().add(newTitle);
                            SetManager.getInstance().getCurrentSet().getSlots().add(editedBreak);
                        }
                        else if(posInt > oldPosition){

                            SetManager.getInstance().getCurrentSet().getTitles().add(posInt + 1, newTitle);
                            SetManager.getInstance().getCurrentSet().getSetFragment().getTitles().add(posInt + 1, newTitle);

                            SetManager.getInstance().getCurrentSet().getSlots().add(posInt + 1, editedBreak);

                            SetManager.getInstance().getCurrentSet().getTitles().remove(oldPosition);
                            SetManager.getInstance().getCurrentSet().getSetFragment().getTitles().remove(oldPosition);
                            SetManager.getInstance().getCurrentSet().getSlots().remove(oldPosition);
                        }
                        else{
                            SetManager.getInstance().getCurrentSet().getTitles().add(posInt, newTitle);
                            SetManager.getInstance().getCurrentSet().getSetFragment().getTitles().add(posInt, newTitle);
                            SetManager.getInstance().getCurrentSet().getSlots().add(posInt, editedBreak);

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
                    editBreakDialog.dismiss();
                }
            }
        });
        return editBreakDialog;
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
        catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Called when the dialog is stopped. Updates the set fragment's ListView (removes highlighting,
     * and the deleted break if it was deleted).
     */

    @Override
    public void onStop(){
        super.onStop();
        SetManager.getInstance().getCurrentSet().getSetFragment().getArrayAdapter().notifyDataSetChanged();
    }
}