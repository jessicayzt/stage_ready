package metafire.stageready.dialogs.menu.sub;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.Serializable;

import metafire.stageready.R;
import metafire.stageready.model.SetManager;

/**
 * Created by Jessica on 7/7/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class LyricsSettingsDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = -3233109224883760639L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.lyrics_settings_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        builder.setTitle("Lyrics Settings");

        final CheckBox useLargeTextSizeCheckBox = (CheckBox) view.findViewById(R.id.useLargeTextSizeCheckBox);
        final CheckBox alignLyricsToCentreCheckBox = (CheckBox) view.findViewById(R.id.alignLyricsToCentreCheckBox);

        useLargeTextSizeCheckBox.setChecked(SetManager.getInstance().getUseLargeTextSize());
        alignLyricsToCentreCheckBox.setChecked(SetManager.getInstance().getAlignLyricsToCentre());

        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                boolean useLargeTextSize = useLargeTextSizeCheckBox.isChecked();
                boolean alignLyricsToCentre = alignLyricsToCentreCheckBox.isChecked();

                SetManager.getInstance().setUseLargeTextSize(useLargeTextSize);
                SetManager.getInstance().setAlignLyricsToCentre(alignLyricsToCentre);

                EditText lyricsEditText = (EditText) getActivity().findViewById(R.id.lyricsEditText);

                if (useLargeTextSize){
                    lyricsEditText.setTextSize(27);
                }

                else {
                    lyricsEditText.setTextSize(18);
                }

                if (alignLyricsToCentre){
                    lyricsEditText.setGravity(Gravity.CENTER_HORIZONTAL);
                }

                else {
                    lyricsEditText.setGravity(Gravity.LEFT);
                }

            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LyricsSettingsDialog.this.getDialog().cancel();
                    }
                });

        final AlertDialog lyricsSettingsDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        final Button positiveButton = lyricsSettingsDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = lyricsSettingsDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);
        lyricsSettingsDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        lyricsSettingsDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        return lyricsSettingsDialog;
    }
}