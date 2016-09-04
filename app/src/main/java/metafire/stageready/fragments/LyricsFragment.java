package metafire.stageready.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;

import metafire.stageready.R;
import metafire.stageready.model.SetManager;
import metafire.stageready.model.Song;

/**
 * Created by Jessica on 6/9/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class LyricsFragment extends Fragment implements Serializable {

    private static final long serialVersionUID = 6678923115233176341L;

    /**
     * Called when the fragment's view is first created.
     * @param inflater the layout inflater used to inflate the view
     * @param container the view group used to inflate the view
     * @param savedInstanceState the saved instance state to load the view of the fragment
     * @return the view of the fragment
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_lyrics, container, false);
        final EditText lyricsEditText = (EditText) rootView.findViewById(R.id.lyricsEditText);
        Song songToLoadLyrics = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();

        if (songToLoadLyrics.getLyrics().length() != 0 && lyricsEditText != null) {
            lyricsEditText.setText(songToLoadLyrics.getLyrics());
        }

        Button undoButton = (Button) rootView.findViewById(R.id.undoButton);
        Button lyricsMenuButton = (Button) rootView.findViewById(R.id.lyricsMenuButton);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthButton = (int) Math.floor(width * 0.498);

        undoButton.setWidth(widthButton);
        undoButton.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        lyricsMenuButton.setWidth(widthButton);
        lyricsMenuButton.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.MULTIPLY);

        if (lyricsEditText != null) {
            if (SetManager.getInstance().getUseLargeTextSize()) {
                lyricsEditText.setTextSize(27);
            }
            else{
                lyricsEditText.setTextSize(18);
            }

            if (SetManager.getInstance().getAlignLyricsToCentre()) {
                lyricsEditText.setGravity(Gravity.CENTER_HORIZONTAL);
            }

            else{
                lyricsEditText.setGravity(Gravity.LEFT);
            }

            if (lyricsEditText.getText().toString().length() == 0){
                lyricsEditText.setHint(getActivity().getResources().getString(R.string.add_lyrics));
            }
            else {
                lyricsEditText.setHint("");
            }

            lyricsEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (lyricsEditText.getText().toString().length() == 0){
                        lyricsEditText.setHint(getActivity().getResources().getString(R.string.add_lyrics));
                    }
                    else {
                        lyricsEditText.setHint("");
                    }
                }
            });
        }
        return rootView;
    }
}