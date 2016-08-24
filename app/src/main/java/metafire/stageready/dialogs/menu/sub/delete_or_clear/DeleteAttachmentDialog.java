package metafire.stageready.dialogs.menu.sub.delete_or_clear;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;

import java.io.Serializable;

import metafire.stageready.R;
import metafire.stageready.adapters.AttachmentsAdapter;
import metafire.stageready.model.SetManager;
import metafire.stageready.model.Song;

/**
 * Created by Jessica on 6/16/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class DeleteAttachmentDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = -2651858809647180503L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Delete?");

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
                int currentAttachmentPosition = currentSong.getCurrentAttachmentPosition();
                currentSong.getAttachments().remove(currentAttachmentPosition);
                GridView attachmentsGridView = (GridView) getActivity().findViewById(R.id.attachmentsGridView);
                AttachmentsAdapter attachmentsAdapter = (AttachmentsAdapter) attachmentsGridView.getAdapter();
                attachmentsAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DeleteAttachmentDialog.this.getDialog().cancel();
            }
        });

        final AlertDialog deleteAttachmentDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        Button positiveButton = deleteAttachmentDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = deleteAttachmentDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);
        deleteAttachmentDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        deleteAttachmentDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        return deleteAttachmentDialog;
    }
}
