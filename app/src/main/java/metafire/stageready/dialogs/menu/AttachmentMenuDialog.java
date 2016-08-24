package metafire.stageready.dialogs.menu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.Serializable;

import metafire.stageready.R;
import metafire.stageready.activities.SongDetailsActivity;
import metafire.stageready.dialogs.menu.sub.notes.AttachmentNotesDialog;

/**
 * Created by Jessica on 7/2/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class AttachmentMenuDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = 6881764892367007278L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setItems(R.array.attachment_options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        DialogFragment attachmentNotesDialog = new AttachmentNotesDialog();
                        attachmentNotesDialog.show(getFragmentManager(), "AttachmentNotesDialog");
                        break;
                    case 1:
                        SongDetailsActivity songDetailsActivity = (SongDetailsActivity) getActivity();
                        songDetailsActivity.launchDeleteAttachmentDialog();
                        break;
                }
            }
        });

        final AlertDialog attachmentDialog = builder.show();

        attachmentDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        attachmentDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        return attachmentDialog;
    }
}