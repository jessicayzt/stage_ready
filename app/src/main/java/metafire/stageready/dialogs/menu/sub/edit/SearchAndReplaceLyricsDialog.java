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

import java.io.Serializable;

import metafire.stageready.R;

/**
 * Created by Jessica on 7/7/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class SearchAndReplaceLyricsDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = -8062175354271476797L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.search_and_replace_lyrics_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        builder.setTitle("Search and Replace");

        builder.setPositiveButton("Replace", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditText replaceEditText = (EditText) view.findViewById(R.id.replaceEditText);
                EditText withEditText = (EditText) view.findViewById(R.id.withEditText);
                EditText lyricsEditText = (EditText) getActivity().findViewById(R.id.lyricsEditText);
                String lyricsString = lyricsEditText.getText().toString().trim();
                String newLyrics = lyricsString.replace(replaceEditText.getText().toString().trim(), withEditText.getText().toString().trim());
                lyricsEditText.setText(newLyrics);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SearchAndReplaceLyricsDialog.this.getDialog().cancel();
                    }
                });

        final AlertDialog searchAndReplaceLyricsDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        final Button positiveButton = searchAndReplaceLyricsDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = searchAndReplaceLyricsDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);
        searchAndReplaceLyricsDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        searchAndReplaceLyricsDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        return searchAndReplaceLyricsDialog;
    }
}
