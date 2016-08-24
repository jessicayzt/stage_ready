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
import metafire.stageready.model.SetManager;
import metafire.stageready.model.Song;

/**
 * Created by Jessica on 7/2/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class AttachmentNotesDialog extends DialogFragment implements Serializable{

    private static final long serialVersionUID = -3329220978462585729L;

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

        EditText notesEditText = (EditText) view.findViewById(R.id.notesEditText);
        Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
        notesEditText.setText(currentSong.getAttachments().get(currentSong.getCurrentAttachmentPosition()).getNotes());

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditText notesEditText = (EditText) view.findViewById(R.id.notesEditText);
                Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
                currentSong.getAttachments().get(currentSong.getCurrentAttachmentPosition()).setNotes(notesEditText.getText().toString().trim());
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AttachmentNotesDialog.this.getDialog().cancel();
                    }
                });

        final AlertDialog attachmentNotesDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        final Button positiveButton = attachmentNotesDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = attachmentNotesDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);
        attachmentNotesDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        attachmentNotesDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        return attachmentNotesDialog;
    }
}