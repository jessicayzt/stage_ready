package metafire.stageready.dialogs.menu.sub;

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

import java.io.Serializable;

import metafire.stageready.R;

/**
 * Created by Jessica on 7/11/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class AboutDialog extends DialogFragment implements Serializable{

    private static final long serialVersionUID = 5311387407126287570L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.about_dialog, null);
        builder.setView(view);
        builder.setTitle("About");

        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AboutDialog.this.getDialog().cancel();
            }
        });

        final AlertDialog aboutDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        Button positiveButton = aboutDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        aboutDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        aboutDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        return aboutDialog;
    }
}