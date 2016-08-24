package metafire.stageready.fragments;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

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

public class StatsFragment extends Fragment implements AdapterView.OnItemSelectedListener, Serializable{

    private static final long serialVersionUID = 8130439973908447332L;
    private Song currentSong;

    /**
     * Called when the fragment's view is first created.
     * @param inflater           the layout inflater used to inflate the view
     * @param container          the view group used to inflate the view
     * @param savedInstanceState the saved instance state to load the view of the fragment
     * @return the view of the fragment
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_stats, container, false);
        TextView songNameTextView = (TextView) rootView.findViewById(R.id.songNameTextView);
        TextView songLengthTextView = (TextView) rootView.findViewById(R.id.songLengthTextView);
        TextView positionInSetTextView = (TextView) rootView.findViewById(R.id.positionInSetTextView);
        Spinner keySpinner = (Spinner) rootView.findViewById(R.id.keySpinner);
        final TextView tempoMarkingTextView = (TextView) rootView.findViewById(R.id.tempoMarkingTextView);
        final NumberPicker bpmNumberPicker = (NumberPicker) rootView.findViewById(R.id.bpmNumberPicker);

        int bpmNumberPickerDividerColor = Color.rgb(0,172,193);

        setDividerColor(bpmNumberPicker, bpmNumberPickerDividerColor);

        currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();

        songNameTextView.setText(String.format(getActivity().getResources().getString(R.string.name_colon), currentSong.getName()));
        songLengthTextView.setText(String.format(getActivity().getResources().getString(R.string.length), String.valueOf(currentSong.getMinutes()), String.valueOf(currentSong.getSeconds())));
        int incrementedIndex = SetManager.getInstance().getCurrentSet().getSlots().indexOf(currentSong) + 1;
        positionInSetTextView.setText(String.format(getActivity().getResources().getString(R.string.position_in_set), String.valueOf(incrementedIndex)));

        ArrayAdapter keyAdapter = ArrayAdapter.createFromResource(getContext(), R.array.keys, android.R.layout.simple_spinner_dropdown_item);
        keySpinner.setAdapter(keyAdapter);
        keySpinner.setOnItemSelectedListener(this);


        if (currentSong.getKey() != null && currentSong.getKey().length() != 0) {
            int indexOfKey = keyAdapter.getPosition(currentSong.getKey()); // keyAdapter can only contain keys, so call to get method is checked
            keySpinner.setSelection(indexOfKey);
        }

        bpmNumberPicker.setMinValue(1);
        bpmNumberPicker.setMaxValue(300);
        bpmNumberPicker.setWrapSelectorWheel(false);

        if (currentSong.getBpm() == 0){
            bpmNumberPicker.setValue(150);
        }

        else {
            bpmNumberPicker.setValue(currentSong.getBpm());
        }

        updateTempoMarkingTextView(bpmNumberPicker.getValue(), tempoMarkingTextView);


        bpmNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                currentSong.setBpm(newVal);
                updateTempoMarkingTextView(newVal, tempoMarkingTextView);
            }
        });

        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView key = (TextView) view;
        currentSong.setKey(key.getText().toString().trim());
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
        currentSong.setKey(null);
    }

    private void updateTempoMarkingTextView(int tempo, TextView tempoMarkingTextView){
        if (tempo >= 1 && tempo <= 19){
            tempoMarkingTextView.setText(getActivity().getResources().getString(R.string.larghissimo));
        }
        else if (tempo >= 20 && tempo <= 39){
            tempoMarkingTextView.setText(getActivity().getResources().getString(R.string.grave));
        }

        else if (tempo >= 40 && tempo <= 59){
            tempoMarkingTextView.setText(getActivity().getResources().getString(R.string.lento_largo));
        }

        else if (tempo >= 60 && tempo <= 65){
            tempoMarkingTextView.setText(getActivity().getResources().getString(R.string.larghetto));
        }

        else if (tempo >= 66 && tempo <= 75){
            tempoMarkingTextView.setText(getActivity().getResources().getString(R.string.adagio));
        }

        else if (tempo >= 76 && tempo <= 107){
            tempoMarkingTextView.setText(getActivity().getResources().getString(R.string.andante));
        }

        else if (tempo >= 108 && tempo <= 119){
            tempoMarkingTextView.setText(getActivity().getResources().getString(R.string.moderato));
        }

        else if (tempo >= 120 && tempo <= 139){
            tempoMarkingTextView.setText(getActivity().getResources().getString(R.string.allegro));
        }

        else if (tempo >= 140 && tempo <= 167){
            tempoMarkingTextView.setText(getActivity().getResources().getString(R.string.vivace));
        }

        else if (tempo >= 168 && tempo <= 199){
            tempoMarkingTextView.setText(getActivity().getResources().getString(R.string.presto));
        }

        else {
            tempoMarkingTextView.setText(getActivity().getResources().getString(R.string.prestissimo));
        }
    }

    private static void setDividerColor(NumberPicker picker, int newColor) {

        java.lang.reflect.Field[] numberPickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field field : numberPickerFields) {
            if (field.getName().equals("mSelectionDivider")) {
                field.setAccessible(true);
                try {
                    ColorDrawable newColorDrawable = new ColorDrawable(newColor);
                    field.set(picker, newColorDrawable);
                }
                catch (IllegalArgumentException e) {
                    // Empty catch block as there is no possibility of exception being thrown
                }
                catch (Resources.NotFoundException e) {
                    // Empty catch block as there is no possibility of exception being thrown
                }
                catch (IllegalAccessException e) {
                    // Empty catch block as the default blue divider color does not affect the user greatly
                }
                break;
            }
        }
    }
}