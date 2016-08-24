package metafire.stageready.dialogs.menu.sub.notes;

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

import java.io.Serializable;

import metafire.stageready.R;
import metafire.stageready.model.Break;
import metafire.stageready.model.SetManager;

/**
 * Created by Jessica on 7/7/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class BreakNotesDialog extends DialogFragment implements Serializable{

    private static final long serialVersionUID = 2926079827956812985L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.notes_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        builder.setTitle("Notes");
        final Break currentBreak = (Break) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
        final EditText notesEditText = (EditText) view.findViewById(R.id.notesEditText);
        notesEditText.setText(currentBreak.getNotes());

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                currentBreak.setNotes(notesEditText.getText().toString().trim());
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        BreakNotesDialog.this.getDialog().cancel();
                    }
                });

        final AlertDialog breakNotesDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        final Button positiveButton = breakNotesDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = breakNotesDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);
        breakNotesDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width * 0.85);

        breakNotesDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        return breakNotesDialog;
    }

    /**
     * Called when the dialog is paused. Removes highlighting on the set fragment's ListView on the
     * selected break.
     */

    @Override
    public void onPause(){
        super.onPause();
        SetManager.getInstance().getCurrentSet().getSetFragment().getArrayAdapter().notifyDataSetChanged();
    }
}