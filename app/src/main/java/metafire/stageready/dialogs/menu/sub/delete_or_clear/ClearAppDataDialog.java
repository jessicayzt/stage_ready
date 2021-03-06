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

import java.io.IOException;
import java.io.Serializable;

import metafire.stageready.R;

/**
 * Created by Jessica on 7/6/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class ClearAppDataDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = 6814359419005702649L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Clear app data? All data that is not backed up will be lost! If you choose to proceed, data will be cleared and the application will terminate.");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Runtime runtime = Runtime.getRuntime();
                try {
                    runtime.exec("pm clear metafire.stageready");
                }
                catch (IOException e) {
                    // empty catch block
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ClearAppDataDialog.this.getDialog().cancel();
            }
        });

        final AlertDialog clearAppDataDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        Button positiveButton = clearAppDataDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = clearAppDataDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);
        clearAppDataDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        clearAppDataDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        return clearAppDataDialog;
    }
}